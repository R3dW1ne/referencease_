package com.ffhs.referencease.services.interfaces;

import com.ffhs.referencease.dto.UserAccountDTO;

public interface IAuthenticationService {

  //  @Override
  //  public boolean authenticate(String email, String password) {
  //    return false;
  //  }
  String encryptPassword(String password);

  // Überprüfen des Passworts
  boolean checkPassword(String candidate, String encryptedPassword);

  //  @Override
  //  public boolean authenticate(String email, String password) {
  //    return false;
  //  }
  boolean registerPasswordsMatches(UserAccountDTO userAccountDTO);
}
