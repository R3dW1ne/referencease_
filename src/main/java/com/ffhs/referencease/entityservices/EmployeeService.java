package com.ffhs.referencease.entityservices;

import com.ffhs.referencease.entities.Employee;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class EmployeeService {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  public void save(Employee employee) {
    if (employee.getEmployeeId() == null) {
      // Neuer Eintrag
      entityManager.persist(employee);
    } else {
      // Aktualisierung eines bestehenden Eintrags
      entityManager.merge(employee);
    }
  }

  // Hier können Sie weitere Methoden hinzufügen, z.B.:
  // - `find` um einen Employee anhand seiner ID zu finden
  // - `findAll` um alle Employees abzurufen
  // - `delete` um einen Employee zu löschen
  // - usw.
}
