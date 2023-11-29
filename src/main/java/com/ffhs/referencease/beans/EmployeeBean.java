package com.ffhs.referencease.beans;

import com.ffhs.referencease.entities.Department;
import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.entities.Position;
import com.ffhs.referencease.services.service_interfaces.IDepartmentService;
import com.ffhs.referencease.services.service_interfaces.IEmployeeService;
import com.ffhs.referencease.services.service_interfaces.IPositionService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Named
@RequestScoped
public class EmployeeBean {

  private final IEmployeeService employeeService;
  private final IPositionService positionService;
  private final IDepartmentService departmentService;

  @Getter @Setter
  private Employee employee = new Employee();

  @Getter
  private List<Position> positions;

  @Getter
  private List<Department> departments;

  @Inject
  public EmployeeBean(IEmployeeService employeeService, IPositionService positionService, IDepartmentService departmentService) {
    this.employeeService = employeeService;
    this.positionService = positionService;
    this.departmentService = departmentService;
  }

  @PostConstruct
  public void init() {
    positions = positionService.getAllPositions();
    departments = departmentService.getAllDepartments();
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

  public List<Employee> getEmployees() {
    return employeeService.getAllEmployees();
  }
}