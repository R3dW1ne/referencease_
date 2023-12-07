package com.ffhs.referencease.dao.interfaces;

import com.ffhs.referencease.entities.ReferenceReason;
import com.ffhs.referencease.entities.TextTemplate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITextTemplateDAO {

  Optional<TextTemplate> findById(UUID id);

  List<TextTemplate> findAll();

  void create(TextTemplate textTemplate);

  TextTemplate update(TextTemplate textTemplate);

  void delete(TextTemplate textTemplate);

  List<TextTemplate> getTextTemplatesForReason(ReferenceReason reason);
}
