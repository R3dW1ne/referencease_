package com.ffhs.referencease.entities.enums;

import lombok.Getter;

@Getter
public enum EReferenceReason {
  ABSCHLUSSZEUGNIS("Abschlusszeugnis"), ZWISCHENZEUGNIS("Zwischenzeugnis"), POSITIONSWECHSEL(
      "Positionswechsel");

  private final String displayName;

  EReferenceReason(String displayName) {
    this.displayName = displayName;
  }
}

