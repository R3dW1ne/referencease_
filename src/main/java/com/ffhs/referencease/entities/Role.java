package com.ffhs.referencease.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Role")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int roleId;

  @Column(unique = true)
  private String roleName;

  // Getter, Setter, hashCode, equals und toString Methoden
}
