package com.ffhs.referencease.services.interfaces;

import com.ffhs.referencease.entities.TextType;
import java.util.List;
import java.util.UUID;

public interface ITextTypeService {

  TextType getTextTypeByName(String name);

  TextType getTextTypeById(UUID id);

  List<TextType> getAllTextTypes();

  void createTextTypeIfNotExists(String displayName);
}
