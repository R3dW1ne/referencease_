package com.ffhs.referencease.services;

import com.ffhs.referencease.dao.interfaces.IUserAccountDAO;
import com.ffhs.referencease.dto.UserAccountDTO;
import com.ffhs.referencease.entities.UserAccount;
import com.ffhs.referencease.exceptionhandling.PositionNotFoundException;
import com.ffhs.referencease.services.interfaces.IRoleService;
import com.ffhs.referencease.services.interfaces.IUserAccountService;
import com.ffhs.referencease.utils.PWHash;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.Optional;
import java.util.UUID;

/**
 * Service-Klasse zur Verwaltung von Benutzerkonten. Diese Klasse stellt Methoden zur Verfügung, um
 * Benutzerkonten zu suchen, zu speichern und deren Passwörter zu validieren. Sie nutzt DAO-Klassen
 * für die Datenbankinteraktion und implementiert das IUserAccountService Interface.
 */
@Stateless
public class UserAccountService implements IUserAccountService {

  private final IRoleService roleService;
  private final IUserAccountDAO userAccountDAO;

  /**
   * Konstruktor, der den RoleService und UserAccountDAO injiziert.
   *
   * @param roleService    Der Service für Benutzerrollen.
   * @param userAccountDAO Das Data Access Object für Benutzerkonten.
   */
  @Inject
  public UserAccountService(IRoleService roleService, IUserAccountDAO userAccountDAO) {
    this.roleService = roleService;
    this.userAccountDAO = userAccountDAO;
  }

  /**
   * Sucht ein Benutzerkonto anhand seiner ID.
   *
   * @param id Die UUID des Benutzerkontos.
   * @return Ein DTO des Benutzerkontos.
   * @throws PositionNotFoundException Wenn kein Benutzerkonto mit der gegebenen ID gefunden wird.
   */
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

  /**
   * Sucht ein Benutzerkonto anhand seiner E-Mail-Adresse.
   *
   * @param email Die E-Mail-Adresse des Benutzerkontos.
   * @return Ein DTO des Benutzerkontos.
   * @throws PositionNotFoundException Wenn kein Benutzerkonto mit der gegebenen E-Mail-Adresse
   *                                   gefunden wird.
   */
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

  /**
   * Speichert ein neues Benutzerkonto basierend auf den Daten eines DTOs.
   *
   * @param dto Das DTO des Benutzerkontos.
   */
  @Override
  public void save(UserAccountDTO dto) {
    UserAccount userAccount = new UserAccount();
    userAccount.setEmail(dto.getEmail());
    userAccount.setPassword(PWHash.createHash(dto.getPassword()));
    userAccount.setRoles(roleService.findByRoleName("User"));
    userAccountDAO.save(userAccount);
  }

  /**
   * Überprüft, ob das bereitgestellte Passwort mit dem Passwort des Benutzerkontos übereinstimmt,
   * das mit der gegebenen E-Mail-Adresse verbunden ist.
   *
   * @param email    Die E-Mail-Adresse des Benutzerkontos.
   * @param password Das zu überprüfende Passwort.
   * @return true, wenn das Passwort übereinstimmt, sonst false.
   */
  @Override
  public boolean passwordMatches(String email, String password) {
    return userAccountDAO.passwordMatches(email, password);
  }

  /**
   * Überprüft, ob ein Benutzerkonto mit der angegebenen E-Mail-Adresse existiert.
   *
   * @param email Die zu überprüfende E-Mail-Adresse.
   * @return true, wenn ein Benutzerkonto mit der E-Mail-Adresse existiert, sonst false.
   */
  @Override
  public boolean emailExists(String email) {
    return userAccountDAO.emailExists(email);
  }
}

