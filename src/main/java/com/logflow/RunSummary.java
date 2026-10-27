package com.logflow;
import java.util.*;
public class RunSummary {
  public long incoming, output, skipped, deadLetter, elapsedMillis;
  public final Map<String,Long> droppedByStage=new LinkedHashMap<>();
  public void dropped(String stage){droppedByStage.merge(stage,1L,Long::sum);}
  public void print(){System.out.println("RunSummary: incoming="+incoming+", output="+output+", skipped="+skipped+", deadLetter="+deadLetter+", elapsedMs="+elapsedMillis+", droppedByStage="+droppedByStage);}
}
