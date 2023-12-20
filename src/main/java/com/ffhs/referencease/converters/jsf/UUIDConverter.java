package com.ffhs.referencease.converters.jsf;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.util.UUID;

@FacesConverter(value = "uuidConverter")
public class UUIDConverter implements Converter {

  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) {
    return value == null ? null : UUID.fromString(value);
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component, Object value) {
    return value == null ? null : value.toString();
  }
}
