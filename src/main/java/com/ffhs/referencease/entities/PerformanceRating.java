package com.ffhs.referencease.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;

@Entity
@Data
@Table(name = "PerformanceRating")
public class PerformanceRating implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int ratingId;

  private String category;
  private int score;

  @Lob
  private String comment;

  @ManyToOne
  @JoinColumn(name = "referenceId")
  private ReferenceLetter referenceLetter;

  // Getter, Setter, hashCode, equals und toString Methoden
}
