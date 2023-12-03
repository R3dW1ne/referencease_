package com.ffhs.referencease.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;

@Entity
@Data
@Table(name = "Department")
public class Department implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long departmentId;

  @Column(unique = true)
  private String departmentName;
}

