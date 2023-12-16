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
      userAccount.setRoles(roleService.findByRoleName("User"));
    } catch (PositionNotFoundException e) {
      LOGGER.info(e.getMessage());
    }
    userAccountDAO.save(userAccount);
  }

  @Override
  public boolean passwordMatches(String email, String password) {
    return userAccountDAO.passwordMatches(email, password);
  }

  @Override
  public boolean emailExists(String email) {
    return userAccountDAO.emailExists(email);
  }
}

