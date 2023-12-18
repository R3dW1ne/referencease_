package com.ffhs.referencease.entities;

import com.ffhs.referencease.converters.jpa.UUIDConverter;
import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Data;
import org.hibernate.annotations.Cascade;


@Entity
@Data
@Table(name = "ReferenceLetter")
public class ReferenceLetter implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  // Zusätzliche Felder für Mitarbeiterinformationen
  private String employeeName;
  private String employeePosition;
  private String employeeDepartment;

  @Id
  @GeneratedValue
  @Convert(converter = UUIDConverter.class)
  private UUID referenceId;
  private LocalDate endDate;
  private LocalDate deliveryDate;

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
