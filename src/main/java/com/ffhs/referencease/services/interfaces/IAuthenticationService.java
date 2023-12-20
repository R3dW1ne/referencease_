package com.ffhs.referencease.services.interfaces;

import com.ffhs.referencease.dto.UserAccountDTO;

public interface IAuthenticationService {

  String encryptPassword(String password);

  // Überprüfen des Passworts
  boolean checkPassword(String candidate, String encryptedPassword);

  boolean registerPasswordsMatches(UserAccountDTO userAccountDTO);
}
