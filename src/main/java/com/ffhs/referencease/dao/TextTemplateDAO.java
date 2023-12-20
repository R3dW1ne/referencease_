package com.ffhs.referencease.dao;

import com.ffhs.referencease.dao.interfaces.ITextTemplateDAO;
import com.ffhs.referencease.entities.Gender;
import com.ffhs.referencease.entities.ReferenceReason;
import com.ffhs.referencease.entities.TextTemplate;
import com.ffhs.referencease.entities.TextType;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * DAO class for TextTemplate entity.
 */
@Stateless
public class TextTemplateDAO implements ITextTemplateDAO {

  @PersistenceContext(unitName = "default")
  private EntityManager em;

  @Override
  public Optional<TextTemplate> findById(UUID id) {
    return Optional.ofNullable(em.find(TextTemplate.class, id));
  }

  @Override
  public List<TextTemplate> findAll() {
    return em.createQuery("SELECT tt FROM TextTemplate tt", TextTemplate.class).getResultList();
  }

  @Override
  @Transactional
  public void create(TextTemplate textTemplate) {
    em.persist(textTemplate);
  }

  @Override
  @Transactional
  public TextTemplate update(TextTemplate textTemplate) {
    return em.merge(textTemplate);
  }

  @Override
  @Transactional
  public void delete(TextTemplate textTemplate) {
    em.remove(textTemplate);
  }

  @Override
  public List<TextTemplate> getTextTemplatesForReasonTypeAndGender(ReferenceReason reason,
      TextType textType, Gender gender) {
    return em.createQuery(
            "SELECT tt FROM TextTemplate tt JOIN tt.referenceReasons rr JOIN tt.genders g WHERE rr = :reason AND tt.textType = :textType AND g = :gender",
            TextTemplate.class).setParameter("reason", reason).setParameter("textType", textType)
        .setParameter("gender", gender).getResultList();
  }

  @Override
  @Transactional
  public void createTextTemplateIfNotExists(String key, String template,
      List<ReferenceReason> referenceReasons, List<Gender> genders, TextType textType) {
    // Erstellt eine Query, die alle Bedingungen überprüft
    String queryStr = "SELECT t FROM TextTemplate t WHERE t.key = :key AND t.template = :template AND t.textType = :textType";
    TypedQuery<TextTemplate> query = em.createQuery(queryStr, TextTemplate.class)
        .setParameter("key", key).setParameter("template", template)
        .setParameter("textType", textType);

    // Prüft, ob ein TextTemplate mit den gegebenen Kriterien existiert
    boolean exists = query.getResultList().stream().anyMatch(textTemplate -> new HashSet<>(
        textTemplate.getReferenceReasons()).containsAll(referenceReasons) && new HashSet<>(
        textTemplate.getGenders()).containsAll(genders));

    if (!exists) {
      TextTemplate newTextTemplate = new TextTemplate();
      newTextTemplate.setKey(key);
      newTextTemplate.setTemplate(template);
      newTextTemplate.setReferenceReasons(referenceReasons);
      newTextTemplate.setGenders(genders);
      newTextTemplate.setTextType(textType);

      em.persist(newTextTemplate);
    }
  }

}
