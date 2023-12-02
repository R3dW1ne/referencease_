package com.ffhs.referencease.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "Employee")
public class Employee implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long employeeId;

  @Column(unique = true)
  private String employeeNumber;

  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;

  @ManyToOne
  @JoinColumn(name = "positionId")
  private Position position;

  @ManyToOne
  @JoinColumn(name = "departmentId")
  private Department department;

  private String phone;
  private LocalDate startDate;
  private LocalDate endDate;

  // Getter, Setter, hashCode, equals und toString Methoden
}
