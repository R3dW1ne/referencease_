package com.ffhs.referencease.entityservices;

import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.entities.Role;
import com.ffhs.referencease.entities.UserAccount;
import jakarta.ejb.Stateless;
import jakarta.faces.context.FacesContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt;

@Stateless
public class UserService {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  public boolean registerNewUser(UserAccount userAccount) {
    // Überprüfen, ob bereits ein Benutzer mit derselben E-Mail-Adresse existiert
    if (emailExists(userAccount.getEmail())) {
      // Logik, um zu handhaben, wenn der Benutzer bereits existiert
      // Zum Beispiel: Rückgabe von 'false' oder Auslösen einer Exception
      return false;
    }

    // Rolle und Mitarbeiter zuweisen
    Role userRole = entityManager.find(Role.class, 1);
    userAccount.setRole(userRole);

//    Employee employee = new Employee();
//    // Setzen Sie die Eigenschaften des Mitarbeiters basierend auf den übergebenen Details
//    employee.setFirstName(employeeDetails.getFirstName());
//    employee.setLastName(employeeDetails.getLastName());
//    // usw.
//    userAccount.setEmployee(employee);

    // Passwort verschlüsseln
    userAccount.setPassword(encryptPassword(userAccount.getPassword()));

    // Benutzer speichern
    entityManager.persist(userAccount);

    // Erfolgreiche Registrierung
    return true;
  }


  private boolean emailExists(String email) {
    TypedQuery<Long> query = entityManager.createQuery(
        "SELECT COUNT(u) FROM UserAccount u WHERE u.email = :email", Long.class);
    query.setParameter("email", email);
    Long count = query.getSingleResult();
    return count > 0;
  }


  public void save(UserAccount userAccount) {
    if (userAccount.getUserId() == null) {
      // Neuer Eintrag
      entityManager.persist(userAccount);
    } else {
      // Aktualisierung eines bestehenden Eintrags
      entityManager.merge(userAccount);
    }
  }


  private String encryptPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }

  // Überprüfen des Passworts
  public boolean checkPassword(String candidate, String encryptedPassword) {
    return BCrypt.checkpw(candidate, encryptedPassword);
  }

  public Optional<UserAccount> getUserByEmail(String email) {
    TypedQuery<UserAccount> query = entityManager.createQuery(
        "SELECT u FROM UserAccount u WHERE u.email = :email", UserAccount.class);
    query.setParameter("email", email);
    UserAccount userAccount = query.getSingleResult();
    return Optional.of(userAccount);
  }

  public Optional<UserAccount> getUserById(Long userId) {
    TypedQuery<UserAccount> query = entityManager.createQuery(
        "SELECT u FROM UserAccount u WHERE u.userId = :userId", UserAccount.class);
    query.setParameter("userId", userId);
    UserAccount userAccount = query.getSingleResult();
    return Optional.of(userAccount);
  }

  public Optional<UserAccount> get(Long id) {
    return Optional.ofNullable(entityManager.find(UserAccount.class, id));
  }

  public void updateUser(UserAccount userAccount) {
    Optional<UserAccount> userToUpdate = get(userAccount.getUserId());
    if (userToUpdate.isPresent()) {
      entityManager.getTransaction().begin();
      entityManager.merge(userAccount);
      entityManager.getTransaction().commit();
    }
  }
}

