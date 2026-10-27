package com.logflow;
import java.time.*; import java.time.format.DateTimeFormatter; import java.util.regex.*;
public class ParserStage {
  private static final Pattern CLF=Pattern.compile("^(\\S+) \\S+ \\S+ \\[([^]]+)] \"(\\S+) (\\S+) [^\"]*\" (\\d{3}) (\\S+)$");
  public LogRecord parse(String line) {
    Matcher m=CLF.matcher(line);
    if(!m.matches()) throw new IllegalArgumentException("Malformed CLF log line");
    String ts;
    try { ts=OffsetDateTime.parse(m.group(2),DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z",java.util.Locale.ENGLISH)).toInstant().toString(); }
    catch(Exception e){ throw new IllegalArgumentException("Invalid timestamp: "+m.group(2)); }
    long bytes=m.group(6).equals("-")?0:Long.parseLong(m.group(6));
    return new LogRecord(ts,m.group(1),m.group(3),m.group(4),Integer.parseInt(m.group(5)),bytes);
  }
}
