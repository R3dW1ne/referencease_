package com.ffhs.referencease.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Entity
@Data
@Table(name = "UserAccount")
public class UserAccount {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(unique = true)
  @Email(message = "Invalid email format.")
  private String email;

  @Column(length = 60)
  private String password;

  @Transient // Diese Annotation sorgt dafür, dass das Feld nicht persistent gespeichert wird
  private String confirmPassword;

  @ManyToOne
  @JoinColumn(name = "roleId")
  private Role role;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "employeeId")
  private Employee employee;
}
