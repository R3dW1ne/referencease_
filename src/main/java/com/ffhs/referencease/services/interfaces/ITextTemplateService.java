package com.ffhs.referencease.services.interfaces;

import com.ffhs.referencease.entities.Gender;
import com.ffhs.referencease.entities.ReferenceReason;
import com.ffhs.referencease.entities.TextTemplate;
import com.ffhs.referencease.entities.TextType;
import java.util.List;
import java.util.UUID;

public interface ITextTemplateService {

  TextTemplate getTextTemplateById(UUID id);

  List<TextTemplate> getAllTextTemplates();

  void saveTextTemplate(TextTemplate textTemplate);

  void updateTextTemplate(TextTemplate textTemplate);

  void deleteTextTemplate(UUID id);

  List<TextTemplate> getTextTemplatesForReasonTypeAndGender(ReferenceReason reason,
      TextType textType, Gender gender);

  void createTextTemplateIfNotExists(String key, String value,
      List<ReferenceReason> associatedReferenceReasons, List<Gender> associatedGenders,
      TextType textType);

  void initializeTextTemplates();
}
