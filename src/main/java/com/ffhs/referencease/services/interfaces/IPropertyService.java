package com.ffhs.referencease.services.interfaces;

import com.ffhs.referencease.entities.Property;
import java.util.List;
import java.util.UUID;

public interface IPropertyService {
  Property getPropertyByName(String name);
  Property getPropertyById(UUID id);
  List<Property> getAllProperties();
  void createPropertyIfNotExists(String displayName);
  // Weitere Methoden nach Bedarf
}
