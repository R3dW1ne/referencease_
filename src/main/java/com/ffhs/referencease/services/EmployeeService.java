package com.ffhs.referencease.services;

import com.ffhs.referencease.dao.interfaces.IEmployeeDAO;
import com.ffhs.referencease.dto.EmployeeDTO;
import com.ffhs.referencease.entities.Department;
import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.entities.Gender;
import com.ffhs.referencease.entities.Position;
import com.ffhs.referencease.exceptionhandling.BusinessException;
import com.ffhs.referencease.exceptionhandling.DatabaseException;
import com.ffhs.referencease.exceptionhandling.OperationResult;
import com.ffhs.referencease.services.interfaces.IDepartmentService;
import com.ffhs.referencease.services.interfaces.IEmployeeService;
import com.ffhs.referencease.services.interfaces.IGenderService;
import com.ffhs.referencease.services.interfaces.IPositionService;
import com.ffhs.referencease.services.interfaces.IReferenceLetterService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
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
  private final IDepartmentService departmentService;
  private final IPositionService positionService;
  private final IGenderService genderService;
  private final ModelMapper modelMapper;

  @Inject
  public EmployeeService(IEmployeeDAO employeeDao, IReferenceLetterService referenceLetterService,
      IDepartmentService departmentService, IPositionService positionService,
      IGenderService genderService, ModelMapper modelMapper) {
    this.employeeDao = employeeDao;
    this.referenceLetterService = referenceLetterService;
    this.departmentService = departmentService;
    this.positionService = positionService;
    this.genderService = genderService;
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
    validateEmployeeDTO(employeeDTO);
    boolean isNewEmployee = employeeDTO.getEmployeeId() == null;

    if (!isNewEmployee && !employeeExistsInDB(employeeDTO)) {
      return OperationResult.failure("Mitarbeiter existiert nicht mehr.");
    }

    if (employeeNumberIsDuplicate(employeeDTO)) {
      return OperationResult.failure(
          "Mitarbeiternummer bereits an einem anderen Mitarbeiter vergeben.");
    }

    return trySaveOrUpdateEmployee(employeeDTO);
  }

  /**
   * Validiert, ob das übergebene EmployeeDTO-Objekt null ist.
   *
   * @param employeeDTO Das zu validierende EmployeeDTO-Objekt.
   * @throws BusinessException Wenn das EmployeeDTO-Objekt null ist.
   */
  private void validateEmployeeDTO(EmployeeDTO employeeDTO) throws BusinessException {
    if (employeeDTO == null) {
      throw new BusinessException("Mitarbeiter ist null.");
    }
  }

  /**
   * Überprüft, ob ein Mitarbeiter bereits in der Datenbank existiert.
   *
   * @param employeeDTO Das EmployeeDTO-Objekt des zu überprüfenden Mitarbeiters.
   * @return true, wenn der Mitarbeiter in der Datenbank existiert, sonst false.
   */
  private boolean employeeExistsInDB(EmployeeDTO employeeDTO) {
    return employeeDao.employeeIdExists(employeeDTO.getEmployeeId());
  }

  /**
   * Überprüft, ob die Mitarbeiternummer eines Mitarbeiters dupliziert ist.
   *
   * @param employeeDTO Das EmployeeDTO-Objekt mit der zu überprüfenden Mitarbeiternummer.
   * @return true, wenn die Mitarbeiternummer bereits existiert und sie einem anderen Mitarbeiter
   * zugeordnet ist, sonst false.
   */
  private boolean employeeNumberIsDuplicate(EmployeeDTO employeeDTO) {
    String employeeNumber = employeeDTO.getEmployeeNumber();
    boolean employeeNumberExists = employeeDao.employeeNumberExists(employeeNumber);
    EmployeeDTO existingEmployee = getEmployeeByEmployeeNumber(employeeNumber);

    return employeeNumberExists && !existingEmployee.getEmployeeId()
        .equals(employeeDTO.getEmployeeId());
  }

  /**
   * Versucht, einen Mitarbeiter zu speichern oder zu aktualisieren.
   *
   * @param employeeDTO Das EmployeeDTO-Objekt des Mitarbeiters, der gespeichert oder aktualisiert
   *                    werden soll.
   * @return Ein OperationResult-Objekt, das das Ergebnis der Operation enthält.
   * @throws BusinessException Bei einem Fehler im Geschäftsprozess.
   * @throws DatabaseException Bei einem Datenbankfehler.
   */
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

  /**
   * Erstellt eine spezifizierte Anzahl von zufälligen Mitarbeiterdatensätzen, falls noch keine
   * Mitarbeiter in der Datenbank vorhanden sind. Jeder Mitarbeiter wird mit zufälligen Attributen
   * wie Mitarbeiternummer, Namen, Geburtsdatum, Telefonnummer, Anfangsdatum der Beschäftigung,
   * Abteilung, Position und Geschlecht erstellt. Diese Methode wird nur ausgeführt, wenn noch keine
   * Mitarbeiter in der Datenbank existieren.
   *
   * @param count  Die Anzahl der zu erstellenden zufälligen Mitarbeiter.
   * @param random Das Random-Objekt zur Erzeugung zufälliger Werte.
   */
  @Override
  public void createRandomEmployeesIfNotExists(int count, Random random) {
    // Überprüfen, ob bereits Mitarbeiter in der Datenbank existieren
    long employeeCount = countEmployees();
    if (employeeCount > 0) {
      // Es gibt bereits Mitarbeiter, also keine neuen hinzufügen
      return;
    }

    for (int i = 0; i < count; i++) {
      Employee employee = new Employee();
      employee.setEmployeeNumber(
          UUID.randomUUID().toString().substring(0, 8)); // Zufällige Mitarbeiternummer
      employee.setFirstName("Firstname" + i);
      employee.setLastName("Lastname" + i);
      employee.setDateOfBirth(LocalDate.now().minusYears(
          random.nextInt(40) + 18)); // Zufälliges Geburtsdatum zwischen 18 und 58 Jahren
      employee.setPhone("123-456-7890"); // Beispieltelefonnummer
      employee.setStartDate(LocalDate.now().minusYears(random.nextInt(
          10))); // Zufälliges Anfangsdatum der Beschäftigung in den letzten 10 Jahren
      // Abteilung kann auch zufällig zugewiesen werden
      Department randomDepartment = departmentService.getRandomDepartment(random);
      if (randomDepartment != null) {
        employee.setDepartment(randomDepartment);
      }

      // Zufällige Position auswählen
      Position randomPosition = positionService.getRandomPosition(random);
      if (randomPosition != null) {
        employee.setPosition(randomPosition);
      }

      // Zufälliges Gender auswählen
      Gender randomGender = genderService.getRandomGender(random);
      if (randomGender != null) {
        employee.setGender(randomGender);
      }
      saveEmployee(employee);
    }
  }
}
