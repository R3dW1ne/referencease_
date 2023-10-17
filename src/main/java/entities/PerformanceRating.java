package entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "PerformanceRating")
public class PerformanceRating {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int ratingId;

  private String category;
  private int score;
  private String comment;

  @ManyToOne
  @JoinColumn(name = "referenceId")
  private ReferenceLetter referenceLetter;

  // Getter, Setter, hashCode, equals und toString Methoden
}
