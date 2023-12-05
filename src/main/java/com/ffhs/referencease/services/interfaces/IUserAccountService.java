package com.ffhs.referencease.services.interfaces;

import com.ffhs.referencease.dto.UserAccountDTO;
import com.ffhs.referencease.exceptionhandling.PositionNotFoundException;
import java.util.UUID;

public interface IUserAccountService {
  UserAccountDTO findById(UUID id) throws PositionNotFoundException;
  void save(UserAccountDTO dto);

  boolean passwordMatches(String email, String password);

  boolean emailExists(String email);

  UserAccountDTO getUserByEmail(String email) throws PositionNotFoundException;

//  boolean registerNewUser(UserAccount userAccount);
//
//  boolean emailExists(String email);
//

  // Weitere Methoden nach Bedarf
}
