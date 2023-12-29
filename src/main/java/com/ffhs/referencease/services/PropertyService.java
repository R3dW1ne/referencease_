package com.ffhs.referencease.services;

import com.ffhs.referencease.dao.interfaces.IPropertyDAO;
import com.ffhs.referencease.entities.Property;
import com.ffhs.referencease.services.interfaces.IPropertyService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;
import java.util.UUID;

@Stateless
public class PropertyService implements IPropertyService {

  private final IPropertyDAO propertyDAO;

  @Inject
  public PropertyService(IPropertyDAO propertyDAO) {
    this.propertyDAO = propertyDAO;
  }

  @Override
  public Property getPropertyByName(String name) {
    return propertyDAO.findByName(name).orElseThrow(
        () -> new IllegalArgumentException("Property mit dem Namen " + name + " nicht gefunden"));
  }

  @Override
  public Property getPropertyById(UUID id) {
    return propertyDAO.findById(id).orElseThrow(
        () -> new IllegalArgumentException("Property mit ID " + id + " nicht gefunden"));
  }

  @Override
  public List<Property> getAllProperties() {
    return propertyDAO.findAll();
  }

  @Override
  public void createPropertyIfNotExists(String displayName) {
    if (propertyDAO.findByName(displayName).isEmpty()) {
      Property property = new Property();
      property.setPropertyName(displayName);
      propertyDAO.create(property);
    }
  }
}
