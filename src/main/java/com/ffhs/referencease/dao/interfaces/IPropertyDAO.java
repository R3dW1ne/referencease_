package com.ffhs.referencease.dao.interfaces;

import com.ffhs.referencease.entities.Property;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IPropertyDAO {
  Optional<Property> findById(UUID id);
  Optional<Property> findByName(String name);
  List<Property> findAll();
  void create(Property property);
  // Weitere Methoden nach Bedarf
}
