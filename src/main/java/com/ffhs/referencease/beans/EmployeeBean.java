package com.ffhs.referencease.beans;

import com.ffhs.referencease.dto.EmployeeDTO;
import com.ffhs.referencease.entities.Department;
import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.entities.Gender;
import com.ffhs.referencease.entities.Position;
import com.ffhs.referencease.exceptionhandling.BusinessException;
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
//    selectedEmployee = new EmployeeDTO();
    employees = employeeService.getAllEmployees();
    filteredEmployees = employeeService.getAllEmployees();
    positions = positionService.getAllPositions();
    departments = departmentService.getAllDepartments();
    genders = genderService.getAllGenders();
  }

//  /**
//   * Speichert oder aktualisiert einen Mitarbeiter basierend auf dem Vorhandensein einer ID.
//   *
//   * @param referenceLetterBean Referenz zum ReferenceLetterBean für den aktuellen Kontext.
//   */
//  public void saveOrUpdateEmployee(ReferenceLetterBean referenceLetterBean) {
//    boolean isSaved = employeeService.saveOrUpdateEmployee(selectedEmployee, referenceLetterBean);
//    if (Boolean.TRUE.equals(listSelectionNeeded)) {
//      Employee employeeToSaveOrUpdate = employeeService.convertToEntity(selectedEmployee);
//      referenceLetterBean.getReferenceLetter().setEmployee(employeeToSaveOrUpdate);
//    }
//    if (isSaved) {
//      refreshEmployeeList();
//      sendInfoToFrontend("Mitarbeiter erfolgreich gespeichert/aktualisiert.");
//      editMode = true;
//    }
//  }


  public void saveOrUpdateEmployee(ReferenceLetterBean referenceLetterBean) {
    boolean isNewEmployee = selectedEmployee.getEmployeeId() == null;
    OperationResult<EmployeeDTO> result = employeeService.saveOrUpdateEmployee(selectedEmployee);

    if (result.isSuccess()) {
      EmployeeDTO savedEmployee = result.getData();

      if (Boolean.TRUE.equals(listSelectionNeeded)) {
        referenceLetterBean.getReferenceLetter().setEmployee(employeeService.convertToEntity(savedEmployee));
      }

      selectedEmployee = savedEmployee;
      editMode = true;

      employees = employeeService.getAllEmployees();
      filteredEmployees = employeeService.getAllEmployees();

      String message = isNewEmployee ? "erfolgreich gespeichert" : "erfolgreich aktualisiert";
      sendInfoToFrontend("Mitarbeiter " + savedEmployee.getFirstName() + " " + savedEmployee.getLastName() + " " + message);
    } else {
      sendInfoToFrontend(result.getErrorMessage());
    }
  }


//  public void saveOrUpdateEmployee(ReferenceLetterBean referenceLetterBean) {
//    EmployeeDTO existingEmployee = employeeService.getEmployeeByEmployeeNumber(selectedEmployee.getEmployeeNumber());
//
//    // Überprüfen, ob der Employee neu ist oder aktualisiert werden soll
//    boolean isNewEmployee = selectedEmployee.getEmployeeId() == null;
//
//    // Überprüfen für neuen Employee: EmployeeNumber darf nicht bereits verwendet werden
//    // Überprüfen für bestehenden Employee: EmployeeNumber darf nur verwendet werden, wenn sie zum aktuellen Employee gehört
//    if (existingEmployee != null && (isNewEmployee || !existingEmployee.getEmployeeId().equals(selectedEmployee.getEmployeeId()))) {
//      sendInfoToFrontend("Mitarbeiternummer bereits von einem anderen Mitarbeiter vergeben.");
//      return;
//    }
//
//    if (Boolean.TRUE.equals(listSelectionNeeded)) {
//      Employee employeeToSaveOrUpdate = employeeService.convertToEntity(selectedEmployee);
//      referenceLetterBean.getReferenceLetter().setEmployee(employeeToSaveOrUpdate);
//    }
//    employee = employeeService.updateEmployee(selectedEmployee);
//    selectedEmployee = employee;
//    editMode = true;
////    -- Only needed if session scoped --
//    employees = employeeService.getAllEmployees();
//    filteredEmployees = employeeService.getAllEmployees();
////    -- Only needed if session scoped --
//    String message = isNewEmployee ? "erfolgreich gespeichert" : "erfolgreich aktualisiert";
//    sendInfoToFrontend("Mitarbeiter " + selectedEmployee.getFirstName() + " " + selectedEmployee.getLastName() + " " + message);
//  }


  public void sendInfoToFrontend(String message) {
    FacesContext.getCurrentInstance().addMessage(null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    PrimeFaces.current().ajax()
        .update("employeeListForm:messages", "employeeListForm:employeeTable");
  }

  public boolean hasSelectedEmployee() {
    return this.selectedEmployee != null;
  }


  /**
   * Löscht einen Mitarbeiter und alle zugehörigen Referenzbriefe.
   */
  public void deleteEmployee() {
    employeeService.deleteEmployee(selectedEmployee);
    refreshEmployeeList();
    sendInfoToFrontend("Mitarbeiter erfolgreich gelöscht.");
  }

  private void refreshEmployeeList() {
    employees = employeeService.getAllEmployees();
    filteredEmployees = new ArrayList<>(employees);
    selectedEmployee = null;
  }
//  public void deleteEmployee() {
//    List<ReferenceLetter> referenceLetters = referenceLetterService.findReferenceLettersByEmployeeId(selectedEmployee.getEmployeeId());
//////    zukünftige implementation für das behalten der Arbeitszeugnisse
////    for (ReferenceLetter letter : referenceLetters) {
////      letter.setEmployeeName(selectedEmployee.getFirstName() + " " + selectedEmployee.getLastName());
////      letter.setEmployeePosition(selectedEmployee.getPosition().getPositionName()); // Annahme: Position hat ein Feld 'positionName'
////      letter.setEmployeeDepartment(selectedEmployee.getDepartment().getDepartmentName()); // Annahme: Department hat ein Feld 'departmentName'
////      letter.setEmployee(null); // Entfernen der Verbindung zum Employee
////      referenceLetterService.updateReferenceLetter(letter); // Speichern der Änderungen
////    }
//    for (ReferenceLetter letter : referenceLetters) {
//      referenceLetterService.deleteReferenceLetter(letter.getReferenceId()); // Speichern der Änderungen
//    }
//
//    employees.removeIf(
//        employeeDTO -> employeeDTO.getEmployeeId().equals(selectedEmployee.getEmployeeId()));
//    filteredEmployees.remove(selectedEmployee);
//    employeeService.deleteEmployee(selectedEmployee);
//    String message =
//        "Mitarbeiter " + selectedEmployee.getFirstName() + " " + selectedEmployee.getLastName()
//            + " erfolgreich gelöscht";
//    selectedEmployee = null;
//    FacesContext.getCurrentInstance()
//        .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, message, null));
//    PrimeFaces.current().ajax()
//        .update("employeeListForm:messages", "employeeListForm:employeeTable");
//  }

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