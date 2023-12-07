package com.ffhs.referencease.dao.interfaces;

import com.ffhs.referencease.entities.ReferenceLetter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IReferenceLetterDAO {

  Optional<ReferenceLetter> findById(UUID id);

  List<ReferenceLetter> findAll();

  void create(ReferenceLetter referenceLetter);

  ReferenceLetter update(ReferenceLetter referenceLetter);

  void delete(ReferenceLetter referenceLetter);
}
