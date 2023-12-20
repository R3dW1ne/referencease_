package com.ffhs.referencease.entities.enums;

import lombok.Getter;

@Getter
public enum ERole {
  ROLE_USER("User"), ROLE_MODERATOR("Moderator"), ROLE_ADMIN("Admin");

  private final String displayName;

  ERole(String displayName) {
    this.displayName = displayName;
  }
}
