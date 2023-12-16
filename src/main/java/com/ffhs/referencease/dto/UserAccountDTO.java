package com.ffhs.referencease.dto;

import com.ffhs.referencease.valadators.annotations.ValidRegistrationUserDTO;
import com.ffhs.referencease.valadators.group_interfaces.RegistrationGroup;
import com.ffhs.referencease.valadators.group_interfaces.UpdateGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
import java.io.Serial;
import java.io.Serializable;
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
  @Email(message = "Bitte geben Sie eine gültige Email-Adresse ein. (@Email Validation)")
//  @Pattern(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
//      message = "Bitte geben Sie eine gültige Email-Adresse ein. (@Pattern Validation)")
  private String email;
//  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",
//      message = "Das Passwort muss mindestens 8 Zeichen lang sein und mindestens einen Grossbuchstaben, einen Kleinbuchstaben und eine Zahl enthalten. (@Pattern Validation)")
  private String password;
  private String confirmPassword;
  private UUID roleId;
  private UUID employeeId;
}
