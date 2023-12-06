package com.ffhs.referencease.dto;

import java.io.Serializable;
import lombok.Data;
import java.util.UUID;

@Data
public class GenderDTO implements Serializable {
  private UUID genderId;
  private String genderName;

  @Override
  public String toString() {
    return genderName;
  }
}
