package com.ffhs.referencease.dao.interfaces;

import com.ffhs.referencease.entities.UserAccount;
import java.util.Optional;
import java.util.UUID;

public interface IUserAccountDAO {
  Optional<UserAccount> findById(UUID id);

  Optional<UserAccount> findByEmail(String email);

  void save(UserAccount userAccount);

  boolean emailExists(String email);

  boolean passwordMatches(String email, String password);

  // Weitere Methoden nach Bedarf
}
