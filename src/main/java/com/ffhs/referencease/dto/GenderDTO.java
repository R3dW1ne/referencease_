package com.ffhs.referencease.dto;

import java.io.Serializable;
import lombok.Data;
import java.util.UUID;

@Data
public class GenderDTO implements Serializable {

  private static final long serialVersionUID = 1L;
  private UUID genderId;
  private String genderName;

  @Override
  public String toString() {
    return genderName;
  }
}
