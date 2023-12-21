package com.ffhs.referencease.services;

import com.ffhs.referencease.dto.UserAccountDTO;
import com.ffhs.referencease.services.interfaces.IAuthenticationService;
import jakarta.ejb.Stateless;
import org.mindrot.jbcrypt.BCrypt;

@Stateless
public class AuthenticationService implements IAuthenticationService {

  @Override
  public String encryptPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }

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
