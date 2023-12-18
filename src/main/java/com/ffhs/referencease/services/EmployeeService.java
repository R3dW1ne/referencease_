package com.ffhs.referencease.services;

import com.ffhs.referencease.dao.interfaces.IEmployeeDAO;
import com.ffhs.referencease.dto.EmployeeDTO;
import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.entities.ReferenceLetter;
import com.ffhs.referencease.exceptionhandling.BusinessException;
import com.ffhs.referencease.exceptionhandling.OperationResult;
import com.ffhs.referencease.services.interfaces.IEmployeeService;
import com.ffhs.referencease.services.interfaces.IReferenceLetterService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;


@Stateless
public class EmployeeService implements IEmployeeService {

  private final IEmployeeDAO employeeDao;
  private final IReferenceLetterService referenceLetterService;
  private final ModelMapper modelMapper;

  @Inject
  public EmployeeService(IEmployeeDAO employeeDao, IReferenceLetterService referenceLetterService,
      ModelMapper modelMapper) {
    this.employeeDao = employeeDao;
    this.referenceLetterService = referenceLetterService;
    this.modelMapper = modelMapper;
  }

  @Override
  public EmployeeDTO getEmployee(UUID id) {
    return employeeDao.find(id).map(this::convertToDTO).orElse(null);
  }

  @Override
  public List<EmployeeDTO> getAllEmployees() {
    return employeeDao.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
  }

  @Override
  public long countEmployees() {
    return employeeDao.countEmployees();
  }


  @Override
  public void saveEmployee(Employee employee) {
    employeeDao.save(employee);
  }

  @Override
  public OperationResult<EmployeeDTO> saveOrUpdateEmployee(EmployeeDTO employeeDTO)
      throws BusinessException {
    EmployeeDTO existingEmployee = getEmployeeByEmployeeNumber(employeeDTO.getEmployeeNumber());
    boolean isNewEmployee = employeeDTO.getEmployeeId() == null;

    if (existingEmployee != null && (isNewEmployee || !existingEmployee.getEmployeeId()
        .equals(employeeDTO.getEmployeeId()))) {
      return OperationResult.failure(
          "Mitarbeiternummer bereits von einem anderen Mitarbeiter vergeben.");
    }

    Employee employeeEntity = convertToEntity(employeeDTO);
    Employee savedEmployee = isNewEmployee ? saveEmployeeInternal(employeeEntity)
        : updateEmployeeInternal(employeeEntity);
    return OperationResult.success(convertToDTO(savedEmployee));
  }

  private Employee saveEmployeeInternal(Employee employee) {
    employeeDao.save(employee);
    return employee;
  }

  private Employee updateEmployeeInternal(Employee employee) {
    return employeeDao.update(employee);
  }

  @Override
  public void deleteEmployee(EmployeeDTO employeeDTO) {
    UUID employeeId = employeeDTO.getEmployeeId();
    referenceLetterService.findReferenceLettersByEmployeeId(employeeId).forEach(letter -> referenceLetterService.deleteReferenceLetter(letter.getReferenceId()));
    employeeDao.deleteById(employeeId);
  }

  @Override
  public boolean employeeNumberExists(String employeeNumber) {
    return employeeDao.employeeNumberExists(employeeNumber);
  }

  @Override
  public EmployeeDTO getEmployeeByEmployeeNumber(String employeeNumber) {
    return employeeDao.findByEmployeeNumber(employeeNumber).map(this::convertToDTO).orElse(null);
  }

  @Override
  public EmployeeDTO convertToDTO(Employee employee) {
    return modelMapper.map(employee, EmployeeDTO.class);
  }

  @Override
  public Employee convertToEntity(EmployeeDTO dto) {
    return modelMapper.map(dto, Employee.class);
  }
}
