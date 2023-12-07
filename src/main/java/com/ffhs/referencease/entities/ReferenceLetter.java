package com.ffhs.referencease.entities;

import com.ffhs.referencease.converters.UUIDConverter;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Data;


@Entity
@Data
@Table(name = "ReferenceLetter")
public class ReferenceLetter implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  @Convert(converter = UUIDConverter.class)
  private UUID referenceId;

  @Lob
  private String introduction;

  @Lob
  private String companyDescription;

  @Lob
  private String responsibilities;

  @Lob
  private String properties;

  @Lob
  private String conclusion;

  @ManyToOne
  @JoinColumn(name = "employeeId")
  private Employee employee;

  @ManyToOne
  @JoinColumn(name = "referenceReasonId")
  private ReferenceReason referenceReason;

  private LocalDate deliveryDate;
}
