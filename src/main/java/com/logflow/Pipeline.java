package com.logflow;
import java.io.*; import java.nio.file.*; import java.time.Instant; import java.util.*;
public class Pipeline {
  private final FileLineSource source; private final List<Stage> stages; private final FileSink sink; private final String errorPolicy; private final Path deadLetterPath;
  Pipeline(FileLineSource s,List<Stage> st,FileSink out,String policy,Path dl){source=s;stages=List.copyOf(st);sink=out;errorPolicy=policy;deadLetterPath=dl;}
  public RunSummary run() {
    long start=System.nanoTime(); RunSummary sum=new RunSummary(); FileSink dead=deadLetterPath==null?null:new FileSink(deadLetterPath);
    try {
      List<String> lines=source.readAll(); sum.incoming=lines.size(); sink.open(); if(dead!=null) dead.open();
      for(String line:lines) {
        List<LogRecord> current;
        try { current=List.of(new ParserStage().parse(line)); }
        catch(Exception ex){handle(line,"parse",ex,sum,dead);continue;}
        for(Stage stage:stages) {
          List<LogRecord> next=new ArrayList<>();
          for(LogRecord r:current) try {next.addAll(stage.process(r));}
          catch(Exception ex){handle(line,stage.name(),ex,sum,dead);}
          if(next.size()<current.size()) sum.dropped(stage.name());
          current=next; if(current.isEmpty()) break;
        }
        for(LogRecord r:current){sink.write(r.format(List.of("timestamp","clientIp","method","path","status","bytes")));sum.output++;}
      }
    } catch(IOException e){throw new PipelineException("Pipeline I/O failure: "+e.getMessage(),e);}
    finally {try{sink.close();if(dead!=null)dead.close();}catch(IOException e){throw new PipelineException("Could not close output: "+e.getMessage(),e);} sum.elapsedMillis=(System.nanoTime()-start)/1_000_000;}
    sum.print(); return sum;
  }
  private void handle(String raw,String stage,Exception e,RunSummary s,FileSink dead) {
    if(errorPolicy.equals("FAIL_FAST")) throw new PipelineException("Failure at stage '"+stage+"': "+e.getMessage(),e);
    if(errorPolicy.equals("SKIP")){s.skipped++;return;}
    if(errorPolicy.equals("DEAD_LETTER")){if(dead==null)throw new PipelineException("DEAD_LETTER policy requires deadLetter path");try{dead.write(new FailedRecord(raw,stage,e.getMessage(),Instant.now().toString()).toString());s.deadLetter++;}catch(IOException io){throw new PipelineException("Cannot write dead-letter record",io);}return;}
    throw new PipelineException("Unsupported error policy: "+errorPolicy);
  }
}
