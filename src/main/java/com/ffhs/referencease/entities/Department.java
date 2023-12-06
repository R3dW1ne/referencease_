package com.ffhs.referencease.entities;

import com.ffhs.referencease.converters.UUIDConverter;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;
import lombok.Data;

@Entity
@Data
@Table(name = "Department")
public class Department implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
//  @GeneratedValue(strategy = GenerationType.AUTO)
  @GeneratedValue
  @Convert(converter = UUIDConverter.class)
  private UUID departmentId;

  @Column(unique = true)
  private String departmentName;

  @Override
  public String toString() {
    return departmentName;
  }
}

