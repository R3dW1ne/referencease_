package com.ffhs.referencease.dao.interfaces;

import com.ffhs.referencease.entities.Role;
import java.util.Optional;
import java.util.UUID;

public interface IRoleDAO {
  Optional<Role> findById(UUID roleId);
  Optional<Role> findByRoleName(String roleName);
}
