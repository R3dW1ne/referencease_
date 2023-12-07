package com.ffhs.referencease.services.interfaces;

import com.ffhs.referencease.entities.ReferenceReason;
import com.ffhs.referencease.entities.TextTemplate;
import java.util.List;
import java.util.UUID;

public interface ITextTemplateService {

  TextTemplate getTextTemplateById(UUID id);

  List<TextTemplate> getAllTextTemplates();

  void saveTextTemplate(TextTemplate textTemplate);

  void updateTextTemplate(TextTemplate textTemplate);

  void deleteTextTemplate(UUID id);

  List<TextTemplate> getTextTemplatesForReason(ReferenceReason reason);
}
