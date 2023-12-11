package com.ffhs.referencease.dao;

import com.ffhs.referencease.dao.interfaces.IReferenceReasonDAO;
import com.ffhs.referencease.entities.ReferenceReason;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Stateless
public class ReferenceReasonDAO implements IReferenceReasonDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Optional<ReferenceReason> findById(UUID id) {
    return Optional.ofNullable(entityManager.find(ReferenceReason.class, id));
  }

  @Override
  public List<ReferenceReason> findAll() {
    return entityManager.createQuery("SELECT r FROM ReferenceReason r", ReferenceReason.class).getResultList();
  }
}
