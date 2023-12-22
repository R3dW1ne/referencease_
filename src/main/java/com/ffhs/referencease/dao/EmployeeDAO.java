package com.ffhs.referencease.dao;

import com.ffhs.referencease.dao.interfaces.IEmployeeDAO;
import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.exceptionhandling.DatabaseException;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Stateless
public class EmployeeDAO implements IEmployeeDAO {

  @PersistenceContext(unitName = "default")
  private EntityManager em;

  @Override
  public Optional<Employee> find(UUID id) throws DatabaseException {
    try {

      return Optional.ofNullable(em.find(Employee.class, id));
    } catch (PersistenceException e) {
      throw new DatabaseException("Error finding Employee with id " + id, e);
    }
  }

  @Override
  public Employee findByEmployeeId(UUID id) {
    try {
      return em.createQuery("SELECT e FROM Employee e WHERE e.employeeId = :id", Employee.class)
          .setParameter("id", id).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  @Override
  public List<Employee> findAll() throws DatabaseException {
    try {
      return em.createQuery("SELECT e FROM Employee e", Employee.class).getResultList();
    } catch (PersistenceException e) {
      throw new DatabaseException("Error finding all Employees", e);
    }
  }


  @Override
  @Transactional
  public void delete(Employee employee) throws DatabaseException {
    try {
      if (em.contains(employee)) {
        em.remove(employee);
      } else {
        em.remove(em.merge(employee));
      }
    } catch (PersistenceException e) {
      throw new DatabaseException("Error deleting Employee", e);
    }
  }

  @Override
  @Transactional
  public Employee saveOrUpdateEmployee(Employee employee) throws DatabaseException {
    try {
      if (employee.getEmployeeId() == null) {
        save(employee);
        return employee; // Persistierte Entität zurückgeben
      } else {
        return update(employee); // Aktualisierte Entität zurückgeben
      }
    } catch (PersistenceException | IllegalArgumentException e) {
      throw new DatabaseException("Error saving or updating Employee", e);
    }
  }

  @Override
  @Transactional
  public void save(Employee employee) throws DatabaseException {
    try {
      em.persist(employee);
    } catch (PersistenceException | IllegalArgumentException e) {
      throw new DatabaseException("Error saving Employee", e);
    }
  }

  @Override
  @Transactional
  public Employee update(Employee employee) throws DatabaseException {
    try {
      return em.merge(employee);
    } catch (PersistenceException | IllegalArgumentException e) {
      throw new DatabaseException("Error updating Employee", e);
    }
  }

  @Override
  @Transactional
  public void deleteById(UUID id) throws DatabaseException {
    try {
      Employee employee = em.find(Employee.class, id);
      if (employee != null) {
        em.remove(employee);
      }
    } catch (PersistenceException e) {
      throw new DatabaseException("Error deleting Employee by id", e);
    }
  }

  @Override
  public boolean employeeNumberExists(String employeeNumber) throws DatabaseException {
    if (employeeNumber == null) {
      return false;
    }
    try {
      Query query = em.createQuery(
          "SELECT COUNT(e) FROM Employee e WHERE e.employeeNumber = :employeeNumber");
      query.setParameter("employeeNumber", employeeNumber);
      long count = (long) query.getSingleResult();
      return count > 0;
    } catch (PersistenceException e) {
      throw new DatabaseException("Error checking if employee number exists", e);
    }
  }

  @Override
  public boolean employeeIdExists(UUID employeeId) throws DatabaseException {
    if (employeeId == null) {
      return false;
    }
    try {
      TypedQuery<Long> query = em.createQuery(
          "SELECT COUNT(e) FROM Employee e WHERE e.employeeId = :employeeId", Long.class);
      query.setParameter("employeeId", employeeId);
      long count = query.getSingleResult();
      return count > 0;
    } catch (PersistenceException e) {
      throw new DatabaseException("Error checking if employeeId exists", e);
    }
  }

  @Override
  public Optional<Employee> findByEmployeeNumber(String employeeNumber) throws DatabaseException {
    try {
      TypedQuery<Employee> query = em.createQuery(
          "SELECT e FROM Employee e WHERE e.employeeNumber = :employeeNumber", Employee.class);
      query.setParameter("employeeNumber", employeeNumber);
      List<Employee> results = query.getResultList();
      return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    } catch (PersistenceException e) {
      throw new DatabaseException("Error finding Employee by employee number", e);
    }
  }


  @Override
  public Long countEmployees() throws DatabaseException {
    try {
      return em.createQuery("SELECT COUNT(e) FROM Employee e", Long.class).getSingleResult();
    } catch (PersistenceException e) {
      throw new DatabaseException("Error counting employees", e);
    }
  }
}
