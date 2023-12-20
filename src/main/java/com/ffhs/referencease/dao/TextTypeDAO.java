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

  @PersistenceContext(unitName = "default")
  private EntityManager em;

  @Override
  public Optional<TextType> findByName(String textTypeName) {
    List<TextType> results = em.createQuery(
            "SELECT tt FROM TextType tt WHERE tt.textTypeName = :textTypeName", TextType.class)
        .setParameter("textTypeName", textTypeName).getResultList();
    return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
  }


  @Override
  public Optional<TextType> findById(UUID id) {
    return Optional.ofNullable(em.find(TextType.class, id));
  }

  @Override
  public List<TextType> findAll() {
    return em.createQuery("SELECT tt FROM TextType tt", TextType.class).getResultList();
  }

  @Override
  public void create(TextType textType) {
    em.persist(textType);
  }
}
