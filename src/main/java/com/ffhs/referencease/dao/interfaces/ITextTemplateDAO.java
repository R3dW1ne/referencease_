package com.ffhs.referencease.dao.interfaces;

import com.ffhs.referencease.entities.Gender;
import com.ffhs.referencease.entities.ReferenceReason;
import com.ffhs.referencease.entities.TextTemplate;
import com.ffhs.referencease.entities.TextType;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITextTemplateDAO {

  Optional<TextTemplate> findById(UUID id);

  List<TextTemplate> findAll();

  void create(TextTemplate textTemplate);

  TextTemplate update(TextTemplate textTemplate);

  void delete(TextTemplate textTemplate);

  List<TextTemplate> getTextTemplatesForReasonTypeAndGender(ReferenceReason reason,
      TextType textType, Gender gender);

  @Transactional
  void createTextTemplateIfNotExists(String key, String template,
      List<ReferenceReason> referenceReasons, List<Gender> genders, TextType textType);
}
