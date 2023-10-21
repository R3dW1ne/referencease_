package com.ffhs.referencease.beans;

import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.entityservices.EmployeeService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@Named
@ViewScoped
public class EmployeeBean {

  @Inject
  private EmployeeService employeeService;


  private Employee employee;

  @PostConstruct
  public void init() {
    employee = new Employee();
  }

  public void saveEmployee() {
    employeeService.save(employee);
    // Hier könnten Sie auch eine Erfolgsmeldung anzeigen oder zu einer anderen Seite navigieren
  }
//
//  // Getter und Setter
//  public Employee getEmployee() {
//    return employee;
//  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }
}
