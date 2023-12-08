package com.ffhs.referencease.entities;

import com.ffhs.referencease.converters.jpa.UUIDConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.UUID;
import lombok.Data;

@Entity
//@NoArgsConstructor
//@AllArgsConstructor
@Data
@Table(name = "Gender")
public class Gender implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  @Convert(converter = UUIDConverter.class)
  private UUID genderId;

  @Column(unique = true)
  private String genderName;
}
