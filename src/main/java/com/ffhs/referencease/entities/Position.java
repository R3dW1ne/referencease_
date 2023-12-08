package com.ffhs.referencease.entities;

import com.ffhs.referencease.converters.jpa.UUIDConverter;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;
import lombok.Data;

@Entity
@Data
@Table(name = "Position")
public class Position implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
//  @GeneratedValue(strategy = GenerationType.AUTO)
  @GeneratedValue
  @Convert(converter = UUIDConverter.class)
  private UUID positionId;

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
