package com.ffhs.referencease.entities;

import com.ffhs.referencease.converters.jpa.UUIDConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Data;

@Data
@Entity
public class TextType {

  @Id
  @GeneratedValue
  @Convert(converter = UUIDConverter.class)
  private UUID textTypeId;

  @Column(unique = true)
  private String textTypeName;
}
