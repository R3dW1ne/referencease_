package com.ffhs.referencease.dao;

import com.ffhs.referencease.dao.interfaces.IEmployeeDAO;
import com.ffhs.referencease.entities.Employee;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
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
  @Override
  public List<Employee> findAll() {
    return em.createQuery("SELECT e FROM Employee e", Employee.class).getResultList();
  }
  @Override
  @Transactional
  public void save(Employee employee) {
    if (employee.getEmployeeId() == null) {
      em.persist(employee);
    } else {
      em.merge(employee);
    }
  }
  @Override
  @Transactional
  public void delete(Employee employee) {
    if (em.contains(employee)) {
      em.remove(employee);
    } else {
      em.remove(em.merge(employee));
    }
  }

  @Override
  @Transactional
  public Employee update(Employee employee) {
    return em.merge(employee);
  }

  @Override
  @Transactional
  public void deleteById(UUID id) {
    Employee employee = em.find(Employee.class, id);
    em.remove(employee);
  }

  @Override
  public boolean findByEmployeeNumber(String employeeNumber) {
    try {
      Query query = em.createQuery("SELECT COUNT(e) FROM Employee e WHERE e.employeeNumber = :employeeNumber");
      query.setParameter("employeeNumber", employeeNumber);
      long count = (long) query.getSingleResult();
      return count == 0;
    } catch (NoResultException e) {
      return true;
    }
  }
}
