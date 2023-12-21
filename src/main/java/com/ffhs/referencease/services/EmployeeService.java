package com.ffhs.referencease.services;

import com.ffhs.referencease.dao.interfaces.IEmployeeDAO;
import com.ffhs.referencease.dto.EmployeeDTO;
import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.exceptionhandling.BusinessException;
import com.ffhs.referencease.exceptionhandling.DatabaseException;
import com.ffhs.referencease.exceptionhandling.OperationResult;
import com.ffhs.referencease.services.interfaces.IEmployeeService;
import com.ffhs.referencease.services.interfaces.IReferenceLetterService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;
import java.util.UUID;
import org.modelmapper.ModelMapper;

/**
 * Service-Klasse für die Verwaltung von Mitarbeitern.
 * <p>Diese Klasse bietet Methoden zum Auffinden, Speichern, Aktualisieren und Löschen von
 * Mitarbeitern, sowie zum Konvertieren zwischen Employee-Entitäten und DTOs.</p>
 * <p>Die Klasse verwendet {@link IEmployeeDAO} für den Datenbankzugriff und {@link ModelMapper}
 * für die Objektkonvertierung.</p> $ Autor: Chris Wüthrich
 *
 * @author Chris Wüthrich
 * @version 1.0.0
 */
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

  /**
   * Holt einen Mitarbeiter anhand seiner UUID.
   *
   * @param id Die UUID des Mitarbeiters.
   * @return Ein DTO des Mitarbeiters, falls gefunden, sonst {@code null}.
   */
  @Override
  public EmployeeDTO getEmployee(UUID id) throws DatabaseException {
    return employeeDao.find(id).map(this::convertToDTO).orElse(null);
  }

  /**
   * Liefert eine Liste aller Mitarbeiter als DTOs.
   *
   * @return Eine Liste von EmployeeDTOs.
   */
  @Override
  public List<EmployeeDTO> getAllEmployees() throws DatabaseException {
    return employeeDao.findAll().stream().map(this::convertToDTO).toList();
  }

  /**
   * Zählt die Anzahl der Mitarbeiter in der Datenbank.
   *
   * @return Die Anzahl der Mitarbeiter.
   */
  @Override
  public long countEmployees() throws DatabaseException {

    return employeeDao.countEmployees();
  }

  /**
   * Speichert einen Mitarbeiter in der Datenbank.
   *
   * @param employee Der zu speichernde Mitarbeiter.
   */
  @Override
  public void saveEmployee(Employee employee) throws DatabaseException {

    employeeDao.save(employee);
  }

  /**
   * Speichert oder aktualisiert einen Mitarbeiter basierend auf der Existenz einer ID.
   *
   * @param employeeDTO Das DTO des Mitarbeiters.
   * @return Ein OperationResult mit dem gespeicherten oder aktualisierten Mitarbeiter.
   * @throws BusinessException Bei einem Geschäftsfehler.
   */
  @Override
  public OperationResult<EmployeeDTO> saveOrUpdateEmployee(EmployeeDTO employeeDTO)
      throws BusinessException, DatabaseException {
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

  private Employee saveEmployeeInternal(Employee employee) throws DatabaseException {
    employeeDao.save(employee);
    return employee;
  }

  private Employee updateEmployeeInternal(Employee employee) throws DatabaseException {
    return employeeDao.update(employee);
  }

  /**
   * Löscht einen Mitarbeiter und alle zugehörigen Referenzbriefe.
   *
   * @param employeeDTO Das DTO des zu löschenden Mitarbeiters.
   */
  @Override
  public void deleteEmployee(EmployeeDTO employeeDTO) throws DatabaseException {
    UUID employeeId = employeeDTO.getEmployeeId();
    referenceLetterService.findReferenceLettersByEmployeeId(employeeId)
        .forEach(letter -> referenceLetterService.deleteReferenceLetter(letter.getReferenceId()));
    employeeDao.deleteById(employeeId);
  }

  /**
   * Überprüft, ob eine Mitarbeiternummer bereits existiert.
   *
   * @param employeeNumber Die zu überprüfende Mitarbeiternummer.
   * @return {@code true}, wenn die Nummer existiert, sonst {@code false}.
   */
  @Override
  public boolean employeeNumberExists(String employeeNumber) throws DatabaseException {
    return employeeDao.employeeNumberExists(employeeNumber);
  }

  /**
   * Holt einen Mitarbeiter anhand seiner Mitarbeiternummer.
   *
   * @param employeeNumber Die Mitarbeiternummer des Mitarbeiters.
   * @return Ein DTO des Mitarbeiters, falls gefunden, sonst {@code null}.
   */
  @Override
  public EmployeeDTO getEmployeeByEmployeeNumber(String employeeNumber) throws DatabaseException {
    return employeeDao.findByEmployeeNumber(employeeNumber).map(this::convertToDTO).orElse(null);
  }

  /**
   * Konvertiert eine Employee-Entität in ein DTO.
   *
   * @param employee Die zu konvertierende Employee-Entität.
   * @return Das konvertierte EmployeeDTO.
   */
  @Override
  public EmployeeDTO convertToDTO(Employee employee) {
    return modelMapper.map(employee, EmployeeDTO.class);
  }

  /**
   * Konvertiert ein EmployeeDTO in eine Employee-Entität.
   *
   * @param dto Das zu konvertierende EmployeeDTO.
   * @return Die konvertierte Employee-Entität.
   */
  @Override
  public Employee convertToEntity(EmployeeDTO dto) {
    return modelMapper.map(dto, Employee.class);
  }
}
