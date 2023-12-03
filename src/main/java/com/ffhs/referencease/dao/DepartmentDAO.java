package com.ffhs.referencease.dao;

import com.ffhs.referencease.dao.interfaces.IDepartmentDAO;
import com.ffhs.referencease.entities.Department;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class DepartmentDAO implements IDepartmentDAO {

  @Override
  public Optional<Department> find(Long id) {
    return Optional.ofNullable(em.find(Department.class, id));
  }

  @PersistenceContext
  private EntityManager em;

  @Override
  public List<Department> findAll() {
    return em.createQuery("SELECT d FROM Department d", Department.class).getResultList();
  }
}
