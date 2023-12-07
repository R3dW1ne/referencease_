package com.ffhs.referencease.entities.enums;

import lombok.Getter;

@Getter
public enum EProperty {
  AUFFASSUNGSGABE("Auffassungsgabe"),
  FACHKOMPETENZ("Fachkompetenz"),
  LEISTUNGSBEREITSCHAFT("Leistungsbereitschaft"),
  BELASTBARKEIT("Belastbarkeit");

  private final String displayName;

  EProperty(String displayName) {
    this.displayName = displayName;
  }
}

