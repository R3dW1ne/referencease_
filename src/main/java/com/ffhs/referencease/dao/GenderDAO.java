package com.ffhs.referencease.dao;

import com.ffhs.referencease.dao.interfaces.IGenderDAO;
import com.ffhs.referencease.entities.Gender;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * DAO-Klasse für die Verwaltung von Gender-Daten. Diese Klasse bietet Methoden für CRUD-Operationen
 * auf Gender-Entitäten. Sie nutzt den EntityManager für Datenbankinteraktionen und implementiert
 * das IGenderDAO Interface für eine definierte Schnittstelle zur Verwaltung von Gender-Daten.
 */
@Stateless
public class GenderDAO implements IGenderDAO {

  @PersistenceContext(unitName = "default")
  private EntityManager em;

  /**
   * Findet ein Geschlecht (Gender) anhand seiner ID.
   *
   * @param genderId Die UUID des Geschlechts.
   * @return Ein Optional, das das gefundene Geschlecht enthält, oder ein leeres Optional, falls
   * kein Geschlecht gefunden wurde.
   */
  @Override
  public Optional<Gender> getGenderById(UUID genderId) {
    Gender gender = em.find(Gender.class, genderId);
    return Optional.ofNullable(gender);
  }

  /**
   * Gibt eine Liste aller Geschlechter zurück.
   *
   * @return Eine Liste von Gender-Objekten.
   */
  @Override
  public List<Gender> getAllGenders() {
    return em.createQuery("SELECT g FROM Gender g", Gender.class).getResultList();
  }

  /**
   * Speichert ein neues Geschlecht in der Datenbank.
   *
   * @param gender Das zu speichernde Gender-Objekt.
   */
  @Override
  @Transactional
  public void saveGender(Gender gender) {
    em.persist(gender);
  }

  /**
   * Aktualisiert ein bestehendes Geschlecht in der Datenbank.
   *
   * @param gender Das zu aktualisierende Gender-Objekt.
   */
  @Override
  @Transactional
  public void updateGender(Gender gender) {
    em.merge(gender);
  }

  /**
   * Löscht ein Geschlecht anhand seiner ID.
   *
   * @param genderId Die UUID des zu löschenden Geschlechts.
   */
  @Override
  @Transactional
  public void deleteGender(UUID genderId) {
    Gender gender = em.find(Gender.class, genderId);
    if (gender != null) {
      em.remove(gender);
    }
  }

  /**
   * Findet ein Geschlecht anhand seines Namens.
   *
   * @param genderName Der Name des Geschlechts.
   * @return Ein Optional, das das gefundene Geschlecht enthält, oder ein leeres Optional, falls
   * kein Geschlecht gefunden wurde.
   */
  @Override
  public Optional<Gender> findByName(String genderName) {
    List<Gender> results = em.createQuery("SELECT p FROM Gender p WHERE p.genderName = :name",
                                          Gender.class).setParameter("name", genderName)
        .getResultList();
    return results.stream().findFirst();
  }

  /**
   * Erstellt ein neues Geschlecht in der Datenbank.
   *
   * @param gender Das zu erstellende Gender-Objekt.
   */
  @Override
  public void create(Gender gender) {
    em.persist(gender);
  }
}
