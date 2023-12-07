package com.ffhs.referencease.dao;

import com.ffhs.referencease.dao.interfaces.ITextTemplateDAO;
import com.ffhs.referencease.entities.ReferenceReason;
import com.ffhs.referencease.entities.TextTemplate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ejb.Stateless;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * DAO class for TextTemplate entity.
 */
@Stateless
public class TextTemplateDAO implements ITextTemplateDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Optional<TextTemplate> findById(UUID id) {
    return Optional.ofNullable(entityManager.find(TextTemplate.class, id));
  }

  @Override
  public List<TextTemplate> findAll() {
    return entityManager.createQuery("SELECT tt FROM TextTemplate tt", TextTemplate.class).getResultList();
  }

  @Override
  public void create(TextTemplate textTemplate) {
    entityManager.persist(textTemplate);
  }

  @Override
  public TextTemplate update(TextTemplate textTemplate) {
    return entityManager.merge(textTemplate);
  }

  @Override
  public void delete(TextTemplate textTemplate) {
    entityManager.remove(textTemplate);
  }
  @Override
  public List<TextTemplate> getTextTemplatesForReason(ReferenceReason reason) {
    return entityManager.createQuery("SELECT tt FROM TextTemplate tt JOIN tt.referenceReasons rr WHERE rr = :reason", TextTemplate.class)
        .setParameter("reason", reason)
        .getResultList();
  }
}
