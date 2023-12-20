package com.ffhs.referencease.entities.enums;

public enum EScore {
  ONE("1", "schlecht"), TWO("2", "befriedigend"), THREE("3", "gut"), FOUR("4", "sehr gut"), FIVE(
      "5", "hervorragend");

  private final String value;
  private final String displayName;

  EScore(String value, String displayName) {
    this.value = value;
    this.displayName = displayName;
  }

  public String getValue() {
    return value;
  }

  public String getDisplayName() {
    return displayName;
  }
}

