package com.ffhs.referencease.dto;

import com.ffhs.referencease.entities.Department;
import com.ffhs.referencease.entities.Gender;
import com.ffhs.referencease.entities.Position;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private UUID employeeId;

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
