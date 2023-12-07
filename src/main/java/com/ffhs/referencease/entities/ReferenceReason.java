package com.ffhs.referencease.entities;

import com.ffhs.referencease.converters.UUIDConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.UUID;
import lombok.Data;

@Data
@Entity
public class ReferenceReason implements Serializable {


  @Id
  @GeneratedValue
  @Convert(converter = UUIDConverter.class)
  private UUID referenceReasonId;

  private String name;
}
