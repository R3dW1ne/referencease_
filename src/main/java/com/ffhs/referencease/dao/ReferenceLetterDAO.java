package com.ffhs.referencease.dao;

import com.ffhs.referencease.dao.interfaces.IReferenceLetterDAO;
import com.ffhs.referencease.entities.ReferenceLetter;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Stateless
public class ReferenceLetterDAO implements IReferenceLetterDAO {

  @PersistenceContext(unitName = "default")
  private EntityManager em;

  @Override
  public Optional<ReferenceLetter> findById(UUID id) {
    return Optional.ofNullable(em.find(ReferenceLetter.class, id));
  }

  @Override
  public List<ReferenceLetter> findAll() {
    return em.createQuery("SELECT rl FROM ReferenceLetter rl", ReferenceLetter.class)
        .getResultList();
  }

  @Override
  @Transactional
  public void create(ReferenceLetter referenceLetter) {
    em.persist(referenceLetter);
  }

  @Override
  @Transactional
  public ReferenceLetter update(ReferenceLetter referenceLetter) {
    return em.merge(referenceLetter);
  }

  @Override
  @Transactional
  public void delete(ReferenceLetter referenceLetter) {
    em.remove(em.contains(referenceLetter) ? referenceLetter : em.merge(referenceLetter));
  }

  /**
   * Löscht ein ReferenceLetter-Objekt anhand seiner ID.
   *
   * @param id Die UUID des zu löschenden ReferenceLetter.
   */
  @Override
  @Transactional
  public void deleteById(UUID id) {
    ReferenceLetter referenceLetter = em.find(ReferenceLetter.class, id);
    if (referenceLetter != null) {
      em.remove(referenceLetter);
    }
  }

  @Override
  public List<ReferenceLetter> findReferenceLettersByEmployeeId(UUID employeeId) {
    TypedQuery<ReferenceLetter> query = em.createQuery(
        "SELECT rl FROM ReferenceLetter rl WHERE rl.employee.employeeId = :employeeId",
        ReferenceLetter.class);
    query.setParameter("employeeId", employeeId);
    return query.getResultList();
  }
}
