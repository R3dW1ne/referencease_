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

/**
 * DAO-Klasse für die Verwaltung von Mitarbeiterdaten. Diese Klasse stellt Methoden zur Verfügung,
 * um CRUD-Operationen auf Mitarbeiterentitäten durchzuführen. Sie verwendet den EntityManager, um
 * Interaktionen mit der Datenbank zu handhaben. Die Klasse implementiert das Interface
 * IEmployeeDAO, um eine klare Schnittstelle für die Mitarbeiterverwaltung zu bieten.
 */
@Stateless
public class EmployeeDAO implements IEmployeeDAO {

  @PersistenceContext(unitName = "default")
  private EntityManager em;

  /**
   * Findet einen Mitarbeiter anhand seiner ID.
   *
   * @param id Die UUID des Mitarbeiters.
   * @return Ein Optional, das den gefundenen Mitarbeiter enthält oder leer ist, wenn kein
   * Mitarbeiter gefunden wurde.
   * @throws DatabaseException Bei einem Fehler in der Datenbankabfrage.
   */
  @Override
  public Optional<Employee> find(UUID id) throws DatabaseException {
    try {
      return Optional.ofNullable(em.find(Employee.class, id));
    } catch (PersistenceException e) {
      throw new DatabaseException("Error finding Employee with id " + id, e);
    }
  }

  /**
   * Findet einen Mitarbeiter anhand seiner Mitarbeiter-ID.
   *
   * @param id Die UUID des Mitarbeiters.
   * @return Das gefundene Mitarbeiterobjekt oder null, wenn kein Mitarbeiter gefunden wird.
   */
  @Override
  public Employee findByEmployeeId(UUID id) {
    try {
      return em.createQuery("SELECT e FROM Employee e WHERE e.employeeId = :id", Employee.class)
          .setParameter("id", id).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  /**
   * Ruft eine Liste aller Mitarbeiter ab.
   *
   * @return Eine Liste von Mitarbeiterobjekten.
   * @throws DatabaseException Bei einem Fehler in der Datenbankabfrage.
   */
  @Override
  public List<Employee> findAll() throws DatabaseException {
    try {
      return em.createQuery("SELECT e FROM Employee e", Employee.class).getResultList();
    } catch (PersistenceException e) {
      throw new DatabaseException("Error finding all Employees", e);
    }
  }

  /**
   * Löscht einen Mitarbeiter aus der Datenbank.
   *
   * @param employee Das zu löschende Mitarbeiterobjekt.
   * @throws DatabaseException Bei einem Fehler beim Löschen des Mitarbeiters.
   */
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

  /**
   * Speichert oder aktualisiert einen Mitarbeiter in der Datenbank.
   *
   * @param employee Das zu speichernde oder zu aktualisierende Mitarbeiterobjekt.
   * @return Der gespeicherte oder aktualisierte Mitarbeiter.
   * @throws DatabaseException Bei einem Fehler beim Speichern oder Aktualisieren des Mitarbeiters.
   */
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

  /**
   * Speichert einen neuen Mitarbeiter in der Datenbank.
   *
   * @param employee Das zu speichernde Mitarbeiterobjekt.
   * @throws DatabaseException Bei einem Fehler beim Speichern des neuen Mitarbeiters.
   */
  @Override
  @Transactional
  public void save(Employee employee) throws DatabaseException {
    try {
      em.persist(employee);
    } catch (PersistenceException | IllegalArgumentException e) {
      throw new DatabaseException("Error saving Employee", e);
    }
  }

  /**
   * Aktualisiert einen bestehenden Mitarbeiter in der Datenbank.
   *
   * @param employee Das zu aktualisierende Mitarbeiterobjekt.
   * @return Der aktualisierte Mitarbeiter.
   * @throws DatabaseException Bei einem Fehler beim Aktualisieren des Mitarbeiters.
   */
  @Override
  @Transactional
  public Employee update(Employee employee) throws DatabaseException {
    try {
      return em.merge(employee);
    } catch (PersistenceException | IllegalArgumentException e) {
      throw new DatabaseException("Error updating Employee", e);
    }
  }

  /**
   * Löscht einen Mitarbeiter anhand seiner ID.
   *
   * @param id Die UUID des zu löschenden Mitarbeiters.
   * @throws DatabaseException Bei einem Fehler beim Löschen des Mitarbeiters.
   */
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

  /**
   * Überprüft, ob eine Mitarbeiternummer bereits existiert.
   *
   * @param employeeNumber Die zu überprüfende Mitarbeiternummer.
   * @return true, wenn die Mitarbeiternummer existiert, sonst false.
   * @throws DatabaseException Bei einem Fehler bei der Überprüfung.
   */
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

  /**
   * Überprüft, ob eine Mitarbeiter-ID bereits existiert.
   *
   * @param employeeId Die zu überprüfende Mitarbeiter-ID.
   * @return true, wenn die Mitarbeiter-ID existiert, sonst false.
   * @throws DatabaseException Bei einem Fehler bei der Überprüfung.
   */
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

  /**
   * Findet einen Mitarbeiter anhand seiner Mitarbeiternummer.
   *
   * @param employeeNumber Die Mitarbeiternummer des gesuchten Mitarbeiters.
   * @return Ein Optional, das den gefundenen Mitarbeiter enthält, oder ein leeres Optional, falls
   * kein Mitarbeiter gefunden wurde.
   * @throws DatabaseException Bei einem Fehler bei der Suche.
   */
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

  /**
   * Zählt die Anzahl der Mitarbeiter in der Datenbank.
   *
   * @return Die Anzahl der Mitarbeiter.
   * @throws DatabaseException Bei einem Fehler bei der Zählung.
   */
  @Override
  public Long countEmployees() throws DatabaseException {
    try {
      return em.createQuery("SELECT COUNT(e) FROM Employee e", Long.class).getSingleResult();
    } catch (PersistenceException e) {
      throw new DatabaseException("Error counting employees", e);
    }
  }
}
