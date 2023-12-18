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
  public EmployeeService(IEmployeeDAO employeeDao, IReferenceLetterService referenceLetterService, ModelMapper modelMapper) {
    this.employeeDao = employeeDao;
    this.referenceLetterService = referenceLetterService;
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
    employeeDao.save(employee);
  }

  @Override
  public OperationResult<EmployeeDTO> saveOrUpdateEmployee(EmployeeDTO employeeDTO) throws BusinessException{
    EmployeeDTO existingEmployee = getEmployeeByEmployeeNumber(employeeDTO.getEmployeeNumber());
    boolean isNewEmployee = employeeDTO.getEmployeeId() == null;

    if (existingEmployee != null && (isNewEmployee || !existingEmployee.getEmployeeId().equals(employeeDTO.getEmployeeId()))) {
      return OperationResult.failure("Mitarbeiternummer bereits von einem anderen Mitarbeiter vergeben.");
    }

    Employee employeeEntity = convertToEntity(employeeDTO);
    Employee savedEmployee = isNewEmployee ? saveEmployeeInternal(employeeEntity) : updateEmployeeInternal(employeeEntity);
    return OperationResult.success(convertToDTO(savedEmployee));
  }

  private Employee saveEmployeeInternal(Employee employee) {
    employeeDao.save(employee);
    return employee;
  }

  private Employee updateEmployeeInternal(Employee employee) {
    return employeeDao.update(employee);
  }

//  @Override
//  public boolean saveOrUpdateEmployee(EmployeeDTO employeeDTO,
//      ReferenceLetterBean referenceLetterBean) {
//    Employee employee = modelMapper.map(employeeDTO, Employee.class);
//
//    // Überprüfen, ob der Employee neu ist oder aktualisiert werden soll
//    boolean isNewEmployee = employee.getEmployeeId() == null;
//
//    // Überprüfen, ob ein anderer Employee mit derselben EmployeeNumber existiert
//    EmployeeDTO existingEmployee = getEmployeeByEmployeeNumber(employeeDTO.getEmployeeNumber());
//    if (existingEmployee != null && (!isNewEmployee && !existingEmployee.getEmployeeId().equals(employee.getEmployeeId()))) {
//      return false; // EmployeeNumber ist bereits vergeben
//    }
//
////    if (Boolean.TRUE.equals(referenceLetterBean.getListSelectionNeeded())) {
////      Employee employeeForReferenceLetter = convertToEntity(referenceLetterBean.getEmployeeAsDTO());
////      referenceLetterBean.getReferenceLetter().setEmployee(employeeForReferenceLetter);
////    }
//
//    if (isNewEmployee) {
//      employeeDao.save(employee);
//    } else {
//      employeeDao.update(employee);
//    }
//    return true; // Operation erfolgreich
//  }

  @Override
  public void deleteEmployee(EmployeeDTO employeeDTO) {
    UUID employeeId = employeeDTO.getEmployeeId();

    // Referenzbriefe des Mitarbeiters abrufen und löschen
    List<ReferenceLetter> referenceLetters = referenceLetterService.findReferenceLettersByEmployeeId(employeeId);
    for (ReferenceLetter letter : referenceLetters) {
      // Optional: Informationen des Mitarbeiters im Referenzbrief speichern, bevor der Mitarbeiter gelöscht wird
      // Beispiel:
      // letter.setEmployeeName(employeeDTO.getFirstName() + " " + employeeDTO.getLastName());
      // letter.setEmployeePosition(employeeDTO.getPosition().getPositionName());
      // letter.setEmployeeDepartment(employeeDTO.getDepartment().getDepartmentName());
      // letter.setEmployee(null); // Entfernen der Verbindung zum Mitarbeiter
      // referenceLetterService.updateReferenceLetter(letter); // Speichern der Änderungen
      referenceLetterService.deleteReferenceLetter(letter.getReferenceId());
    }

    // Mitarbeiter löschen
    employeeDao.deleteById(employeeId);
}


//  @Override
//  public void deleteEmployee(EmployeeDTO employeeDTO) {
//    employeeDao.delete(convertToEntity(employeeDTO));
//  }

  @Override
  public void deleteEmployeeById(UUID id) {
    employeeDao.deleteById(id);
  }

  @Override
  public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO) {
    return convertToDTO(employeeDao.update(convertToEntity(employeeDTO)));
  }

  @Override
  public boolean employeeNumberExists(String employeeNumber) {
    return employeeDao.employeeNumberExists(employeeNumber);
  }

  @Override
  public EmployeeDTO getEmployeeByEmployeeNumber(String employeeNumber) {
    Optional<Employee> employee = employeeDao.findByEmployeeNumber(employeeNumber);
    return employee.map(this::convertToDTO).orElse(null);
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
