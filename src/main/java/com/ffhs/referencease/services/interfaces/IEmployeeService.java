package com.ffhs.referencease.services.interfaces;

import com.ffhs.referencease.beans.ReferenceLetterBean;
import com.ffhs.referencease.dto.EmployeeDTO;
import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.exceptionhandling.BusinessException;
import com.ffhs.referencease.exceptionhandling.OperationResult;
import java.util.List;
import java.util.UUID;


public interface IEmployeeService {
  EmployeeDTO getEmployee(UUID id);
  List<EmployeeDTO> getAllEmployees();
//  void saveEmployee(Employee employee);
//  void deleteEmployee(Employee employee);
//  Employee updateEmployee(Employee employee);

  void saveEmployee(EmployeeDTO employeeDTO);

//  boolean saveOrUpdateEmployee(EmployeeDTO employeeDTO, ReferenceLetterBean referenceLetterBean);

  OperationResult<EmployeeDTO> saveOrUpdateEmployee(EmployeeDTO employeeDTO) throws BusinessException;

  void deleteEmployee(EmployeeDTO employeeDTO);

  void deleteEmployeeById(UUID id);

  EmployeeDTO updateEmployee(EmployeeDTO employeeDTO);

  boolean employeeNumberExists(String employeeNumber);

  // Hilfsmethode zur Konvertierung von Entity zu DTO

  EmployeeDTO getEmployeeByEmployeeNumber(String employeeNumber);

  EmployeeDTO convertToDTO(Employee employee);

  // Hilfsmethode zur Konvertierung von DTO zu Entity

  Employee convertToEntity(EmployeeDTO dto);
}