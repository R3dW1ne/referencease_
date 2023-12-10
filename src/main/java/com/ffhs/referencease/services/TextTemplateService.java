package com.ffhs.referencease.services;

import com.ffhs.referencease.dao.interfaces.ITextTemplateDAO;
import com.ffhs.referencease.entities.Gender;
import com.ffhs.referencease.entities.ReferenceReason;
import com.ffhs.referencease.entities.TextTemplate;
import com.ffhs.referencease.entities.TextType;
import com.ffhs.referencease.services.interfaces.ITextTemplateService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing TextTemplate entities.
 */
@Stateless
public class TextTemplateService implements ITextTemplateService {


  private final ITextTemplateDAO textTemplateDAO;
  @Inject
  public TextTemplateService(ITextTemplateDAO textTemplateDAO) {
    this.textTemplateDAO = textTemplateDAO;
  }

  @Override
  public TextTemplate getTextTemplateById(UUID id) {
    Optional<TextTemplate> textTemplate = textTemplateDAO.findById(id);
    if (textTemplate.isEmpty()) {
      throw new NoSuchElementException("TextTemplate not found with id: " + id);
    }
    return textTemplate.get();
  }

  @Override
  public List<TextTemplate> getAllTextTemplates() {
    return textTemplateDAO.findAll();
  }

  @Override
  public void saveTextTemplate(TextTemplate textTemplate) {
    textTemplateDAO.create(textTemplate);
  }

  @Override
  public void updateTextTemplate(TextTemplate textTemplate) {
    textTemplateDAO.update(textTemplate);
  }

  @Override
  public void deleteTextTemplate(UUID id) {
    TextTemplate textTemplate = getTextTemplateById(id); // Throws exception if not found
    textTemplateDAO.delete(textTemplate);
  }

  @Override
  public List<TextTemplate> getTextTemplatesForReasonTypeAndGender(ReferenceReason reason,
      TextType textType, Gender gender) {
    return textTemplateDAO.getTextTemplatesForReasonTypeAndGender(reason,textType,gender);
  }
}
