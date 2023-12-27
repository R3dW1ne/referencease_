package com.ffhs.referencease.utils;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

public class FrontendMessages {

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
