package com.logflow;
import java.util.*;
public class ProjectionStage implements Stage {
  private final List<String> fields;
  public ProjectionStage(String... fields){this.fields=List.of(fields); for(String f:this.fields) if(!List.of("timestamp","clientIp","method","path","status","bytes").contains(f)) throw new IllegalArgumentException("Unknown field: "+f);}
  public List<LogRecord> process(LogRecord r){return List.of(r.project(fields));}
  public List<String> fields(){return fields;}
}
