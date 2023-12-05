package com.ffhs.referencease.dao;

import com.ffhs.referencease.dao.interfaces.IEmployeeDAO;
import com.ffhs.referencease.entities.Employee;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Stateless
public class EmployeeDAO implements IEmployeeDAO {

  @PersistenceContext
  private EntityManager em;

  @Override
  public Optional<Employee> find(UUID id) {
    return Optional.ofNullable(em.find(Employee.class, id));
  }

  public List<Employee> findAll() {
    return em.createQuery("SELECT e FROM Employee e", Employee.class).getResultList();
  }

  public void save(Employee employee) {
    if (employee.getEmployeeId() == null) {
      em.persist(employee);
    } else {
      em.merge(employee);
    }
  }

  public void delete(Employee employee) {
    if (em.contains(employee)) {
      em.remove(employee);
    } else {
      em.remove(em.merge(employee));
    }
  }

  public Employee update(Employee employee) {
    return em.merge(employee);
  }
}
