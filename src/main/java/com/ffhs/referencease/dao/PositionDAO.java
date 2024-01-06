package com.ffhs.referencease.dao;

import com.ffhs.referencease.dao.interfaces.IPositionDAO;
import com.ffhs.referencease.entities.Position;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

/**
 * DAO-Klasse für die Verwaltung von Positionsdaten. Diese Klasse bietet Methoden, um
 * CRUD-Operationen auf Positions-Entitäten durchzuführen. Sie nutzt den EntityManager für
 * Datenbankinteraktionen und implementiert das IPositionDAO Interface für eine definierte
 * Schnittstelle zur Verwaltung von Positionsdaten.
 */
@Stateless
public class PositionDAO implements IPositionDAO {

  @PersistenceContext(unitName = "default")
  private EntityManager em;

  /**
   * Findet eine Position anhand ihrer ID.
   *
   * @param id Die ID der Position.
   * @return Ein Optional, das die gefundene Position enthält oder ein leeres Optional, falls keine
   * Position gefunden wurde.
   */
  @Override
  public Optional<Position> find(Long id) {
    return Optional.ofNullable(em.find(Position.class, id));
  }

  /**
   * Ruft eine Liste aller Positionen ab.
   *
   * @return Eine Liste von Position-Objekten.
   */
  @Override
  public List<Position> findAll() {
    return em.createQuery("SELECT p FROM Position p", Position.class).getResultList();
  }

  /**
   * Findet eine Position anhand ihres Namens.
   *
   * @param positionName Der Name der Position.
   * @return Ein Optional, das die gefundene Position enthält, oder ein leeres Optional, falls keine
   * Position gefunden wurde.
   */
  @Override
  public Optional<Position> findByName(String positionName) {
    List<Position> results = em.createQuery(
            "SELECT p FROM Position p WHERE p.positionName = :positionName", Position.class)
        .setParameter("positionName", positionName).getResultList();
    return results.stream().findFirst();
  }

  /**
   * Erstellt eine neue Position in der Datenbank.
   *
   * @param position Das zu erstellende Position-Objekt.
   */
  @Override
  public void create(Position position) {
    em.persist(position);
  }
}
