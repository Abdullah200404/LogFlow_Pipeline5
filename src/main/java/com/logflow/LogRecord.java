package com.logflow;
import java.time.Instant;
import java.util.*;
public record LogRecord(String timestamp, String clientIp, String method, String path, int status, long bytes) {
  public String field(String name) {
    return switch(name) {
      case "timestamp" -> timestamp; case "clientIp" -> clientIp; case "method" -> method;
      case "path" -> path; case "status" -> Integer.toString(status); case "bytes" -> Long.toString(bytes);
      default -> throw new IllegalArgumentException("Unknown field: " + name);
    };
  }
  public LogRecord project(List<String> fields) {
    return new LogRecord(fields.contains("timestamp")?timestamp:"", fields.contains("clientIp")?clientIp:"",
      fields.contains("method")?method:"", fields.contains("path")?path:"",
      fields.contains("status")?status:0, fields.contains("bytes")?bytes:0);
  }
  public String format(List<String> fields) {
    return String.join(",", fields.stream().map(f -> f + "=" + field(f)).toList());
  }
  public boolean valid() {
    try { Instant.parse(timestamp); } catch(Exception e) { return false; }
    if (clientIp == null || !clientIp.matches("[0-9a-fA-F:.]+")) return false;
    return status >= 100 && status <= 599 && bytes >= 0;
  }
}
