package com.ffhs.referencease.entities;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "Employee")
public class Employee {

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

  private String email;
  private String phone;
  private LocalDate startDate;
  private LocalDate endDate;

  // Getter, Setter, hashCode, equals und toString Methoden
}