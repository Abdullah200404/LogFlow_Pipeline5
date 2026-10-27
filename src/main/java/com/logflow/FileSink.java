package com.logflow;
import java.io.*; import java.nio.charset.StandardCharsets; import java.nio.file.*; import java.util.*;
public class FileSink implements AutoCloseable {
  private final Path path; private BufferedWriter writer;
  public FileSink(Path path){this.path=path;}
  public void open() throws IOException {Path parent=path.toAbsolutePath().getParent(); if(parent!=null) Files.createDirectories(parent); writer=Files.newBufferedWriter(path,StandardCharsets.UTF_8);}
  public void write(String line) throws IOException {if(writer==null) throw new IllegalStateException("FileSink is not open"); writer.write(line); writer.newLine();}
  public void close() throws IOException {if(writer!=null){writer.close();writer=null;}}
}
