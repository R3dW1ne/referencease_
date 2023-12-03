package com.ffhs.referencease.dto;

import java.io.Serializable;
import java.util.UUID;
import lombok.Data;

@Data
public class UserAccountDTO implements Serializable {

  private UUID userId;
  private String email;
  private String password;
  private String confirmPassword;
  private UUID roleId;
  private UUID employeeId;

  // Standardkonstruktor, Getter und Setter
}
