package com.ffhs.referencease.dto;

import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.entities.Role;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserAccountDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private UUID userId;
  @Pattern(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
      message = "Bitte geben Sie eine gültige Email-Adresse ein. (@Pattern Validation)")
  private String email;
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",
      message = "Das Passwort muss mindestens 8 Zeichen lang sein und mindestens einen Grossbuchstaben, einen Kleinbuchstaben und eine Zahl enthalten. (@Pattern Validation in UserAccountDTO)")
  private String password;
  private String confirmPassword;
  private Set<Role> roles;
  private Employee employee;
}
