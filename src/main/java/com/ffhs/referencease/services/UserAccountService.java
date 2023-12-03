package com.ffhs.referencease.services;

import com.ffhs.referencease.dao.interfaces.IUserAccountDAO;
import com.ffhs.referencease.dto.UserAccountDTO;
import com.ffhs.referencease.entities.UserAccount;
import com.ffhs.referencease.exceptionhandling.PositionNotFoundException;
import com.ffhs.referencease.services.interfaces.IRoleService;
import com.ffhs.referencease.services.interfaces.IUserAccountService;
import com.ffhs.referencease.utils.PBKDF2Hash;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class UserAccountService implements IUserAccountService{

  private static final Logger LOGGER = LoggerFactory.getLogger(UserAccountService.class);


  private final IRoleService roleService;


  private final IUserAccountDAO userAccountDAO;

  @Inject
  public UserAccountService(IRoleService roleService, IUserAccountDAO userAccountDAO) {
    this.roleService = roleService;
    this.userAccountDAO = userAccountDAO;
  }

  @Override
  public UserAccountDTO findById(UUID id) throws PositionNotFoundException {
    Optional<UserAccount> userAccount = userAccountDAO.findById(id);
    if (userAccount.isPresent()) {
      UserAccount ua = userAccount.get();
      UserAccountDTO dto = new UserAccountDTO();
      dto.setEmail(ua.getEmail());
      dto.setUserId(ua.getUserId());
      return dto;
    } else {
      throw new PositionNotFoundException("Benutzer mit ID " + id + " nicht gefunden.");
    }
  }

  @Override
  public UserAccountDTO getUserByEmail(String email) throws PositionNotFoundException {
    Optional<UserAccount> userAccount = userAccountDAO.findByEmail(email);
    if (userAccount.isPresent()) {
      UserAccount ua = userAccount.get();
      UserAccountDTO dto = new UserAccountDTO();
      dto.setEmail(ua.getEmail());
      dto.setUserId(ua.getUserId());
      return dto;
    } else {
      throw new PositionNotFoundException("Benutzer mit Email " + email + " nicht gefunden.");
    }
  }

  @Override
  public void save(UserAccountDTO dto) {
    UserAccount userAccount = new UserAccount();
    userAccount.setEmail(dto.getEmail());
    userAccount.setPassword(PBKDF2Hash.createHash(dto.getPassword()));
    try {
      userAccount.setRole(roleService.findByRoleName("ROLE_USER"));
    } catch (PositionNotFoundException e) {
      LOGGER.info(e.getMessage());
    }
    userAccountDAO.save(userAccount);
  }

  @Override
  public boolean passwordMatches(String email, String password) {
    return userAccountDAO.passwordMatches(email, password);
  }

//  public void save(UserAccount userAccount) {
//    if (userAccount.getUserId() == null) {
//      // Neuer Eintrag
//      entityManager.persist(userAccount);
//    } else {
//      // Aktualisierung eines bestehenden Eintrags
//      entityManager.merge(userAccount);
//    }
//  }

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

  @Override
  public boolean emailExists(String email) {
//    TypedQuery<Long> query = entityManager.createQuery(
//        "SELECT COUNT(u) FROM UserAccount u WHERE u.email = :email", Long.class);
//    query.setParameter("email", email);
//    Long count = query.getSingleResult();
//    return count > 0;
    return userAccountDAO.emailExists(email);
  }

//  public String encryptPassword(String password) {
//    return BCrypt.hashpw(password, BCrypt.gensalt());
//  }
//
//  // Überprüfen des Passworts
//  public boolean checkPassword(String candidate, String encryptedPassword) {
//    return BCrypt.checkpw(candidate, encryptedPassword);
//  }



//  public Optional<UserAccount> getUserById(UUID userId) {
//    TypedQuery<UserAccount> query = entityManager.createQuery(
//        "SELECT u FROM UserAccount u WHERE u.userId = :userId", UserAccount.class);
//    query.setParameter("userId", userId);
//    UserAccount userAccount = query.getSingleResult();
//    return Optional.of(userAccount);
//  }

//  public Optional<UserAccount> get(UUID id) {
//    return Optional.ofNullable(entityManager.find(UserAccount.class, id));
//  }

//  public void updateUser(UserAccount userAccount) {
//    Optional<UserAccount> userToUpdate = get(userAccount.getUserId());
//    if (userToUpdate.isPresent()) {
//      entityManager.getTransaction().begin();
//      entityManager.merge(userAccount);
//      entityManager.getTransaction().commit();
//    }
//  }
}

