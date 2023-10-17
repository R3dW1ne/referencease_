package entities;

import jakarta.persistence.Column;
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
@Table(name = "UserAccount")
public class UserAccount {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int userId;

  @Column(unique = true)
  private String username;

  private String password;
  private String role;

  @ManyToOne
  @JoinColumn(name = "employeeId")
  private Employee employee;

  // Getter, Setter, hashCode, equals und toString Methoden
}