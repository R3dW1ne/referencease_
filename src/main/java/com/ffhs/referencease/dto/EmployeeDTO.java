package com.ffhs.referencease.dto;

import com.ffhs.referencease.entities.Department;
import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.entities.Position;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class EmployeeDTO implements Serializable {

  public EmployeeDTO(Employee employee) {
    this.employeeId = employee.getEmployeeId();
    this.employeeNumber = employee.getEmployeeNumber();
    this.firstName = employee.getFirstName();
    this.lastName = employee.getLastName();
    this.dateOfBirth = employee.getDateOfBirth();
    this.phone = employee.getPhone();
    this.startDate = employee.getStartDate();
    this.endDate = employee.getEndDate();
    this.position = employee.getPosition();
    this.department = employee.getDepartment();
  }

  private UUID employeeId;
  private String employeeNumber;
  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;
  private String phone;
  private LocalDate startDate;
  private LocalDate endDate;
  private Position position;
  private Department department;
}
