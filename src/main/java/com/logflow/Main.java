package com.logflow;
import java.nio.file.*;
public class Main {
  public static void main(String[] args) {
    try {
      if(args.length==2 && args[0].equals("--config")){new PipelineLoader().load(Path.of(args[1])).run();return;}
      System.err.println("Usage: java -jar logflow-pipeline-5.0.0.jar --config configs/error-report.yaml");
      System.exit(2);
    } catch(Exception e){System.err.println("ERROR: "+e.getMessage());System.exit(1);}
  }
}
