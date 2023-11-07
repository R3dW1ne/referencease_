package com.ffhs.referencease.entityservices;

import com.ffhs.referencease.entities.UserAccount;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;

@Stateless
public class UserService {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  public void registerNewUser(UserAccount userAccount) {
    // Passwort verschlüsseln
    String encryptedPassword = encryptPassword(userAccount.getPassword());
    userAccount.setPassword(encryptedPassword);

    // confirmPassword zurücksetzen, bevor der Benutzer gespeichert wird
    userAccount.setConfirmPassword(null);

    // Benutzer speichern
    save(userAccount);
  }


  @Transactional
  public void save(UserAccount userAccount) {
    if (userAccount.getUserId() == null) {
      // Neuer Eintrag
      entityManager.persist(userAccount);
    } else {
      // Aktualisierung eines bestehenden Eintrags
      entityManager.merge(userAccount);
    }
  }


  @Transactional
  private String encryptPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }
  @Transactional
  // Überprüfen des Passworts
  public boolean checkPassword(String candidate, String encryptedPassword) {
    return BCrypt.checkpw(candidate, encryptedPassword);
  }
}

