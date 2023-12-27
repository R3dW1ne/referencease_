package com.ffhs.referencease.dao;

import com.ffhs.referencease.dao.interfaces.IPositionDAO;
import com.ffhs.referencease.entities.Position;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Stateless
public class PositionDAO implements IPositionDAO {

  @PersistenceContext(unitName = "default")
  private EntityManager em;

  @Override
  public Optional<Position> find(Long id) {
    return Optional.ofNullable(em.find(Position.class, id));
  }

  @Override
  public List<Position> findAll() {
    return em.createQuery("SELECT p FROM Position p", Position.class).getResultList();
  }

  @Override
  public Optional<Position> findByName(String positionName) {
    List<Position> results = em.createQuery(
            "SELECT p FROM Position p WHERE p.positionName = :positionName", Position.class)
        .setParameter("positionName", positionName).getResultList();
    return results.stream().findFirst();
  }

  @Override
  public void create(Position position) {
    em.persist(position);
  }
}
