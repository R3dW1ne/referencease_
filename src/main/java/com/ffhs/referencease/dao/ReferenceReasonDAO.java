package com.ffhs.referencease.dao;

import com.ffhs.referencease.dao.interfaces.IReferenceReasonDAO;
import com.ffhs.referencease.entities.ReferenceReason;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Stateless
public class ReferenceReasonDAO implements IReferenceReasonDAO {

  @PersistenceContext(unitName = "default")
  private EntityManager em;

  @Override
  public Optional<ReferenceReason> findById(UUID id) {
    return Optional.ofNullable(em.find(ReferenceReason.class, id));
  }

  @Override
  public List<ReferenceReason> findAll() {
    return em.createQuery("SELECT r FROM ReferenceReason r", ReferenceReason.class).getResultList();
  }

  @Override
  public Optional<ReferenceReason> findByReasonName(String reasonName) {
    List<ReferenceReason> results = em.createQuery(
            "SELECT p FROM ReferenceReason p WHERE p.reasonName = :name", ReferenceReason.class)
        .setParameter("name", reasonName).getResultList();
    return results.stream().findFirst();
  }

  @Override
  public void save(ReferenceReason referenceReason) {
    if (referenceReason.getReferenceReasonId() == null) {
      em.persist(referenceReason);
    } else {
      em.merge(referenceReason);
    }
  }
}
