package com.ffhs.referencease.entities;


import com.ffhs.referencease.converters.UUIDConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Company")
public class Company implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  @Convert(converter = UUIDConverter.class)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Lob
  private String description;

  @ManyToOne
  @JoinColumn(name = "referenceId")
  private ReferenceLetter referenceLetter;
}