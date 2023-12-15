package com.ffhs.referencease.services;

import com.ffhs.referencease.dao.interfaces.IEmployeeDAO;
import com.ffhs.referencease.dto.EmployeeDTO;
import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.services.interfaces.IEmployeeService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;


@Stateless
public class EmployeeService implements IEmployeeService {

  private final IEmployeeDAO employeeDao;

  private final ModelMapper modelMapper;

  @Inject
  public EmployeeService(IEmployeeDAO employeeDao, ModelMapper modelMapper) {
    this.employeeDao = employeeDao;
    this.modelMapper = modelMapper;
  }


//  @Override
//  public Optional<EmployeeDTO> getEmployee(UUID id) {
//    return employeeDao.find(id).map(this::convertToDTO);
//  }

  @Override
  public EmployeeDTO getEmployee(UUID id) {
    Optional<Employee> employee = employeeDao.find(id);
    if (employee.isPresent()) {
      return convertToDTO(employee.get());
    } else {
      return null;
    }
  }

  @Override
  public List<EmployeeDTO> getAllEmployees() {
    return employeeDao.findAll().stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  @Override
  public void saveEmployee(EmployeeDTO employeeDTO) {
    Employee employee = modelMapper.map(employeeDTO, Employee.class);
//    employeeDao.save(convertToEntity(employeeDTO));
    employeeDao.save(employee);
  }

  @Override
  public void deleteEmployee(EmployeeDTO employeeDTO) {
    employeeDao.delete(convertToEntity(employeeDTO));
  }

  @Override
  public void deleteEmployeeById(UUID id) {
    employeeDao.deleteById(id);
  }

  @Override
  public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO) {
    return convertToDTO(employeeDao.update(convertToEntity(employeeDTO)));
  }

  @Override
  public boolean existsByEmployeeNumber(String employeeNumber) {
    return employeeDao.findByEmployeeNumber(employeeNumber);
  }

  @Override
  // Hilfsmethode zur Konvertierung von Entity zu DTO
  public EmployeeDTO convertToDTO(Employee employee) {
    return new EmployeeDTO(employee);
  }

  @Override
  // Hilfsmethode zur Konvertierung von DTO zu Entity
  public Employee convertToEntity(EmployeeDTO dto) {
    return new Employee(dto);
  }
}
