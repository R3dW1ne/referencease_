package com.ffhs.referencease.beans;

import com.ffhs.referencease.dto.EmployeeDTO;
import com.ffhs.referencease.entities.Department;
import com.ffhs.referencease.entities.Gender;
import com.ffhs.referencease.entities.Position;
import com.ffhs.referencease.exceptionhandling.DatabaseException;
import com.ffhs.referencease.exceptionhandling.OperationResult;
import com.ffhs.referencease.services.interfaces.IDepartmentService;
import com.ffhs.referencease.services.interfaces.IEmployeeService;
import com.ffhs.referencease.services.interfaces.IGenderService;
import com.ffhs.referencease.services.interfaces.IPositionService;
import com.ffhs.referencease.services.interfaces.IReferenceLetterService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;

/**
 * Bean-Klasse zur Verwaltung von Mitarbeiterdaten. Diese Klasse dient als Managed Bean für die
 * JSF-Oberfläche, um CRUD-Operationen für Mitarbeiter durchzuführen und Mitarbeiterdaten für die
 * Benutzeroberfläche bereitzustellen. Sie verwendet verschiedene Service-Klassen, um Geschäftslogik
 * auf Mitarbeiterdaten anzuwenden.
 *
 * @author Chris Wüthrich
 * @version 1.0.0
 */
@Named
@Setter
@Getter
@SessionScoped
public class EmployeeBean implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private static final Logger LOG = Logger.getLogger(EmployeeBean.class.getName());

  private final transient IEmployeeService employeeService;
  private final transient IPositionService positionService;
  private final transient IDepartmentService departmentService;
  private final transient IGenderService genderService;
  private final transient IReferenceLetterService referenceLetterService;

  private EmployeeDTO employee;
  private EmployeeDTO selectedEmployee;
  private String selectedEmployeeIdAsString;
  private Boolean editMode = false;
  private Boolean listSelectionNeeded = false;
  private List<EmployeeDTO> employees;
  private List<EmployeeDTO> filteredEmployees;
  private List<Position> positions;
  private List<Department> departments;
  private List<Gender> genders;

  @Inject
  public EmployeeBean(IEmployeeService employeeService, IPositionService positionService,
      IDepartmentService departmentService, IGenderService genderService,
      IReferenceLetterService referenceLetterService) {
    this.employeeService = employeeService;
    this.positionService = positionService;
    this.departmentService = departmentService;
    this.genderService = genderService;
    this.referenceLetterService = referenceLetterService;
  }

  @PostConstruct
  public void init() {
    employee = new EmployeeDTO();
    employees = employeeService.getAllEmployees();
    filteredEmployees = employeeService.getAllEmployees();
    positions = positionService.getAllPositions();
    departments = departmentService.getAllDepartments();
    genders = genderService.getAllGenders();
  }

  /**
   * Speichert oder aktualisiert einen Mitarbeiter.
   *
   * @param referenceLetterBean Bean für das zugehörige Referenzschreiben.
   */
  public void saveOrUpdateEmployee(ReferenceLetterBean referenceLetterBean) {
    boolean isNewEmployee = selectedEmployee.getEmployeeId() == null;
    String message = "";
    OperationResult<EmployeeDTO> result;
    try {
      result = employeeService.saveOrUpdateEmployee(selectedEmployee);
    } catch (DatabaseException e) {
      LOG.severe(e.getMessage());
      sendInfoToFrontend("Fehler beim Speichern des Mitarbeiters.");
      return;
    }

    if (result.isSuccess()) {
      EmployeeDTO savedEmployee = result.getData();

      if (Boolean.TRUE.equals(listSelectionNeeded)) {
        referenceLetterBean.getReferenceLetter()
            .setEmployee(employeeService.convertToEntity(savedEmployee));
      }

      selectedEmployee = savedEmployee;
      editMode = true;

      employees = employeeService.getAllEmployees();
      filteredEmployees = employeeService.getAllEmployees();

      message = isNewEmployee ? "erfolgreich gespeichert." : "erfolgreich aktualisiert.";
      LOG.info(message);
      sendInfoToFrontend(
          "Mitarbeiter " + savedEmployee.getFirstName() + " " + savedEmployee.getLastName() + " "
              + message);
    } else {
      message = result.getErrorMessage();
      LOG.warning(message);
      sendInfoToFrontend(message);
    }
  }

  /**
   * Sendet eine Nachricht an die Benutzeroberfläche.
   *
   * @param message Die Nachricht, die angezeigt werden soll.
   */
  public void sendInfoToFrontend(String message) {
    FacesContext.getCurrentInstance()
        .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    PrimeFaces.current().ajax()
        .update("employeeListForm:messages", "employeeListForm:employeeTable");
  }

  /**
   * Löscht einen Mitarbeiter und alle zugehörigen Referenzbriefe.
   */
  public void deleteEmployee() {
    try {
      employeeService.deleteEmployee(selectedEmployee);
    } catch (DatabaseException e) {
      LOG.severe(e.getMessage());
      sendInfoToFrontend("Fehler beim löschen des Mitarbeiters.");
      return;
    }
    refreshEmployeeList();
    sendInfoToFrontend("Mitarbeiter erfolgreich gelöscht.");
  }

  private void refreshEmployeeList() {
    employees = employeeService.getAllEmployees();
    filteredEmployees = new ArrayList<>(employees);
    selectedEmployee = null;
  }

  public void resetEmployee() {
    employee = new EmployeeDTO();
    selectedEmployee = new EmployeeDTO();
    editMode = false;
  }

  public void resetEmployeeList() {
    employees = employeeService.getAllEmployees();
    filteredEmployees = employeeService.getAllEmployees();
    listSelectionNeeded = false;
  }

  public String navigateToEmployeeList() {
    resetEmployeeList();
    return "/resources/components/sites/secured/employeeList.xhtml?faces-redirect=true";
  }

  public String newEmployee() {
    resetEmployee();
    return "/resources/components/sites/secured/employeeMod.xhtml?faces-redirect=true";
  }

  public void startEdit(EmployeeDTO employee) {
    this.selectedEmployee = employee;
    this.editMode = true;
  }
}