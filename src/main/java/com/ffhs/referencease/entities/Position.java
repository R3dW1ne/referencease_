package com.ffhs.referencease.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;

@Entity
@Data
@Table(name = "Position")
public class Position implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long positionId;

  @Column(unique = true)
  private String positionName;

//  @ManyToOne
//  @JoinColumn(name = "departmentId")
//  private Department department;

  @Override
  public String toString() {
    return positionName;
  }
}
