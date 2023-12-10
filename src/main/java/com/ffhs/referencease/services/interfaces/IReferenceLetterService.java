package com.ffhs.referencease.services.interfaces;

import com.ffhs.referencease.entities.ReferenceLetter;
import java.util.List;
import java.util.UUID;

public interface IReferenceLetterService {

  ReferenceLetter getReferenceLetterById(UUID id);

  List<ReferenceLetter> getAllReferenceLetters();

  void saveReferenceLetter(ReferenceLetter referenceLetter);

  void updateReferenceLetter(ReferenceLetter referenceLetter);

  void deleteReferenceLetter(UUID id);

  String generateIntroduction(ReferenceLetter referenceLetter);

  Boolean checkReasonAndEmployeeSet(ReferenceLetter referenceLetter, Boolean needsEndDate);

  String setIntroductionButtonMessage(ReferenceLetter referenceLetter, Boolean needsEndDate);
}
