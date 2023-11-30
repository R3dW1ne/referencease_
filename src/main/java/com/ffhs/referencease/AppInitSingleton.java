package com.ffhs.referencease;

import com.ffhs.referencease.entities.Department;
import com.ffhs.referencease.entities.Position;
import com.ffhs.referencease.entities.Role;
import com.ffhs.referencease.entities.enums.ERole;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Startup
@Singleton
public class AppInitSingleton {

  @PersistenceContext
  private EntityManager entityManager;

  @PostConstruct
  public void init() {
    for (ERole roleEnum : ERole.values()) {
      createRoleIfNotExists(roleEnum.name());
    }
    createDepartmentIfNotExists("Human Resources");
    createDepartmentIfNotExists("Finance");
    createDepartmentIfNotExists("Shared IT");
    createPositionIfNotExists("Projektleiter");
    createPositionIfNotExists("Lernender");
    createPositionIfNotExists("System Engineer");
    createPositionIfNotExists("Application Engineer");
    createPositionIfNotExists("Elektroinstallateur");
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
}
