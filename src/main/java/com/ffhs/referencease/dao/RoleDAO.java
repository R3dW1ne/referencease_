package com.ffhs.referencease.dao;

import com.ffhs.referencease.dao.interfaces.IRoleDAO;
import com.ffhs.referencease.entities.Role;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class RoleDAO implements IRoleDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Optional<Role> findById(UUID roleId) {
    return Optional.ofNullable(entityManager.find(Role.class, roleId));
  }

  @Override
  public Optional<Role> findByRoleName(String roleName) {
    TypedQuery<Role> query = entityManager.createQuery(
        "SELECT r FROM Role r WHERE r.roleName = :roleName", Role.class);
    query.setParameter("roleName", roleName);
    try {
      return Optional.of(query.getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }
}
