package com.ffhs.referencease.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;

@Entity
@Data
@Table(name = "RatingTemplate")
public class RatingTemplate implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int templateId;

  private String category;
  private int score;
  private String language;

  @Lob
  private String text;

  // Getter, Setter, hashCode, equals und toString Methoden
}
