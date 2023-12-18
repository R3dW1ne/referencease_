package com.ffhs.referencease.entities;

import com.ffhs.referencease.converters.jpa.UUIDConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "UserAccount")
@NoArgsConstructor
public class UserAccount implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  public UserAccount(String email, String password) {
    this.email = email;
    this.password = password;
  }

  @Id
  @GeneratedValue
  @Convert(converter = UUIDConverter.class)
  private UUID userId;

  @Column(unique = true)
  @Pattern(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
      message = "Bitte geben Sie eine gültige Email-Adresse ein. (@Pattern Validation)")
  private String email;

  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",
      message = "Das Passwort muss mindestens 8 Zeichen lang sein und mindestens einen Grossbuchstaben, einen Kleinbuchstaben und eine Zahl enthalten. (@Pattern Validation in UserAccount)")
  private String password;

  @ManyToMany
  @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "userId"),
      inverseJoinColumns = @JoinColumn(name = "roleId")
  )
  private Set<Role> roles;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "employeeId")
  private Employee employee;
}
