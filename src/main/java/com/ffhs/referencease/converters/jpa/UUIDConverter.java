package com.ffhs.referencease.converters.jpa;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.UUID;

/**
 * JPA-Attributkonverter für die Umwandlung von UUID-Objekten in Strings und umgekehrt.
 * Dieser Konverter wird automatisch auf alle Entitätsattribute vom Typ UUID angewendet,
 * um eine korrekte Speicherung und Wiederherstellung von UUID-Werten in der Datenbank zu
 * ermöglichen.
 */
@Converter(autoApply = true)
public class UUIDConverter implements AttributeConverter<UUID, String> {

  /**
   * Konvertiert ein UUID-Objekt in einen String für die Speicherung in einer Datenbank.
   *
   * @param attribute Das UUID-Objekt, das konvertiert werden soll.
   * @return Der String-Wert des UUID-Objekts oder null, falls das UUID-Objekt null ist.
   */
  @Override
  public String convertToDatabaseColumn(UUID attribute) {
    return attribute == null ? null : attribute.toString();
  }

  /**
   * Konvertiert einen String aus der Datenbank zurück in ein UUID-Objekt.
   *
   * @param dbData Der String aus der Datenbank, der konvertiert werden soll.
   * @return Das UUID-Objekt oder null, falls der String null ist.
   */
  @Override
  public UUID convertToEntityAttribute(String dbData) {
    return dbData == null ? null : UUID.fromString(dbData);
  }
}
