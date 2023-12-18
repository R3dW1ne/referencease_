package com.ffhs.referencease.dao;

import com.ffhs.referencease.dao.interfaces.IReferenceLetterDAO;
import com.ffhs.referencease.entities.ReferenceLetter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Stateless
public class ReferenceLetterDAO implements IReferenceLetterDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Optional<ReferenceLetter> findById(UUID id) {
    return Optional.ofNullable(entityManager.find(ReferenceLetter.class, id));
  }

  @Override
  public List<ReferenceLetter> findAll() {
    return entityManager.createQuery("SELECT rl FROM ReferenceLetter rl", ReferenceLetter.class).getResultList();
  }

  @Override
  @Transactional
  public void create(ReferenceLetter referenceLetter) {
    entityManager.persist(referenceLetter);
  }

  @Override
  @Transactional
  public ReferenceLetter update(ReferenceLetter referenceLetter) {
    return entityManager.merge(referenceLetter);
  }

  @Override
  @Transactional
  public void delete(ReferenceLetter referenceLetter) {
    entityManager.remove(entityManager.contains(referenceLetter) ? referenceLetter : entityManager.merge(referenceLetter));
  }

  /**
   * Löscht ein ReferenceLetter-Objekt anhand seiner ID.
   *
   * @param id Die UUID des zu löschenden ReferenceLetter.
   */
  @Override
  @Transactional
  public void deleteById(UUID id) {
    ReferenceLetter referenceLetter = entityManager.find(ReferenceLetter.class, id);
    if (referenceLetter != null) {
      entityManager.remove(referenceLetter);
    }
  }

  @Override
  public List<ReferenceLetter> findReferenceLettersByEmployeeId(UUID employeeId) {
    TypedQuery<ReferenceLetter> query = entityManager.createQuery(
        "SELECT rl FROM ReferenceLetter rl WHERE rl.employee.employeeId = :employeeId", ReferenceLetter.class);
    query.setParameter("employeeId", employeeId);
    return query.getResultList();
  }
}
