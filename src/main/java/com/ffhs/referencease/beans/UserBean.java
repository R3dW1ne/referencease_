package com.ffhs.referencease.beans;

import com.ffhs.referencease.dto.UserAccountDTO;
import com.ffhs.referencease.services.UserAccountService;
import com.ffhs.referencease.services.interfaces.IUserAccountService;
import com.ffhs.referencease.valadators.annotations.ValidRegistrationUserDTO;
import com.ffhs.referencease.valadators.group_interfaces.RegistrationGroup;
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
public class UserBean implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private final transient IUserAccountService userAccountService;

  @ValidRegistrationUserDTO
  private UserAccountDTO userAccountDTO; // Getter und Setter via Lombok

  @Inject
  public UserBean(IUserAccountService userAccountService) {
    this.userAccountService = userAccountService;
  }

  @PostConstruct
  public void init() {
    userAccountDTO = new UserAccountDTO();
  }

  public String registerWithValidRegistrationUserDTOValidator(@ValidRegistrationUserDTO(groups = RegistrationGroup.class, message = "Die Passwörter stimmen nicht überein. (@ValidRegistrationUserDTO Validation)") UserAccountDTO userAccountDTO){
    return register();
  }


  public String register() {
    if (userAccountService.emailExists(userAccountDTO.getEmail())) {
      FacesContext.getCurrentInstance().addMessage("registerForm:messages",
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler", "Es gibt bereits einen Benutzer mit dieser Email Adresse."));
      return null; // Bleibt auf der Registrierungsseite
    }
    if (!userAccountDTO.getPassword().equals(userAccountDTO.getConfirmPassword())) {
      FacesContext.getCurrentInstance().addMessage("registerForm:password",
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler",
              "Das Passwort und die Passwortbestätigung stimmen nicht überein."));
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
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Benutzeraccount erfolgreich erstellt.", "Viel Spass! \n :)"));

    return "login?faces-redirect=true"; // Weiterleitung zur Login-Seite nach erfolgreicher Registrierung
  }



  public void saveUserAccount() {
    userAccountService.save(userAccountDTO);
    // Hier könnten Sie auch eine Erfolgsmeldung anzeigen oder zu einer anderen Seite navigieren
  }
}