package com.ffhs.referencease.dto;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RoleDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private UUID roleId;
  private String roleName;
}
