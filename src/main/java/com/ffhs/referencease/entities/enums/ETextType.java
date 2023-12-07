package com.ffhs.referencease.entities.enums;

import lombok.Getter;

@Getter
public enum ETextType {
  EINLEITUNG("Einleitung"),
  ABSCHLUSS("Abschluss");

  private final String displayName;

  ETextType(String displayName) {
    this.displayName = displayName;
  }
}

