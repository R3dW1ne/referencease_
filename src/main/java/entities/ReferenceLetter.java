package entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Data;

@Entity
@Data
@Table(name = "ReferenceLetter")
public class ReferenceLetter {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int referenceId;

  private LocalDate creationDate;
  private LocalDate lastModified;
  private String status;
  private String content;

  @ManyToOne
  @JoinColumn(name = "employeeId")
  private Employee employee;

  // Getter, Setter, hashCode, equals und toString Methoden
}
