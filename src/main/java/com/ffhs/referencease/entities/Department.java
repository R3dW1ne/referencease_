package com.ffhs.referencease.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Department")
public class Department {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int departmentId;

  @Column(unique = true)
  private String departmentName;

  // Getter, Setter, hashCode, equals und toString Methoden
}

