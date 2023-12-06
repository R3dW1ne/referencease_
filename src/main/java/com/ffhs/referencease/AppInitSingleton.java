package com.ffhs.referencease;

import com.ffhs.referencease.entities.Department;
import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.entities.Gender;
import com.ffhs.referencease.entities.Position;
import com.ffhs.referencease.entities.Role;
import com.ffhs.referencease.entities.enums.EGender;
import com.ffhs.referencease.entities.enums.ERole;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.UUID;


@Startup
@Singleton
public class AppInitSingleton {

  @PersistenceContext
  private EntityManager entityManager;

  @PostConstruct
  public void init() {
    for (ERole roleEnum : ERole.values()) {
      createRoleIfNotExists(roleEnum.getDisplayName());
    }
    for (EGender genderEnum : EGender.values()) {
      createGenderIfNotExists(genderEnum.getDisplayName());
    }
    createDepartmentIfNotExists("Human Resources");
    createDepartmentIfNotExists("Finance");
    createDepartmentIfNotExists("Shared IT");
    createPositionIfNotExists("Projektleiter");
    createPositionIfNotExists("Lernender");
    createPositionIfNotExists("System Engineer");
    createPositionIfNotExists("Application Engineer");
    createPositionIfNotExists("Elektroinstallateur");
    createRandomEmployees(10);
  }

  private void createGenderIfNotExists(String genderName) {
    if (entityManager.createQuery("SELECT g FROM Gender g WHERE g.genderName = :genderName", Gender.class)
        .setParameter("genderName", genderName)
        .getResultList().isEmpty()) {
      Gender gender = new Gender();
      gender.setGenderName(genderName);
      entityManager.persist(gender);
    }
  }

  private void createRoleIfNotExists(String roleName) {
    if (entityManager.createQuery("SELECT r FROM Role r WHERE r.roleName = :roleName", Role.class)
        .setParameter("roleName", roleName)
        .getResultList().isEmpty()) {
      Role role = new Role(roleName);
      entityManager.persist(role);
    }
  }

  private void createDepartmentIfNotExists(String departmentName) {
    if (entityManager.createQuery("SELECT d FROM Department d WHERE d.departmentName = :departmentName", Department.class)
        .setParameter("departmentName", departmentName)
        .getResultList().isEmpty()) {
      Department department = new Department();
      department.setDepartmentName(departmentName);
      entityManager.persist(department);
    }
  }

  private void createPositionIfNotExists(String positionName) {
    if (entityManager.createQuery("SELECT p FROM Position p WHERE p.positionName = :positionName", Position.class)
        .setParameter("positionName", positionName)
        .getResultList().isEmpty()) {
      Position position = new Position();
      position.setPositionName(positionName);
      entityManager.persist(position);
    }
  }

  private void createRandomEmployees(int count) {
    Random random = new Random();

    for (int i = 0; i < count; i++) {
      Employee employee = new Employee();
      employee.setEmployeeNumber(UUID.randomUUID().toString().substring(0, 8)); // Zufällige Mitarbeiternummer
      employee.setFirstName("First" + i);
      employee.setLastName("Last" + i);
      employee.setDateOfBirth(LocalDate.now().minusYears(random.nextInt(40) + 18)); // Zufälliges Geburtsdatum zwischen 18 und 58 Jahren
      employee.setPhone("123-456-7890"); // Beispieltelefonnummer
      employee.setStartDate(LocalDate.now().minusYears(random.nextInt(10))); // Zufälliges Anfangsdatum der Beschäftigung in den letzten 10 Jahren
      employee.setEndDate(null); // Enddatum auf null setzen

      // Abteilung kann auch zufällig zugewiesen werden
      Department randomDepartment = getRandomDepartment();
      if (randomDepartment != null) {
        employee.setDepartment(randomDepartment);
      }

      // Zufällige Position auswählen
      Position randomPosition = getRandomPosition();
      if (randomPosition != null) {
        employee.setPosition(randomPosition);
      }

      // Zufälliges Gender auswählen
      Gender randomGender = getRandomGender();
      if (randomGender != null) {
        employee.setGender(randomGender);
      }

      entityManager.persist(employee);
    }
  }

  private Gender getRandomGender() {
    // Annahme: Alle vorhandenen Geschlechter abrufen
    List<Gender> genders = entityManager.createQuery("SELECT g FROM Gender g", Gender.class)
        .getResultList();

    if (!genders.isEmpty()) {
      Random random = new Random();
      return genders.get(random.nextInt(genders.size()));
    } else {
      return null;
    }
  }


  // Methode zum Abrufen einer zufälligen Abteilung aus der Datenbank
  private Department getRandomDepartment() {
    // Annahme: Alle vorhandenen Abteilungen abrufen
    List<Department> departments = entityManager.createQuery("SELECT d FROM Department d", Department.class)
        .getResultList();

    if (!departments.isEmpty()) {
      Random random = new Random();
      return departments.get(random.nextInt(departments.size()));
    } else {
      return null;
    }
  }

  // Methode zum Abrufen einer zufälligen Position aus der Datenbank
  private Position getRandomPosition() {
    // Annahme: Alle vorhandenen Positionen abrufen
    List<Position> positions = entityManager.createQuery("SELECT p FROM Position p", Position.class)
        .getResultList();

    if (!positions.isEmpty()) {
      Random random = new Random();
      return positions.get(random.nextInt(positions.size()));
    } else {
      return null;
    }
  }
}
