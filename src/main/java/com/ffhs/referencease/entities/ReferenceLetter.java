package com.ffhs.referencease.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Data;

@Entity
@Data
@Table(name = "ReferenceLetter")
public class ReferenceLetter {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int referenceId;

  private LocalDate creationDate;
  private LocalDate lastModified;
  private String status;

  @Lob
  private String content;

  @ManyToOne
  @JoinColumn(name = "employeeId")
  private Employee employee;

  // Getter, Setter, hashCode, equals und toString Methoden
}
