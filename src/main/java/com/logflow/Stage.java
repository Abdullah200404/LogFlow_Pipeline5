package com.logflow;
import java.util.List;
public interface Stage { List<LogRecord> process(LogRecord record) throws Exception; default String name(){return getClass().getSimpleName();} }
