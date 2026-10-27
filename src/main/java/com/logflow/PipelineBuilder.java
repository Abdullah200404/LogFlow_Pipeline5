package com.logflow;
import java.nio.file.Path; import java.util.*;
public class PipelineBuilder {
  private final FileLineSource source; private final List<Stage> stages=new ArrayList<>();
  private PipelineBuilder(FileLineSource source){this.source=source;}
  public static PipelineBuilder from(FileLineSource source){return new PipelineBuilder(source);}
  public PipelineBuilder via(Stage stage){stages.add(stage);return this;}
  public Pipeline to(FileSink sink){return new Pipeline(source,stages,sink,"FAIL_FAST",null);}
  public Pipeline to(FileSink sink,String policy,Path deadLetter){return new Pipeline(source,stages,sink,policy,deadLetter);}
}
