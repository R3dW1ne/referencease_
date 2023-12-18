package com.ffhs.referencease.dao;

import com.ffhs.referencease.dao.interfaces.IGenderDAO;
import com.ffhs.referencease.entities.Gender;
import com.ffhs.referencease.producers.qualifiers.ProdPU;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Stateless
public class GenderDAO implements IGenderDAO {

  @PersistenceContext(unitName = "default")
  private EntityManager em;

  @Override
  public Optional<Gender> getGenderById(UUID genderId) {
    Gender gender = em.find(Gender.class, genderId);
    return Optional.ofNullable(gender);
  }

  @Override
  public List<Gender> getAllGenders() {
    return em.createQuery("SELECT g FROM Gender g", Gender.class)
        .getResultList();
  }

  @Override
  @Transactional
  public void saveGender(Gender gender) {
    em.persist(gender);
  }

  @Override
  @Transactional
  public void updateGender(Gender gender) {
    em.merge(gender);
  }

  @Override
  @Transactional
  public void deleteGender(UUID genderId) {
    Gender gender = em.find(Gender.class, genderId);
    if (gender != null) {
      em.remove(gender);
    }
  }

  @Override
  public Optional<Gender> findByName(String name) {
    List<Gender> results = em.createQuery("SELECT p FROM Gender p WHERE p.genderName = :name",
            Gender.class)
        .setParameter("name", name)
        .getResultList();
    return results.stream().findFirst();
  }

  @Override
  public void create(Gender gender) {
    em.persist(gender);
  }
}
