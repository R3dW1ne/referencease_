package com.ffhs.referencease.entities;

import com.ffhs.referencease.converters.jpa.UUIDConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Data;


@Entity
@Data
@Table(name = "ReferenceLetter")
public class ReferenceLetter implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private String employeeName;
  private String employeePosition;
  private String employeeDepartment;

  @Id
  @GeneratedValue
  @Convert(converter = UUIDConverter.class)
  private UUID referenceId;
  private LocalDate endDate;
  private LocalDate deliveryDate;
  private LocalDate creationDate;

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
}
