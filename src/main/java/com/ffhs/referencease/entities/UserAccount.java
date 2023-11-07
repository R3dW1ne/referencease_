package com.ffhs.referencease.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "UserAccount")
public class UserAccount {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(unique = true)
  private String email;

  @Column(length = 60)
  private String password;

  @ManyToOne
  @JoinColumn(name = "roleId")
  private Role role;

  @OneToOne
  @JoinColumn(name = "employeeId")
  private Employee employee;
}
