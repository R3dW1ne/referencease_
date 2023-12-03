package com.ffhs.referencease.services.interfaces;

import com.ffhs.referencease.entities.Employee;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface IEmployeeService {
  Optional<Employee> getEmployee(UUID id);
  List<Employee> getAllEmployees();
  void saveEmployee(Employee employee);
  void deleteEmployee(Employee employee);
  Employee updateEmployee(Employee employee);
}