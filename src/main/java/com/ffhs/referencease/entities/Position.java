package com.ffhs.referencease.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Position")
public class Position {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long positionId;

  @Column(unique = true)
  private String positionName;

//  @ManyToOne
//  @JoinColumn(name = "departmentId")
//  private Department department;

}
