package com.ffhs.referencease.dto;

import com.ffhs.referencease.entities.Department;
import com.ffhs.referencease.entities.Position;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class EmployeeDTO implements Serializable {

  private UUID employeeId;
  private String employeeNumber;
  private String firstName;
  private String lastName;
  private Date dateOfBirth;
  private String phone;
  private Date startDate;
  private Date endDate;
  private Position position;
  private Department department;
}
