package com.ffhs.referencease.beans;

import com.ffhs.referencease.entities.UserAccount;
import com.ffhs.referencease.entityservices.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import lombok.Data;

@Named
@Data
@RequestScoped
public class UserBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private final transient UserService userService;

//  @Inject
//  private transient UserService userService; // Service-Klasse zum Interagieren mit der DB

  private transient UserAccount userAccount; // Getter und Setter via Lombok
  @Inject
  public UserBean(UserService userService) {
    this.userService = userService;
  }


  @PostConstruct
  public void init() {
    userAccount = new UserAccount();
  }

  public String register() {
    if (!userAccount.getPassword().equals(userAccount.getConfirmPassword())) {
      FacesContext.getCurrentInstance().addMessage(null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Passwords must match."));
      return null; // Bleibt auf der Registrierungsseite
    }
    // Passwort-Verschlüsselung und User-Persistierung im UserService
    userService.registerNewUser(userAccount);

    // Passwort und confirmPassword zurücksetzen
    userAccount.setPassword(null);
    userAccount.setConfirmPassword(null);

    return "login?faces-redirect=true"; // Weiterleitung zur Login-Seite nach erfolgreicher Registrierung
  }

  public void saveUserAccount() {
    userService.save(userAccount);
    // Hier könnten Sie auch eine Erfolgsmeldung anzeigen oder zu einer anderen Seite navigieren
  }
}