package com.ffhs.referencease.dao;

import com.ffhs.referencease.dao.interfaces.IGenderDAO;
import com.ffhs.referencease.dto.GenderDTO;
import com.ffhs.referencease.entities.Gender;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Stateless
public class GenderDAO implements IGenderDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Optional<Gender> getGenderById(UUID genderId) {
    Gender gender = entityManager.find(Gender.class, genderId);
    return Optional.ofNullable(gender);
  }

  @Override
  public List<Gender> getAllGenders() {
    return entityManager.createQuery("SELECT g FROM Gender g", Gender.class)
        .getResultList();
  }

  @Override
  @Transactional
  public void saveGender(Gender gender) {
    entityManager.persist(gender);
  }

  @Override
  @Transactional
  public void updateGender(Gender gender) {
    entityManager.merge(gender);
  }

  @Override
  @Transactional
  public void deleteGender(UUID genderId) {
    Gender gender = entityManager.find(Gender.class, genderId);
    if (gender != null) {
      entityManager.remove(gender);
    }
  }
}
