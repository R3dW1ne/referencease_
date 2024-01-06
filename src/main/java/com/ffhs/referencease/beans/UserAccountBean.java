package com.ffhs.referencease.beans;

import com.ffhs.referencease.dto.UserAccountDTO;
import com.ffhs.referencease.services.interfaces.IUserAccountService;
import com.ffhs.referencease.utils.FrontendMessages;
import com.ffhs.referencease.valadators.ValidRegistrationUserDTOValidator;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Managed Bean Klasse für die Benutzerverwaltung, speziell für die Registrierung neuer Benutzer.
 * Diese Klasse bietet Funktionen zur Registrierung von Benutzerkonten durch Interaktion mit dem
 * `IUserAccountService`. Sie führt Validierungen durch und verarbeitet die Registrierung von
 * Benutzern im System.
 */
@Named
@Data
@RequestScoped
public class UserAccountBean implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = LogManager.getLogger(UserAccountBean.class);

  private final transient IUserAccountService userAccountService;

  private UserAccountDTO userAccountDTO;

  /**
   * Konstruktor, der eine Instanz von `IUserAccountService` injiziert.
   *
   * @param userAccountService Der Service für Benutzerkonten.
   */
  @Inject
  public UserAccountBean(IUserAccountService userAccountService) {
    this.userAccountService = userAccountService;
  }

  /**
   * Initialisiert die Bean nach der Konstruktion.
   */
  @PostConstruct
  public void init() {
    userAccountDTO = new UserAccountDTO();
  }

  /**
   * Registriert einen neuen Benutzer im System.
   * Überprüft die Einzigartigkeit der E-Mail-Adresse und validiert das Passwort,
   * bevor der Benutzer gespeichert wird. Setzt Erfolgs- oder Fehlermeldungen und
   * leitet entsprechend weiter.
   *
   * @return Ein String, der den Navigationspfad nach der Registrierung bestimmt.
   */
  public String register() {
    String message = "";
    if (userAccountService.emailExists(userAccountDTO.getEmail())) {
      message = "Es gibt bereits einen Benutzer mit dieser Email Adresse (Methoden-Validierung).";
      LOGGER.error(message);
      FrontendMessages.sendErrorMessageToFrontend("registerForm:messages", "Fehler", message);
      return null; // Bleibt auf der Registrierungsseite
    }
    // Erstellen einer Instanz des benutzerdefinierten Validators
    ValidRegistrationUserDTOValidator validator = new ValidRegistrationUserDTOValidator();

    // Manuelle Validierung durchführen
    if (!validator.isValid(userAccountDTO, null)) {
      // Validierung fehlgeschlagen
      message = "Das Passwort und die Passwortbestätigung stimmen nicht überein. (Custom-Validator)";
      LOGGER.error(message);
      FrontendMessages.sendErrorMessageToFrontend("registerForm:messages", "Fehler", message);
      return null; // Bleibt auf der Registrierungsseite
    }
    // Passwort-Verschlüsselung und User-Persistierung im UserService
    userAccountService.save(userAccountDTO);
    message =
        "Benutzeraccount " + userAccountDTO.getEmail() + " erfolgreich erstellt. Viel Spass! \n :)";
    // Passwort und confirmPassword zurücksetzen
    userAccountDTO.setPassword(null);
    userAccountDTO.setConfirmPassword(null);
    userAccountDTO.setEmail(null);

    // Setzen einer Erfolgsmeldung im Flash Scope
    FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    LOGGER.info(message);
    FrontendMessages.sendInfoMessageToFrontend(null, "Erfolg", message);
    return "login?faces-redirect=true"; // Weiterleitung zur Login-Seite nach erfolgreicher Registrierung
  }
}