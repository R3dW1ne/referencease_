package com.ffhs.referencease.dto;

import com.ffhs.referencease.annotations.UniqueEmployeeNumber;
import com.ffhs.referencease.converters.jpa.UUIDConverter;
import com.ffhs.referencease.entities.Department;
import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.entities.Gender;
import com.ffhs.referencease.entities.Position;
import jakarta.inject.Inject;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  public EmployeeDTO(Employee employee) {
    this.employeeId = employee.getEmployeeId();
    this.employeeNumber = employee.getEmployeeNumber();
    this.firstName = employee.getFirstName();
    this.lastName = employee.getLastName();
    this.dateOfBirth = employee.getDateOfBirth();
    this.phone = employee.getPhone();
    this.startDate = employee.getStartDate();
    this.gender = employee.getGender();
    this.position = employee.getPosition();
    this.department = employee.getDepartment();
  }


  private UUID employeeId;

  @Column(unique = true)
  private String employeeNumber;
  @NotBlank(message = "First name is mandatory")
  private String firstName;
  @NotBlank(message = "Last name is mandatory")
  private String lastName;
  @NotNull(message = "Date of birth is mandatory")
  private LocalDate dateOfBirth;
//  private String placeOfOrigin;


  private String phone;
  @NotNull(message = "Start date is mandatory")
  private LocalDate startDate;


  @ManyToOne
  @JoinColumn(name = "genderId")
  @NotNull(message = "Gender is mandatory")
  private Gender gender;

  @ManyToOne
  @JoinColumn(name = "positionId")
  @NotNull(message = "Position is mandatory")
  private Position position;

  @ManyToOne
  @JoinColumn(name = "departmentId")
  @NotNull(message = "Department is mandatory")
  private Department department;
}
