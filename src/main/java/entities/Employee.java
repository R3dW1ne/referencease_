package entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Data;

@Entity
@Data
@Table(name = "Employee")
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int employeeId;

  @Column(unique = true)
  private String employeeNumber;

  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;
  private String position;
  private String department;
  private String email;
  private String phone;
  private LocalDate startDate;
  private LocalDate endDate;

  // Getter, Setter, hashCode, equals und toString Methoden
}
