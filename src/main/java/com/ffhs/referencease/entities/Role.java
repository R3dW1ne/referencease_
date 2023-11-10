package com.ffhs.referencease.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Role")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int roleId;

  @Column(unique = true)
  private String roleName;

  public Role(String roleName) {
    this.roleName = roleName;
  }
}
