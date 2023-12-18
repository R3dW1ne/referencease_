package com.ffhs.referencease.services.interfaces;

import com.ffhs.referencease.entities.Department;
import java.util.List;
import java.util.Optional;

public interface IDepartmentService {
  Optional<Department> getDepartmentById(Long id);
  List<Department> getAllDepartments();

  void createDepartmentIfNotExists(String departmentName);
}
