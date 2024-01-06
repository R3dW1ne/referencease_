package com.ffhs.referencease.exceptionhandling;

import lombok.Getter;

/**
 * Eine Hilfsklasse zur Repräsentation des Ergebnisses einer Operation. Diese Klasse wird verwendet,
 * um eine Operation als Erfolg oder Misserfolg zu kennzeichnen und enthält entweder das Ergebnis
 * der Operation oder eine Fehlermeldung.
 *
 * @param <T> Der Typ des Datenobjekts, das im Falle eines erfolgreichen Operationsergebnisses
 *            zurückgegeben wird.
 */
@Getter
public class OperationResult<T> {

  private final boolean success;
  private final T data;
  private final String errorMessage;

  /**
   * Privater Konstruktor zur Erstellung eines OperationResult-Objekts.
   *
   * @param success      Ein boolean-Wert, der angibt, ob die Operation erfolgreich war.
   * @param data         Das Datenobjekt, das bei Erfolg zurückgegeben wird.
   * @param errorMessage Die Fehlermeldung, die bei einem Misserfolg zurückgegeben wird.
   */
  private OperationResult(boolean success, T data, String errorMessage) {
    this.success = success;
    this.data = data;
    this.errorMessage = errorMessage;
  }

  /**
   * Erstellt ein erfolgreiches Operationsergebnis mit den bereitgestellten Daten.
   *
   * @param <T>  Der Typ des Datenobjekts.
   * @param data Das Datenobjekt.
   * @return Ein erfolgreiches OperationResult-Objekt.
   */
  public static <T> OperationResult<T> success(T data) {
    return new OperationResult<>(true, data, null);
  }

  /**
   * Erstellt ein erfolgloses Operationsergebnis mit einer Fehlermeldung.
   *
   * @param <T>          Der Typ des Datenobjekts.
   * @param errorMessage Die Fehlermeldung.
   * @return Ein erfolgloses OperationResult-Objekt.
   */
  public static <T> OperationResult<T> failure(String errorMessage) {
    return new OperationResult<>(false, null, errorMessage);
  }
}
