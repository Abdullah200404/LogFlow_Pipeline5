package com.logflow;
public record FailedRecord(String rawInput,String stageName,String exception,String timestamp) {}
