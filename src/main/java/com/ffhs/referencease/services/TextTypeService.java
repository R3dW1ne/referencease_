package com.ffhs.referencease.services;

import com.ffhs.referencease.dao.interfaces.ITextTypeDAO;
import com.ffhs.referencease.entities.TextType;
import com.ffhs.referencease.services.interfaces.ITextTypeService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;
import java.util.UUID;

@Stateless
public class TextTypeService implements ITextTypeService {


  private final ITextTypeDAO textTypeDAO;

  @Inject
  public TextTypeService(ITextTypeDAO textTypeDAO) {
    this.textTypeDAO = textTypeDAO;
  }

  @Override
  public TextType getTextTypeByName(String name) {
    return textTypeDAO.findByName(name).orElseThrow(
        () -> new IllegalArgumentException("TextType mit dem Namen " + name + " nicht gefunden"));
  }

  @Override
  public TextType getTextTypeById(UUID id) {
    return textTypeDAO.findById(id).orElseThrow(
        () -> new IllegalArgumentException("TextType mit ID " + id + " nicht gefunden"));
  }

  @Override
  public List<TextType> getAllTextTypes() {
    return textTypeDAO.findAll();
  }

  @Override
  public void createTextTypeIfNotExists(String displayName) {
    if (textTypeDAO.findByName(displayName).isEmpty()) {
      TextType textType = new TextType();
      textType.setTextTypeName(displayName);
      textTypeDAO.create(textType);
    }
  }

  // Weitere Methoden nach Bedarf (z.B. speichern, aktualisieren, löschen)
}
