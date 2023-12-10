package com.ffhs.referencease.beans;

import com.ffhs.referencease.dto.EmployeeDTO;
import com.ffhs.referencease.entities.Department;
import com.ffhs.referencease.entities.Gender;
import com.ffhs.referencease.entities.Position;
import com.ffhs.referencease.services.interfaces.IDepartmentService;
import com.ffhs.referencease.services.interfaces.IEmployeeService;
import com.ffhs.referencease.services.interfaces.IGenderService;
import com.ffhs.referencease.services.interfaces.IPositionService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;

@Named
@SessionScoped
public class EmployeeBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger LOG = Logger.getLogger(EmployeeBean.class.getName());

  @ManagedProperty(value = "#{referenceLetterBean}")
  private ReferenceLetterBean referenceLetterBean;

  private final transient IEmployeeService employeeService;
  private final transient IPositionService positionService;
  private final transient IDepartmentService departmentService;
  private final transient IGenderService genderService;

  @Getter
  @Setter
  private EmployeeDTO employee;


  @Getter
  @Setter
  private EmployeeDTO selectedEmployee;





//  @Getter
//  @Setter
//  private UUID selectedEmployeeId;

  @Getter
  @Setter
  private String selectedEmployeeIdAsString;

  @Getter
  @Setter
  private Boolean editMode = false;

  @Getter
  @Setter
  private List<EmployeeDTO> employees;

//  @Getter
//  @Setter
//  private List<EmployeeDTO> selectedEmployees;

  @Getter
  @Setter
  private List<EmployeeDTO> filteredEmployees;

  @Getter
  private List<Position> positions;

  @Getter
  private List<Department> departments;

  @Getter
  private List<Gender> genders;

  @Inject
  public EmployeeBean(IEmployeeService employeeService, IPositionService positionService,
      IDepartmentService departmentService, IGenderService genderService) {
    this.employeeService = employeeService;
    this.positionService = positionService;
    this.departmentService = departmentService;
    this.genderService = genderService;
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

//  public String loadSelectedEmployeeDetails(UUID employeeId) {
//    selectedEmployeeId = employeeId;
//    return "/resources/components/sites/secured/employeeDetails.xhtml?faces-redirect=true";
//  }

  public void onEmployeeSelect() {

  }

  // Methode, um den ausgewählten Employee in ReferenceLetterBean zu setzen
  public void selectEmployeeForReferenceLetter(EmployeeDTO employeeDTO) {
    referenceLetterBean.setEmployee(employeeDTO);
  }

  public void loadSelectedEmployeeDetails(UUID employeeId) {
    employee = employeeService.getEmployee(employeeId);
    editMode = true;
  }

  public String loadSelectedEmployeeDetailsAndNavigate(UUID employeeId) {
    loadSelectedEmployeeDetails(employeeId);
    return "/resources/components/sites/secured/employeeMod.xhtml?faces-redirect=true";
  }

//  public void loadSelectedEmployeeDetails() {
//    LOG.info("Selected Employee ID: " + selectedEmployeeId);
//    if (selectedEmployeeId != null) {
//      selectedEmployee = employeeService.getEmployee(selectedEmployeeId);
//      // Weitere Aktionen, falls erforderlich
//    }
//  }

//  public String navigateToCreateEmployee() {
//    editMode = false;
//    employeeDTO = new EmployeeDTO();
//    return "/resources/components/sites/secured/createEmployeeImpl.xhtml?faces-redirect=true";
//  }

  public String setEditModeToFalseAndNavigate() {
    editMode = false;
    employee = new EmployeeDTO();
    return "/resources/components/sites/secured/employeeMod.xhtml?faces-redirect=true";
  }

//  public void setSelectedEmployeeById(UUID employeeId) {
//    selectedEmployee = employeeService.getEmployee(employeeId);
//  }

  public void loadEmployee(UUID id) {
    employee = employeeService.getEmployee(id);
  }

//  public Employee loadEmployee(EmployeeDTO employeeDTO) {
//    Employee employee = employeeService.
//  }

  public void saveEmployee() {
//    employee.setDepartment();
    employee = employeeService.updateEmployee(employee);
//    -- Only needed if session scoped --
    employees = employeeService.getAllEmployees();
    filteredEmployees = employeeService.getAllEmployees();
//    -- Only needed if session scoped --
    FacesContext.getCurrentInstance().addMessage(null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Mitarbeiter erfolgreich gespeichert!", null));
  }

  public boolean hasSelectedEmployee() {
    return this.selectedEmployee != null;
  }

  public void deleteEmployee() {
    employees.removeIf(employeeDTO -> employeeDTO.getEmployeeId().equals(selectedEmployee.getEmployeeId()));
    filteredEmployees.remove(selectedEmployee);
    employeeService.deleteEmployee(selectedEmployee);
    selectedEmployee = null;
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Product Removed"));
    PrimeFaces.current().ajax().update("employeeListForm:messages", "employeeListForm:employeeTable");
  }

  public void openNew() {
    this.selectedEmployee = new EmployeeDTO();
  }

  public void deleteEmployee(EmployeeDTO employee) {
    employeeService.deleteEmployee(employee);
    employees = employeeService.getAllEmployees();
    filteredEmployees = employeeService.getAllEmployees();
    FacesContext.getCurrentInstance().addMessage(null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Mitarbeiter erfolgreich gelöscht", null));
    PrimeFaces.current().ajax().update("form:messages", "form:dt-products");
  }

  public void deleteEmployeeById(UUID id) {
    employeeService.deleteEmployeeById(id);
    employees = employeeService.getAllEmployees();
    filteredEmployees = employeeService.getAllEmployees();
    FacesContext.getCurrentInstance().addMessage(null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Mitarbeiter erfolgreich gelöscht", null));
  }

  public void updateEmployee() {
    employee = employeeService.updateEmployee(employee);
  }

//  public EmployeeDTO getSelectedEmployeeByIdString(String employeeId) {
//    UUID uuid = UUID.fromString(employeeId);
//    if (selectedEmployee != null && selectedEmployee.getEmployeeId().equals(uuid)) {
//      return selectedEmployee;
//    }
//    selectedEmployee = employeeService.getEmployee(uuid);
//    return selectedEmployee;
//  }

  public void resetEmployee() {
    employee = new EmployeeDTO();
    selectedEmployee = null;
    editMode = false;
  }

//  public EmployeeDTO getSelectedEmployee() {
//    if (selectedEmployee != null && selectedEmployee.getEmployeeId().equals(selectedEmployeeId)) {
//      return selectedEmployee;
//    }
//    selectedEmployee = employeeService.getEmployee(selectedEmployeeId);
//    return selectedEmployee;
//  }
}