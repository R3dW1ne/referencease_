package com.ffhs.referencease.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Data
@Table(name = "AuditLog")
public class AuditLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int logId;

  private String action;
  private LocalDateTime timestamp;

  @Lob
  private String description;

  @ManyToOne
  @JoinColumn(name = "userId")
  private UserAccount userAccount;

  // Getter, Setter, hashCode, equals und toString Methoden
}
