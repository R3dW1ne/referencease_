package com.ffhs.referencease.beans;

import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.services.service_interfaces.IEmployeeService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;
import lombok.Getter;

@Named
@RequestScoped
public class EmployeeBean {

//  @Inject
//  private IEmployeeService employeeService;

  private final IEmployeeService employeeService;

  @Inject
  public EmployeeBean(IEmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  // Getter und Setter
  @Getter
  private Employee employee = new Employee();

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

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  public List<Employee> getEmployees() {
    return employeeService.getAllEmployees();
  }
}