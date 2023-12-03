package com.ffhs.referencease.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Role")
public class Role implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int roleId;

  @Column(unique = true)
  private String roleName;

  public Role(String roleName) {
    this.roleName = roleName;
  }
}
