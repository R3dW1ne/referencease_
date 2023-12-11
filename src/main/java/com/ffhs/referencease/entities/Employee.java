package com.ffhs.referencease.entities;

import com.ffhs.referencease.annotations.UniqueEmployeeNumber;
import com.ffhs.referencease.converters.jpa.UUIDConverter;
import com.ffhs.referencease.dto.EmployeeDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "Employee")
public class Employee implements Serializable {

  private static final long serialVersionUID = 1L;

  public Employee(EmployeeDTO dto) {
    this.employeeId = dto.getEmployeeId();
    this.employeeNumber = dto.getEmployeeNumber();
    this.firstName = dto.getFirstName();
    this.lastName = dto.getLastName();
    this.dateOfBirth = dto.getDateOfBirth();
//    this.placeOfOrigin = dto.getPlaceOfOrigin();
    this.phone = dto.getPhone();
    this.startDate = dto.getStartDate();
    this.gender = dto.getGender();
    this.position = dto.getPosition();
    this.department = dto.getDepartment();
  }

  @Id
  @GeneratedValue
  @Convert(converter = UUIDConverter.class)
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
