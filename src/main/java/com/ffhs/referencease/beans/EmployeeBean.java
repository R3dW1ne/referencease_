package com.ffhs.referencease.beans;

import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.entityservices.EmployeeService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import lombok.Getter;


@Named
@ViewScoped
public class EmployeeBean implements Serializable {
  private static final long serialVersionUID = 1L;

  @Inject
  private transient EmployeeService employeeService;

  // Getter und Setter
  @Getter
  private transient Employee employee;

  @PostConstruct
  public void init() {
    employee = new Employee();
  }

  public void saveEmployee() {
    employeeService.save(employee);
    // Hier könnten Sie auch eine Erfolgsmeldung anzeigen oder zu einer anderen Seite navigieren
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }
}
