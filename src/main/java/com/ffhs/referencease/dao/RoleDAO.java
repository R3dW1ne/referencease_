package com.ffhs.referencease.dao;

import com.ffhs.referencease.dao.interfaces.IRoleDAO;
import com.ffhs.referencease.entities.Role;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Stateless
public class RoleDAO implements IRoleDAO {

  @PersistenceContext
  private EntityManager em;

  @Override
  public Optional<Role> findById(UUID roleId) {
    return Optional.ofNullable(em.find(Role.class, roleId));
  }

  @Override
  public Set<Role> findByRoleName(String roleName) {
    return new HashSet<>(em.createQuery("SELECT r FROM Role r WHERE r.roleName = :roleName", Role.class)
        .setParameter("roleName", roleName)
        .getResultList());
  }
}
