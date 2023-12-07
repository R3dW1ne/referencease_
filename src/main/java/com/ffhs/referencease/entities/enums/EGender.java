package com.ffhs.referencease.entities.enums;

import lombok.Getter;

@Getter
public enum EGender {
  MAENNLICH("Männlich"),
  WEIBLICH("Weiblich");

  private final String displayName;

  EGender(String displayName) {
    this.displayName = displayName;
  }
}
