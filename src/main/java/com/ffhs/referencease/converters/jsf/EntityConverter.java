package com.ffhs.referencease.converters.jsf;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.WeakHashMap;

@FacesConverter(value = "entityConverter")
public class EntityConverter implements Converter {

  private static final Map<Object, String> entities = new WeakHashMap<>();

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
