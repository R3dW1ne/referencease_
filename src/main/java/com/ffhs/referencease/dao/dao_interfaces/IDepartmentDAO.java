package com.ffhs.referencease.dao.dao_interfaces;

import com.ffhs.referencease.entities.Department;
import java.util.List;
import java.util.Optional;

public interface IDepartmentDAO {

  Optional<Department> find(Long id);

  List<Department> findAll();
}
