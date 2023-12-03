//package com.ffhs.referencease.services;
//
//import com.ffhs.referencease.dao.interfaces.IUserAccountDAO;
//import com.ffhs.referencease.entities.Role;
//import com.ffhs.referencease.entities.UserAccount;
//import com.ffhs.referencease.services.interfaces.IUserAccountService;
//import com.ffhs.referencease.utils.PBKDF2Hash;
//import jakarta.ejb.Stateless;
//import jakarta.inject.Inject;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.TypedQuery;
//import jakarta.transaction.Transactional;
//import java.util.Optional;
//
//@Stateless
//public class UserAccountService_backup implements IUserAccountService {
//
//  @PersistenceContext
//  private EntityManager entityManager;
//
//  @Inject
//  private IUserAccountDAO userAccountDAO;
//
//  @Transactional
//  public boolean registerNewUser(UserAccount userAccount) {
//    // Rolle und Mitarbeiter zuweisen
//    Role userRole = entityManager.find(Role.class, 1);
//    userAccount.setRole(userRole);
//
//    // Passwort verschlüsseln
//    userAccount.setPassword(PBKDF2Hash.createHash(userAccount.getPassword()));
//
//    // Benutzer speichern
//    entityManager.persist(userAccount);
//
//    // Erfolgreiche Registrierung
//    return true;
//  }
//
//
//  public boolean emailExists(String email) {
//    TypedQuery<Long> query = entityManager.createQuery(
//        "SELECT COUNT(u) FROM UserAccount u WHERE u.email = :email", Long.class);
//    query.setParameter("email", email);
//    Long count = query.getSingleResult();
//    return count > 0;
//  }
//
//
//  public void save(UserAccount userAccount) {
//    if (userAccount.getUserId() == null) {
//      // Neuer Eintrag
//      entityManager.persist(userAccount);
//    } else {
//      // Aktualisierung eines bestehenden Eintrags
//      entityManager.merge(userAccount);
//    }
//  }
//
//
////  public String encryptPassword(String password) {
////    return BCrypt.hashpw(password, BCrypt.gensalt());
////  }
////
////  // Überprüfen des Passworts
////  public boolean checkPassword(String candidate, String encryptedPassword) {
////    return BCrypt.checkpw(candidate, encryptedPassword);
////  }
//
//  public Optional<UserAccount> getUserByEmail(String email) {
//    TypedQuery<UserAccount> query = entityManager.createQuery(
//        "SELECT u FROM UserAccount u WHERE u.email = :email", UserAccount.class);
//    query.setParameter("email", email);
//    UserAccount userAccount = query.getSingleResult();
//    return Optional.of(userAccount);
//  }
//
////  public Optional<UserAccount> getUserById(UUID userId) {
////    TypedQuery<UserAccount> query = entityManager.createQuery(
////        "SELECT u FROM UserAccount u WHERE u.userId = :userId", UserAccount.class);
////    query.setParameter("userId", userId);
////    UserAccount userAccount = query.getSingleResult();
////    return Optional.of(userAccount);
////  }
//
////  public Optional<UserAccount> get(UUID id) {
////    return Optional.ofNullable(entityManager.find(UserAccount.class, id));
////  }
//
////  public void updateUser(UserAccount userAccount) {
////    Optional<UserAccount> userToUpdate = get(userAccount.getUserId());
////    if (userToUpdate.isPresent()) {
////      entityManager.getTransaction().begin();
////      entityManager.merge(userAccount);
////      entityManager.getTransaction().commit();
////    }
////  }
//}
//
