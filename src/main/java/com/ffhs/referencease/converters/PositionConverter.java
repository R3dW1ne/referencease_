package com.ffhs.referencease.converters;

import com.ffhs.referencease.entities.Position;
import com.ffhs.referencease.services.service_interfaces.IPositionService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

//@FacesConverter(forClass = Position.class)
@FacesConverter(value = "positionConverter", managed = true)
public class PositionConverter implements Converter<Position> {

  @Inject
  private IPositionService positionService;

  @Override
  public Position getAsObject(FacesContext context, UIComponent component, String value) {
    if (value == null || value.isEmpty()) {
      return null;
    }
    return positionService.getPositionById(Long.parseLong(value)).orElse(null);
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component, Position value) {
    if (value == null) {
      return "";
    }
    return String.valueOf(value.getPositionId());
  }
}

