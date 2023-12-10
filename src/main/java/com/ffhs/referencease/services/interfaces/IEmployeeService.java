package com.ffhs.referencease.services.interfaces;

import com.ffhs.referencease.dto.EmployeeDTO;
import com.ffhs.referencease.entities.Employee;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface IEmployeeService {
  EmployeeDTO getEmployee(UUID id);
  List<EmployeeDTO> getAllEmployees();
//  void saveEmployee(Employee employee);
//  void deleteEmployee(Employee employee);
//  Employee updateEmployee(Employee employee);

  void saveEmployee(EmployeeDTO employeeDTO);

  void deleteEmployee(EmployeeDTO employeeDTO);

  void deleteEmployeeById(UUID id);

  EmployeeDTO updateEmployee(EmployeeDTO employeeDTO);
}