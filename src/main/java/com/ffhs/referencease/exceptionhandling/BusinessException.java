package com.ffhs.referencease.exceptionhandling;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.primefaces.PrimeFaces;

public class BusinessException extends RuntimeException {

  public BusinessException(String message) {
    super(message);
  }

  public BusinessException(String message, Throwable cause) {
    super(message, cause);
  }

  // Weitere Konstruktoren oder Methoden können je nach Bedarf hinzugefügt werden
}
