package com.ffhs.referencease.converters;

import com.ffhs.referencease.entities.Gender;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;


@FacesConverter(value = "ownEnumConverter")
public class OwnEnumConverter implements Converter {

  @Override
  public String getAsString(FacesContext context, UIComponent component, Object value) {
    if (value == null || value.toString().isEmpty()) {
      return "";
    }
    if (value instanceof Enum) {
      return ((Enum<?>) value).name();
    }
    throw new IllegalArgumentException("Object " + value + " is not an Enum.");
  }

  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) {
    if (value == null || value.isEmpty()) {
      return null;
    }

    Class<?> enumType = component.getValueExpression("value").getType(context.getELContext());
    if (!enumType.isEnum()) {
      return new Gender();
//      throw new IllegalArgumentException("Type is not an enum: " + enumType);
    }

    try {
      return Enum.valueOf((Class<Enum>) enumType, value);
    } catch (IllegalArgumentException e) {
      return null; // oder eine angemessene Fehlerbehandlung
    }
  }
}
