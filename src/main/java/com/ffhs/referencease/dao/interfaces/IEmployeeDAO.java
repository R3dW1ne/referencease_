package com.ffhs.referencease.dao.interfaces;

import com.ffhs.referencease.entities.Employee;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface IEmployeeDAO {
  Optional<Employee> find(UUID id);
  List<Employee> findAll();
  void save(Employee employee);
  void delete(Employee employee);
  Employee update(Employee employee);

  @Transactional
  void deleteById(UUID id);
}

