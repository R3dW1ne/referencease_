package com.ffhs.referencease.beans;

import com.ffhs.referencease.entities.Department;
import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.entities.Position;
import com.ffhs.referencease.services.interfaces.IDepartmentService;
import com.ffhs.referencease.services.interfaces.IEmployeeService;
import com.ffhs.referencease.services.interfaces.IPositionService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Named
@RequestScoped
public class EmployeeBean{

//  private static final long serialVersionUID = 1L;

  private final IEmployeeService employeeService;
  private final IPositionService positionService;
  private final IDepartmentService departmentService;

  @Getter
  @Setter
  private Employee employee = new Employee();

//  @Getter
//  @Setter
//  private Employee selectedEmployee;

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
  }

  public String loadSelectedEmployeeDetails(UUID employeeId) {
    return "/resources/components/sites/secured/employeeDetails.xhtml?faces-redirect=true&employeeId="
        + employeeId;
  }

//  public void setSelectedEmployeeById(UUID employeeId) {
//    selectedEmployee = employeeService.getEmployee(employeeId)
//        .orElse(null); // Oder eine andere Handhabung, falls der Mitarbeiter nicht gefunden wird
//  }

  public void loadEmployee(UUID id) {
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