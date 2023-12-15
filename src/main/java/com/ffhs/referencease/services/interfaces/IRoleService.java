package com.ffhs.referencease.services.interfaces;

import com.ffhs.referencease.dto.RoleDTO;
import com.ffhs.referencease.entities.Role;
import com.ffhs.referencease.exceptionhandling.PositionNotFoundException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IRoleService {
  Role findById(UUID roleId) throws PositionNotFoundException;
  Set<Role> findByRoleName(String roleName) throws PositionNotFoundException;

  RoleDTO toDTO(Role role);

  Role toRole(RoleDTO roleDTO);
}
