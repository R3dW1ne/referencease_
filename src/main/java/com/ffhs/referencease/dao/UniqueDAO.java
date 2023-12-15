package com.ffhs.referencease.dao;

import com.ffhs.referencease.dao.interfaces.IUniqueDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class UniqueDAO implements IUniqueDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public EntityManager getEntityManager() {
    return entityManager;
  }
}
