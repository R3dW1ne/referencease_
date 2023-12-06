package com.ffhs.referencease.entities;

import com.ffhs.referencease.converters.UUIDConverter;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;
import lombok.Data;

@Entity
@Data
@Table(name = "PerformanceRating")
public class PerformanceRating implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  @Convert(converter = UUIDConverter.class)
  private UUID ratingId;

  private String category;
  private int score;

  @Lob
  private String comment;

  @ManyToOne
  @JoinColumn(name = "referenceId")
  private ReferenceLetter referenceLetter;

  // Getter, Setter, hashCode, equals und toString Methoden
}
