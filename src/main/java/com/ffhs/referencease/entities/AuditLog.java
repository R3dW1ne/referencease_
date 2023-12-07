//package com.ffhs.referencease.entities;
//
//import jakarta.persistence.*;
//import java.io.Serializable;
//import java.time.LocalDateTime;
//import java.util.UUID;
//import lombok.Data;
//
//@Entity
//@Data
//@Table(name = "AuditLog")
//public class AuditLog implements Serializable {
//
//  private static final long serialVersionUID = 1L;
//
//  @Id
//  @GeneratedValue(strategy = GenerationType.AUTO)
//  private UUID logId;
//
//  private String action;
//  private LocalDateTime timestamp;
//
//  @Lob
//  private String description;
//
//  @ManyToOne
//  @JoinColumn(name = "userId")
//  private UserAccount userAccount;
//
//  // Getter, Setter, hashCode, equals und toString Methoden
//}
