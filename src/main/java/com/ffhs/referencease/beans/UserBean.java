package com.ffhs.referencease.beans;

import com.ffhs.referencease.entities.UserAccount;
import com.ffhs.referencease.entityservices.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import lombok.Data;

@Named
@Data
@RequestScoped
public class UserBean implements Serializable {

  private static final long serialVersionUID = 1L;

  @Inject
  private transient UserService userService; // Service-Klasse zum Interagieren mit der DB

  private transient UserAccount userAccount; // Getter und Setter via Lombok


  @PostConstruct
  public void init() {
    userAccount = new UserAccount();
  }

  public String register() {
    // Registrierungslogik hier
    userService.registerNewUser(userAccount);
    return "login?faces-redirect=true"; // Weiterleitung zur Login-Seite nach erfolgreicher Registrierung
  }

  public void saveUserAccount() {
    userService.save(userAccount);
    // Hier könnten Sie auch eine Erfolgsmeldung anzeigen oder zu einer anderen Seite navigieren
  }
}