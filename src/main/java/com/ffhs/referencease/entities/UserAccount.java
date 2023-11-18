package com.ffhs.referencease.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "UserAccount")
@NoArgsConstructor
public class UserAccount {

  public UserAccount(String email, String password) {
    this.email = email;
    this.password = password;
  }

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
