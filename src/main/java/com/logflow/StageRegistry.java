package com.logflow;
import java.util.*; import java.util.function.Function;
public class StageRegistry {
  private final Map<String,Function<Map<String,Object>,Stage>> factories=new HashMap<>();
  public StageRegistry(){register("filter",p->{int min=num(p,"minStatus",400);String prefix=str(p,"pathPrefix","");return new PredicateFilterStage(r->r.status()>=min && r.path().startsWith(prefix));});register("project",p->new ProjectionStage(str(p,"fields","timestamp,clientIp,status,path").split(",")));register("validate",p->new ValidationStage());}
  public void register(String name,Function<Map<String,Object>,Stage> factory){factories.put(name,factory);}
  public Stage create(String name,Map<String,Object> params){Function<Map<String,Object>,Stage> f=factories.get(name);if(f==null)throw new PipelineException("Unknown stage type: "+name);try{return f.apply(params==null?Map.of():params);}catch(RuntimeException e){throw new PipelineException("Invalid parameters for stage '"+name+"': "+e.getMessage(),e);}}
  static int num(Map<String,Object> p,String k,int d){Object v=p.get(k);if(v==null)return d;if(!(v instanceof Number))throw new IllegalArgumentException(k+" must be numeric");return ((Number)v).intValue();}
  static String str(Map<String,Object> p,String k,String d){Object v=p.get(k);if(v==null)return d;if(v instanceof List<?> l)return String.join(",",l.stream().map(Object::toString).toList());if(!(v instanceof String))throw new IllegalArgumentException(k+" must be a string or list");return (String)v;}
}
