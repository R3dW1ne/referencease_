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
 * DAO-Klasse für die Verwaltung von TextTemplate-Entitäten. Diese Klasse bietet Methoden zur Durchführung von CRUD-Operationen
 * auf TextTemplate-Objekten. Sie nutzt den EntityManager für Datenbankinteraktionen und implementiert das ITextTemplateDAO Interface
 * für eine definierte Schnittstelle zur Verwaltung von Textvorlagen.
 */
@Stateless
public class TextTemplateDAO implements ITextTemplateDAO {

  @PersistenceContext(unitName = "default")
  private EntityManager em;
  /**
   * Findet eine Textvorlage anhand ihrer ID.
   *
   * @param id Die UUID der Textvorlage.
   * @return Ein Optional, das die gefundene Textvorlage enthält oder ein leeres Optional, falls keine Textvorlage gefunden wurde.
   */
  @Override
  public Optional<TextTemplate> findById(UUID id) {
    return Optional.ofNullable(em.find(TextTemplate.class, id));
  }
  /**
   * Ruft eine Liste aller Textvorlagen ab.
   *
   * @return Eine Liste von TextTemplate-Objekten.
   */
  @Override
  public List<TextTemplate> findAll() {
    return em.createQuery("SELECT tt FROM TextTemplate tt", TextTemplate.class).getResultList();
  }
  /**
   * Erstellt eine neue Textvorlage in der Datenbank.
   *
   * @param textTemplate Das zu erstellende TextTemplate-Objekt.
   */
  @Override
  @Transactional
  public void create(TextTemplate textTemplate) {
    em.persist(textTemplate);
  }
  /**
   * Aktualisiert eine bestehende Textvorlage in der Datenbank.
   *
   * @param textTemplate Das zu aktualisierende TextTemplate-Objekt.
   * @return Die aktualisierte Textvorlage.
   */
  @Override
  @Transactional
  public TextTemplate update(TextTemplate textTemplate) {
    return em.merge(textTemplate);
  }
  /**
   * Löscht eine Textvorlage aus der Datenbank.
   *
   * @param textTemplate Das zu löschende TextTemplate-Objekt.
   */
  @Override
  @Transactional
  public void delete(TextTemplate textTemplate) {
    em.remove(textTemplate);
  }
  /**
   * Ruft Textvorlagen ab, die zu einem bestimmten Anlass, Texttyp und Geschlecht passen.
   *
   * @param reason Der Anlass, zu dem die Textvorlage passt.
   * @param textType Der Texttyp der Vorlage.
   * @param gender Das Geschlecht, zu dem die Vorlage passt.
   * @return Eine Liste von TextTemplate-Objekten, die den Kriterien entsprechen.
   */
  @Override
  public List<TextTemplate> getTextTemplatesForReasonTypeAndGender(ReferenceReason reason,
      TextType textType, Gender gender) {
    return em.createQuery(
            "SELECT tt FROM TextTemplate tt JOIN tt.referenceReasons rr JOIN tt.genders g WHERE rr = :reason AND tt.textType = :textType AND g = :gender",
            TextTemplate.class).setParameter("reason", reason).setParameter("textType", textType)
        .setParameter("gender", gender).getResultList();
  }
  /**
   * Erstellt eine Textvorlage in der Datenbank, wenn diese noch nicht existiert.
   *
   * @param key Der Schlüssel der Textvorlage.
   * @param template Der Text der Vorlage.
   * @param referenceReasons Die Anlässe, zu denen die Vorlage passt.
   * @param genders Die Geschlechter, zu denen die Vorlage passt.
   * @param textType Der Texttyp der Vorlage.
   */
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
