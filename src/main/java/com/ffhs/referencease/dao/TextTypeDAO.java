package com.ffhs.referencease.dao;

import com.ffhs.referencease.dao.interfaces.ITextTypeDAO;
import com.ffhs.referencease.entities.TextType;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Stateless
public class TextTypeDAO implements ITextTypeDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Optional<TextType> findByName(String name) {
    return entityManager.createQuery("SELECT tt FROM TextType tt WHERE tt.textTypeName = :name", TextType.class)
        .setParameter("name", name)
        .getResultList().stream().findFirst();
  }
  @Override
  public Optional<TextType> findById(UUID id) {
    return Optional.ofNullable(entityManager.find(TextType.class, id));
  }
  @Override
  public List<TextType> findAll() {
    return entityManager.createQuery("SELECT tt FROM TextType tt", TextType.class).getResultList();
  }
}
