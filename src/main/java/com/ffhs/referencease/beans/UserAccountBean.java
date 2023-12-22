package com.ffhs.referencease.beans;

import com.ffhs.referencease.dto.UserAccountDTO;
import com.ffhs.referencease.services.interfaces.IUserAccountService;
import com.ffhs.referencease.valadators.ValidRegistrationUserDTOValidator;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

@Named
@Data
@RequestScoped
public class UserAccountBean implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private final transient IUserAccountService userAccountService;

  private UserAccountDTO userAccountDTO;

  @Inject
  public UserAccountBean(IUserAccountService userAccountService) {
    this.userAccountService = userAccountService;
  }

  @PostConstruct
  public void init() {
    userAccountDTO = new UserAccountDTO();
  }

  public String register() {
    if (userAccountService.emailExists(userAccountDTO.getEmail())) {
      FacesContext.getCurrentInstance().addMessage("registerForm:messages",
                                                   new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                    "Fehler",
                                                                    "Es gibt bereits einen Benutzer mit dieser Email Adresse (Methoden-Validierung)."));
      return null; // Bleibt auf der Registrierungsseite
    }
    // Erstellen einer Instanz des benutzerdefinierten Validators
    ValidRegistrationUserDTOValidator validator = new ValidRegistrationUserDTOValidator();

    // Manuelle Validierung durchführen
    if (!validator.isValid(userAccountDTO, null)) {
      // Validierung fehlgeschlagen
      FacesContext.getCurrentInstance().addMessage(null,
                                                   new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                    "Das Passwort und die Passwortbestätigung stimmen nicht überein. (Custom-Validator)",
                                                                    null));
      return null; // Bleibt auf der Registrierungsseite
    }
    // Passwort-Verschlüsselung und User-Persistierung im UserService
    userAccountService.save(userAccountDTO);
    // Passwort und confirmPassword zurücksetzen
    userAccountDTO.setPassword(null);
    userAccountDTO.setConfirmPassword(null);
    userAccountDTO.setEmail(null);

    // Setzen einer Erfolgsmeldung im Flash Scope
    FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                                        "Benutzeraccount erfolgreich erstellt.",
                                                                        "Viel Spass! \n :)"));

    return "login?faces-redirect=true"; // Weiterleitung zur Login-Seite nach erfolgreicher Registrierung
  }
}