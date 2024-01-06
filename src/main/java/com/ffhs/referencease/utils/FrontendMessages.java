package com.ffhs.referencease.utils;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

/**
 * Hilfsklasse zur Handhabung von Nachrichten in der Benutzeroberfläche. Diese Klasse bietet
 * statische Methoden, um Informations-, Warnungs- und Fehlermeldungen an die Benutzeroberfläche zu
 * senden. Sie verwendet die FacesContext-API von Jakarta Faces, um Nachrichten innerhalb der
 * JSF-Anwendung zu verwalten. Der Konstruktor ist privat, da diese Klasse nicht instanziiert werden
 * soll.
 */
public class FrontendMessages {

  /**
   * Privater Konstruktor, der eine Ausnahme auslöst, um die Instanziierung der Klasse zu
   * verhindern.
   *
   * @throws IllegalStateException wenn versucht wird, die Klasse zu instanzieren.
   */
  private FrontendMessages() {
    throw new IllegalStateException("Messages class");
  }

  /**
   * Sendet eine INFO Nachricht an die Benutzeroberfläche.
   *
   * @param summary Die Nachricht, die angezeigt werden soll.
   */
  public static void sendInfoMessageToFrontend(String clientId, String summary, String details) {
    FacesContext.getCurrentInstance()
        .addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_INFO, summary, details));
  }

  /**
   * Sendet eine WARNING Nachricht an die Benutzeroberfläche.
   *
   * @param summary Die Nachricht, die angezeigt werden soll.
   */
  public static void sendWarningMessageToFrontend(String clientId, String summary, String details) {
    FacesContext.getCurrentInstance()
        .addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_WARN, summary, details));
  }

  /**
   * Sendet eine ERROR Nachricht an die Benutzeroberfläche.
   *
   * @param summary Die Nachricht, die angezeigt werden soll.
   */
  public static void sendErrorMessageToFrontend(String clientId, String summary, String details) {
    FacesContext.getCurrentInstance()
        .addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, details));
  }
}
