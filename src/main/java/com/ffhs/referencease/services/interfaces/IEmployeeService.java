package com.ffhs.referencease.services.interfaces;

import com.ffhs.referencease.dto.EmployeeDTO;
import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.exceptionhandling.BusinessException;
import com.ffhs.referencease.exceptionhandling.OperationResult;
import java.util.List;
import java.util.Random;
import java.util.UUID;


public interface IEmployeeService {

  EmployeeDTO getEmployee(UUID id);

  List<EmployeeDTO> getAllEmployees();

  long countEmployees();

  void saveEmployee(Employee employee);

  OperationResult<EmployeeDTO> saveOrUpdateEmployee(EmployeeDTO employeeDTO)
      throws BusinessException;

  void deleteEmployee(EmployeeDTO employeeDTO);

  boolean employeeNumberExists(String employeeNumber);

  EmployeeDTO getEmployeeByEmployeeNumber(String employeeNumber);

  EmployeeDTO convertToDTO(Employee employee);

  Employee convertToEntity(EmployeeDTO dto);

  void createRandomEmployeesIfNotExists(int count, Random random);
}