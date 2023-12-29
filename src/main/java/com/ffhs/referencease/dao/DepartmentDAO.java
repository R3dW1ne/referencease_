package com.ffhs.referencease.dao;

import com.ffhs.referencease.dao.interfaces.IDepartmentDAO;
import com.ffhs.referencease.entities.Department;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Stateless
public class DepartmentDAO implements IDepartmentDAO {

  @PersistenceContext(unitName = "default")
  private EntityManager em;

  @Override
  public Optional<Department> find(Long id) {
    return Optional.ofNullable(em.find(Department.class, id));
  }

  @Override
  public List<Department> findAll() {
    return em.createQuery("SELECT d FROM Department d", Department.class).getResultList();
  }

  @Override
  public Optional<Department> findByName(String departmentName) {
    List<Department> results = em.createQuery(
            "SELECT d FROM Department d WHERE d.departmentName = :departmentName", Department.class)
        .setParameter("departmentName", departmentName).getResultList();
    return results.stream().findFirst();
  }


  @Override
  public void create(Department department) {
    em.persist(department);
  }
}
