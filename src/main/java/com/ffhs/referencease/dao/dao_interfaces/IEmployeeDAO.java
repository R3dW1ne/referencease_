package com.ffhs.referencease.dao.dao_interfaces;

import com.ffhs.referencease.entities.Employee;
import jakarta.ejb.Stateless;
import java.util.List;
import java.util.Optional;


public interface IEmployeeDAO {
  Optional<Employee> find(Long id);
  List<Employee> findAll();
  void save(Employee employee);
  void delete(Employee employee);
  Employee update(Employee employee);
}

