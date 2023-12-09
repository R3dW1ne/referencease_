package com.ffhs.referencease.services;

import com.ffhs.referencease.dao.interfaces.IReferenceReasonDAO;
import com.ffhs.referencease.entities.ReferenceReason;
import com.ffhs.referencease.services.interfaces.IReferenceReasonService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;
import java.util.UUID;

@Stateless
public class ReferenceReasonService implements IReferenceReasonService {

  private final IReferenceReasonDAO referenceReasonDAO;
  @Inject
  public ReferenceReasonService(IReferenceReasonDAO referenceReasonDAO) {
    this.referenceReasonDAO = referenceReasonDAO;
  }
@Override
  public ReferenceReason getReferenceReasonById(UUID id) {
    return referenceReasonDAO.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("ReferenceReason mit ID " + id + " nicht gefunden"));
  }
  @Override
  public List<ReferenceReason> getAllReferenceReasons() {
    return referenceReasonDAO.findAll();
  }

  // Weitere Methoden nach Bedarf (z.B. speichern, aktualisieren, löschen)
}