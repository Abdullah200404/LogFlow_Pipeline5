package com.logflow;
import java.util.*;
public class ValidationStage implements Stage {
  public List<LogRecord> process(LogRecord r){if(!r.valid())throw new IllegalArgumentException("Record failed validation (timestamp, IP, status or bytes)");return List.of(r);}
}
