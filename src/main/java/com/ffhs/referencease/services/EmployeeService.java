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
import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
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
  //  @Override
  //  public OperationResult<EmployeeDTO> saveOrUpdateEmployee(EmployeeDTO employeeDTO)
  //      throws BusinessException, DatabaseException {
  //    if (employeeDTO == null) {
  //      throw new BusinessException("Mitarbeiter ist null.");
  //    }
  //    boolean isNewEmployee = employeeDTO.getEmployeeId() == null;
  //    boolean employeeToUpdateIsStillInDB = employeeDao.employeeIdExists(employeeDTO.getEmployeeId());
  //    if (!isNewEmployee && !employeeToUpdateIsStillInDB) {
  //      return OperationResult.failure("Mitarbeiter existiert nicht mehr.");
  //    }
  //
  //    String employeeNumberToSave = employeeDTO.getEmployeeNumber();
  //    boolean employeeNumberExists = employeeDao.employeeNumberExists(employeeNumberToSave);
  //    EmployeeDTO existingEmployee = getEmployeeByEmployeeNumber(employeeNumberToSave);
  //
  //    if (employeeNumberExists && (isNewEmployee || !existingEmployee.getEmployeeId()
  //        .equals(employeeDTO.getEmployeeId()))) {
  //      return OperationResult.failure(
  //          "Mitarbeiternummer bereits von einem anderen Mitarbeiter vergeben.");
  //    }
  //
  //    EmployeeDTO savedEmployeeDTO;
  //    Employee savedEmployee;
  //    try {
  //      savedEmployee = convertToEntity(employeeDTO);
  //      savedEmployee = employeeDao.update(savedEmployee);
  //      savedEmployeeDTO = convertToDTO(savedEmployee);
  //      return OperationResult.success(savedEmployeeDTO);
  //    } catch (DatabaseException | BusinessException e) {
  //      throw new BusinessException("Fehler beim Speichern des Mitarbeiters.", e);
  //    }
  //  }
  @Override
  public OperationResult<EmployeeDTO> saveOrUpdateEmployee(EmployeeDTO employeeDTO)
      throws BusinessException, DatabaseException {
    validateEmployeeDTO(employeeDTO);
    boolean isNewEmployee = employeeDTO.getEmployeeId() == null;

    if (!isNewEmployee && !employeeExistsInDB(employeeDTO)) {
      return OperationResult.failure("Mitarbeiter existiert nicht mehr.");
    }

    if (employeeNumberIsDuplicate(employeeDTO)) {
      return OperationResult.failure(
          "Mitarbeiternummer bereits von einem anderen Mitarbeiter vergeben.");
    }

    return trySaveOrUpdateEmployee(employeeDTO);
  }

  private void validateEmployeeDTO(EmployeeDTO employeeDTO) throws BusinessException {
    if (employeeDTO == null) {
      throw new BusinessException("Mitarbeiter ist null.");
    }
  }

  private boolean employeeExistsInDB(EmployeeDTO employeeDTO) {
    return employeeDao.employeeIdExists(employeeDTO.getEmployeeId());
  }

  private boolean employeeNumberIsDuplicate(EmployeeDTO employeeDTO) {
    String employeeNumber = employeeDTO.getEmployeeNumber();
    boolean employeeNumberExists = employeeDao.employeeNumberExists(employeeNumber);
    EmployeeDTO existingEmployee = getEmployeeByEmployeeNumber(employeeNumber);

    return employeeNumberExists && !existingEmployee.getEmployeeId()
        .equals(employeeDTO.getEmployeeId());
  }

  private OperationResult<EmployeeDTO> trySaveOrUpdateEmployee(EmployeeDTO employeeDTO)
      throws BusinessException, DatabaseException {
    try {
      Employee savedEmployee = convertToEntity(employeeDTO);
      savedEmployee = employeeDao.update(savedEmployee);
      EmployeeDTO savedEmployeeDTO = convertToDTO(savedEmployee);
      return OperationResult.success(savedEmployeeDTO);
    } catch (DatabaseException | BusinessException e) {
      throw new BusinessException("Fehler beim Speichern des Mitarbeiters.", e);
    }
  }


  private EmployeeDTO saveEmployeeInternal(Employee employee) throws DatabaseException {
    try {
      employeeDao.save(employee);
    } catch (DatabaseException e) {
      throw new DatabaseException("Fehler beim Speichern des Mitarbeiters.", e);
    }
    return convertToDTO(employee);
  }

  private EmployeeDTO updateEmployeeInternal(Employee employee) throws DatabaseException {
    try {
      employeeDao.update(employee);
    } catch (DatabaseException e) {
      throw new DatabaseException("Fehler beim Aktualisieren des Mitarbeiters.", e);
    }
    return convertToDTO(employee);
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
  public EmployeeDTO getEmployeeByEmployeeNumber(String employeeNumber)
      throws DatabaseException, BusinessException {
    return employeeDao.findByEmployeeNumber(employeeNumber).map(this::convertToDTO).orElse(null);
  }

  /**
   * Konvertiert eine Employee-Entität in ein DTO.
   *
   * @param employee Die zu konvertierende Employee-Entität.
   * @return Das konvertierte EmployeeDTO.
   */
  @Override
  public EmployeeDTO convertToDTO(Employee employee) throws BusinessException {
    try {
      return modelMapper.map(employee, EmployeeDTO.class);
    } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
      throw new BusinessException("Problem beim Konvertieren zu einem DTO.", e);
    }
  }

  /**
   * Konvertiert ein EmployeeDTO in eine Employee-Entität.
   *
   * @param dto Das zu konvertierende EmployeeDTO.
   * @return Die konvertierte Employee-Entität.
   */
  @Override
  public Employee convertToEntity(EmployeeDTO dto) throws BusinessException {
    try {
      return modelMapper.map(dto, Employee.class);
    } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
      throw new BusinessException("Problem beim Konvertieren zu einer Entität.", e);
    }
  }
}
