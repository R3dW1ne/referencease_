package com.ffhs.referencease.dao;

import com.ffhs.referencease.dao.interfaces.IPositionDAO;
import com.ffhs.referencease.entities.Position;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PositionDAO implements IPositionDAO {

  @Override
  public Optional<Position> find(Long id) {
    return Optional.ofNullable(em.find(Position.class, id));
  }

  @PersistenceContext
  private EntityManager em;

  @Override
  public List<Position> findAll() {
    return em.createQuery("SELECT p FROM Position p", Position.class).getResultList();
  }
}
