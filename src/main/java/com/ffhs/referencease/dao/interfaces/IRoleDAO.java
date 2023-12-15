package com.ffhs.referencease.dao.interfaces;

import com.ffhs.referencease.entities.Role;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IRoleDAO {
  Optional<Role> findById(UUID roleId);
  Set<Role> findByRoleName(String roleName);
}
