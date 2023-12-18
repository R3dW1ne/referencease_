package com.ffhs.referencease.dao.interfaces;

import com.ffhs.referencease.entities.ReferenceReason;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IReferenceReasonDAO {

  Optional<ReferenceReason> findById(UUID id);

  List<ReferenceReason> findAll();

  Optional<ReferenceReason> findByReasonName(String reasonName);

  void save(ReferenceReason referenceReason);
}
