package com.ffhs.referencease.entities;

import com.ffhs.referencease.converters.jpa.UUIDConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
@Entity
public class TextTemplate {


  @Id
  @GeneratedValue
  @Convert(converter = UUIDConverter.class)
  private UUID id;

  private String key; // Eindeutiger Schlüssel für die Vorlage, z.B. "birthDate", "startDate", "endDate", usw.
  private String template; // Die Textvorlage, z.B. ", geboren am ", ", war vom ", " bis ", usw.

  @ManyToMany
  @JoinTable(
      name = "TextTemplate_ReferenceReason",
      joinColumns = @JoinColumn(name = "text_template_id"),
      inverseJoinColumns = @JoinColumn(name = "reference_reason_id")
  )

  private List<ReferenceReason> referenceReasons; // Liste von ReferenceReasons

  @ManyToMany
  @JoinTable(
      name = "TextTemplate_Gender",
      joinColumns = @JoinColumn(name = "text_template_id"),
      inverseJoinColumns = @JoinColumn(name = "gender_id")
  )
  private List<Gender> genders; // Liste von Genders

  @ManyToOne
  @JoinColumn(name="textTypeId", nullable=false)
  private TextType textType; // Typ des Textes, z.B. "Einleitungstext" oder "Abschlusstext"
}
