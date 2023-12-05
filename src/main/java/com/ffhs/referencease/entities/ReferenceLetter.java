package com.ffhs.referencease.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Data;

@Entity
@Data
@Table(name = "ReferenceLetter")
public class ReferenceLetter implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID referenceId;

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
