package com.ffhs.referencease.services;

import com.ffhs.referencease.dao.interfaces.IDepartmentDAO;
import com.ffhs.referencease.entities.Department;
import com.ffhs.referencease.services.interfaces.IDepartmentService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

@Stateless
public class DepartmentService implements IDepartmentService {

  private final IDepartmentDAO departmentDao;

  @Inject
  public DepartmentService(IDepartmentDAO departmentDao) {
    this.departmentDao = departmentDao;
  }

  @Override
  public Optional<Department> getDepartmentById(Long id) {
    return departmentDao.find(id);
  }

  @Override
  public List<Department> getAllDepartments() {
    return departmentDao.findAll();
  }

  @Override
  public void createDepartmentIfNotExists(String departmentName) {
    if (departmentDao.findByName(departmentName).isEmpty()) {
      Department department = new Department();
      department.setDepartmentName(departmentName);
      departmentDao.create(department);
    }
  }
}

