package com.logflow;
import org.junit.jupiter.api.Test; import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
class PipelineTest {
 @Test void filterKeepsMatchingRecord(){
  LogRecord r=new LogRecord("2026-10-20T10:00:00Z","127.0.0.1","GET","/api",404,12);
  assertEquals(1,new PredicateFilterStage(x->x.status()>=400).process(r).size());
  assertEquals(0,new PredicateFilterStage(x->x.status()<400).process(r).size());
 }
 @Test void projectionRemovesUnselectedFields(){
  LogRecord r=new LogRecord("2026-10-20T10:00:00Z","127.0.0.1","GET","/api",404,12);
  LogRecord p=new ProjectionStage("timestamp","status").process(r).get(0);
  assertEquals("",p.path()); assertEquals(404,p.status());
 }
 @Test void registryRejectsUnknownStage(){
  assertThrows(PipelineException.class,()->new StageRegistry().create("nope",Map.of()));
 }
}
