package com.ffhs.referencease.exceptionhandling;

import lombok.Getter;

@Getter
public class OperationResult<T> {

  private final boolean success;
  private final T data;
  private final String errorMessage;

  private OperationResult(boolean success, T data, String errorMessage) {
    this.success = success;
    this.data = data;
    this.errorMessage = errorMessage;
  }

  public static <T> OperationResult<T> success(T data) {
    return new OperationResult<>(true, data, null);
  }

  public static <T> OperationResult<T> failure(String errorMessage) {
    return new OperationResult<>(false, null, errorMessage);
  }
}
