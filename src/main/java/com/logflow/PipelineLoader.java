package com.logflow;
import com.fasterxml.jackson.databind.*; import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.*; import java.nio.file.*; import java.util.*;
public class PipelineLoader {
  private final StageRegistry registry;
  public PipelineLoader(){registry=new StageRegistry();}
  @SuppressWarnings("unchecked") public Pipeline load(Path config) {
    try {
      Map<String,Object> root=new ObjectMapper(new YAMLFactory()).readValue(config.toFile(),Map.class);
      Map<String,Object> p=(Map<String,Object>)root.get("pipeline"); if(p==null)throw new PipelineException("Missing 'pipeline' section");
      Map<String,Object> source=(Map<String,Object>)p.get("source"), sink=(Map<String,Object>)p.get("sink");
      if(source==null||sink==null)throw new PipelineException("Both source and sink are required");
      if(!"file".equals(source.get("type"))||!"file".equals(sink.get("type")))throw new PipelineException("Only file source and sink are supported");
      Map<String,Object> sp=(Map<String,Object>)source.getOrDefault("params",Map.of()), op=(Map<String,Object>)sink.getOrDefault("params",Map.of());
      String in=required(sp,"path"), out=required(op,"path");
      List<Map<String,Object>> specs=(List<Map<String,Object>>)p.get("stages"); if(specs==null||specs.isEmpty())throw new PipelineException("Pipeline stages must not be empty");
      PipelineBuilder b=PipelineBuilder.from(new FileLineSource(Path.of(in)));
      for(Map<String,Object> spec:specs){String type=required(spec,"type");Map<String,Object> params=(Map<String,Object>)spec.getOrDefault("params",Map.of());b.via(registry.create(type,params));}
      String policy=String.valueOf(p.getOrDefault("errorPolicy","FAIL_FAST")); String dl=(String)p.get("deadLetter");
      return b.to(new FileSink(Path.of(out)),policy,dl==null?null:Path.of(dl));
    } catch(PipelineException e){throw e;}catch(Exception e){throw new PipelineException("Configuration error in "+config+": "+e.getMessage(),e);}
  }
  private String required(Map<String,Object> map,String key){Object v=map.get(key);if(!(v instanceof String)||((String)v).isBlank())throw new PipelineException("Missing required parameter: "+key);return (String)v;}
}
