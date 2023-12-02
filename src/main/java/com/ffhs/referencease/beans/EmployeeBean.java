package com.ffhs.referencease.beans;

import com.ffhs.referencease.entities.Department;
import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.entities.Position;
import com.ffhs.referencease.services.service_interfaces.IDepartmentService;
import com.ffhs.referencease.services.service_interfaces.IEmployeeService;
import com.ffhs.referencease.services.service_interfaces.IPositionService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Named
@RequestScoped
public class EmployeeBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private final IEmployeeService employeeService;
  private final IPositionService positionService;
  private final IDepartmentService departmentService;

  @Getter
  @Setter
  private Employee employee = new Employee();

  @Getter
  @Setter
  private Employee selectedEmployee;

  @Getter
  @Setter
//  @ManagedProperty("#{param.employeeId}")
  private Long selectedEmployeeId;

  @Getter
  @Setter
  private List<Employee> employees;

  @Getter
  @Setter
  private List<Employee> filteredEmployees;

  @Getter
  private List<Position> positions;

  @Getter
  private List<Department> departments;

  @Inject
  public EmployeeBean(IEmployeeService employeeService, IPositionService positionService,
      IDepartmentService departmentService) {
    this.employeeService = employeeService;
    this.positionService = positionService;
    this.departmentService = departmentService;
  }

  @PostConstruct
  public void init() {
    employees = employeeService.getAllEmployees();
    filteredEmployees = employeeService.getAllEmployees();
    positions = positionService.getAllPositions();
    departments = departmentService.getAllDepartments();
    if (selectedEmployeeId != null) {
      setSelectedEmployeeById(selectedEmployeeId);
    }
  }

  public String loadSelectedEmployeeDetails(Long employeeId) {
    return "/resources/components/sites/secured/employeeDetails.xhtml?faces-redirect=true&employeeId="
        + employeeId;
  }

  public void setSelectedEmployeeById(Long employeeId) {
    selectedEmployee = employeeService.getEmployee(employeeId)
        .orElse(null); // Oder eine andere Handhabung, falls der Mitarbeiter nicht gefunden wird
  }

  public void loadEmployee(Long id) {
    employee = employeeService.getEmployee(id).orElse(new Employee());
  }

  public void saveEmployee() {
    employeeService.saveEmployee(employee);
  }

  public void deleteEmployee() {
    employeeService.deleteEmployee(employee);
  }

  public void updateEmployee() {
    employee = employeeService.updateEmployee(employee);
  }
}