package com.ffhs.referencease.services;

import com.ffhs.referencease.dto.UserAccountDTO;
import com.ffhs.referencease.services.interfaces.IAuthenticationService;
import com.ffhs.referencease.services.interfaces.IUserAccountService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;

@Stateless
public class AuthenticationService implements IAuthenticationService {

  private final IUserAccountService userAccountService;

  @Inject
  public AuthenticationService(IUserAccountService userAccountService) {
    this.userAccountService = userAccountService;
  }


  //  @Override
//  public boolean authenticate(String email, String password) {
//    return false;
//  }
  @Override
  public String encryptPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }

  // Überprüfen des Passworts
  @Override
  public boolean checkPassword(String candidate, String encryptedPassword) {
    return BCrypt.checkpw(candidate, encryptedPassword);
  }


  @Override
  public boolean registerPasswordsMatches(UserAccountDTO userAccountDTO) {
    return userAccountDTO != null && (userAccountDTO.getPassword() != null
        && userAccountDTO.getConfirmPassword() != null && !userAccountDTO.getPassword()
        .equals(userAccountDTO.getConfirmPassword()));
  }
}
