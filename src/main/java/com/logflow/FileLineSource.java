package com.logflow;
import java.io.*; import java.nio.charset.*; import java.nio.file.*; import java.util.*;
public class FileLineSource {
  private final Path path;
  public FileLineSource(Path path){this.path=path;}
  public List<String> readAll() throws IOException {try(BufferedReader r=Files.newBufferedReader(path,StandardCharsets.UTF_8)){return r.lines().toList();}}
}
