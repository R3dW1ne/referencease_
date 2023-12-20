package com.ffhs.referencease.services;


import com.ffhs.referencease.dao.interfaces.IRoleDAO;
import com.ffhs.referencease.dto.RoleDTO;
import com.ffhs.referencease.entities.Role;
import com.ffhs.referencease.exceptionhandling.PositionNotFoundException;
import com.ffhs.referencease.services.interfaces.IRoleService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.Set;
import java.util.UUID;

@Stateless
public class RoleService implements IRoleService {

  private final IRoleDAO roleDAO;

  @Inject
  public RoleService(IRoleDAO roleDAO) {
    this.roleDAO = roleDAO;
  }

  @Override
  public Role findById(UUID roleId) throws PositionNotFoundException {
    return roleDAO.findById(roleId)
        .orElseThrow(() -> new PositionNotFoundException("Role not found with ID: " + roleId));
  }

  @Override
  public Set<Role> findByRoleName(String roleName) {
    return roleDAO.findByRoleName(roleName);
  }

  @Override
  public RoleDTO toDTO(Role role) {
    return new RoleDTO(role.getRoleId(), role.getRoleName());
  }

  @Override
  public Role toRole(RoleDTO roleDTO) {
    return new Role(roleDTO.getRoleId(), roleDTO.getRoleName());
  }

  @Override
  public void createRoleIfNotExists(String roleName) {
    if (roleDAO.findByRoleName(roleName).isEmpty()) {
      Role role = new Role();
      role.setRoleName(roleName);
      roleDAO.create(role);
    }
  }
}
