package com.ffhs.referencease.converters.jsf;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.WeakHashMap;

/**
 * JSF-Konverter für die Umwandlung von Entitätsobjekten in String-Werte und umgekehrt.
 * Dieser Konverter verwendet UUIDs, um Entitäten eindeutig zu identifizieren und ermöglicht
 * es, diese Objekte in JSF-Komponenten korrekt zu referenzieren und wiederherzustellen.
 * Er nutzt eine WeakHashMap, um eine Zuordnung zwischen den Entitätsobjekten und ihren
 * UUIDs zu verwalten.
 */
@FacesConverter(value = "entityConverter")
public class EntityConverter implements Converter {

  private static final Map<Object, String> entities = new WeakHashMap<>();

  /**
   * Konvertiert eine Entität in einen String (UUID), der die Entität eindeutig identifiziert.
   *
   * @param context Der FacesContext.
   * @param component Die UI-Komponente, die diesen Konverter verwendet.
   * @param entity Das Entitätsobjekt, das konvertiert werden soll.
   * @return Eine eindeutige UUID als String, die die Entität repräsentiert.
   */
  @Override
  public String getAsString(FacesContext context, UIComponent component, Object entity) {
    synchronized (entities) {
      if (!entities.containsKey(entity)) {
        String uuid = UUID.randomUUID().toString();
        entities.put(entity, uuid);
        return uuid;
      } else {
        return entities.get(entity);
      }
    }
  }

  /**
   * Konvertiert einen String (UUID) zurück in das entsprechende Entitätsobjekt.
   *
   * @param context Der FacesContext.
   * @param component Die UI-Komponente, die diesen Konverter verwendet.
   * @param uuid Der String (UUID), der das Entitätsobjekt repräsentiert.
   * @return Das Entitätsobjekt, das der UUID entspricht, oder null, wenn keine Übereinstimmung gefunden wird.
   */
  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String uuid) {
    for (Entry<Object, String> entry : entities.entrySet()) {
      if (entry.getValue().equals(uuid)) {
        return entry.getKey();
      }
    }
    return null;
  }
}
