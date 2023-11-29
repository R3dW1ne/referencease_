package com.ffhs.referencease.services.service_interfaces;

import com.ffhs.referencease.entities.Employee;
import jakarta.ejb.Stateless;
import java.util.List;
import java.util.Optional;


public interface IEmployeeService {
  Optional<Employee> getEmployee(Long id);
  List<Employee> getAllEmployees();
  void saveEmployee(Employee employee);
  void deleteEmployee(Employee employee);
  Employee updateEmployee(Employee employee);
}