package com.ffhs.referencease.services.interfaces;

import com.ffhs.referencease.entities.ReferenceReason;
import java.util.List;
import java.util.UUID;

public interface IReferenceReasonService {

  ReferenceReason getReferenceReasonById(UUID id);

  ReferenceReason getReferenceReasonByReasonName(String name);

  List<ReferenceReason> getAllReferenceReasons();

  void createReferenceReasonIfNotExists(String displayName);
}
