package com.ffhs.referencease.entities;

import com.ffhs.referencease.converters.UUIDConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class PropertyRating implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  @Convert(converter = UUIDConverter.class)
  private UUID propertyRatingId;

  @ManyToOne
  @JoinColumn(name = "employeeId")
  private Employee employee;

  @ManyToOne
  @JoinColumn(name = "propertyId")
  private Property property;

  @ManyToOne
  @JoinColumn(name="referenceId", nullable=false)
  private ReferenceLetter referenceLetter;

  @ManyToOne
  @JoinColumn(name = "scoreId")
  private Score score;

  // Getter und Setter
}
