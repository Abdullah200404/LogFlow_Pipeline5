package com.logflow;
import java.util.*; import java.util.function.Predicate;
public class PredicateFilterStage implements Stage {
  private final Predicate<LogRecord> predicate;
  public PredicateFilterStage(Predicate<LogRecord> predicate){this.predicate=Objects.requireNonNull(predicate);}
  public List<LogRecord> process(LogRecord r){return predicate.test(r)?List.of(r):List.of();}
}
