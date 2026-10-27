package com.logflow;
public class StageException extends Exception {
  private final String stageName, rawInput;
  public StageException(String stageName,String rawInput,Throwable cause){super(cause);this.stageName=stageName;this.rawInput=rawInput;}
  public String stageName(){return stageName;} public String rawInput(){return rawInput;}
}
