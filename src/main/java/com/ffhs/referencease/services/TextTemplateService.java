package com.ffhs.referencease.services;

import com.ffhs.referencease.dao.interfaces.ITextTemplateDAO;
import com.ffhs.referencease.entities.Gender;
import com.ffhs.referencease.entities.ReferenceReason;
import com.ffhs.referencease.entities.TextTemplate;
import com.ffhs.referencease.entities.TextType;
import com.ffhs.referencease.entities.enums.EReferenceReason;
import com.ffhs.referencease.entities.enums.ETextType;
import com.ffhs.referencease.services.interfaces.IGenderService;
import com.ffhs.referencease.services.interfaces.IReferenceReasonService;
import com.ffhs.referencease.services.interfaces.ITextTemplateService;
import com.ffhs.referencease.services.interfaces.ITextTypeService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
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

  private final IReferenceReasonService referenceReasonService;

  private final IGenderService genderService;

  private final ITextTypeService textTypeService;

  @Inject
  public TextTemplateService(ITextTemplateDAO textTemplateDAO,
      IReferenceReasonService referenceReasonService, IGenderService genderService,
      ITextTypeService textTypeService) {
    this.textTemplateDAO = textTemplateDAO;
    this.referenceReasonService = referenceReasonService;
    this.genderService = genderService;
    this.textTypeService = textTypeService;
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
    return textTemplateDAO.getTextTemplatesForReasonTypeAndGender(reason, textType, gender);
  }

  @Override
  public void createTextTemplateIfNotExists(String key, String value,
      List<ReferenceReason> associatedReferenceReasons, List<Gender> associatedGenders,
      TextType textType) {
    textTemplateDAO.createTextTemplateIfNotExists(key, value, associatedReferenceReasons,
                                                  associatedGenders, textType);
  }

  @Override
  public void initializeTextTemplates() {
    ReferenceReason abschlusszeugnis = referenceReasonService.getReferenceReasonByReasonName(
        EReferenceReason.ABSCHLUSSZEUGNIS.getDisplayName());
    ReferenceReason zwischenzeugnis = referenceReasonService.getReferenceReasonByReasonName(
        EReferenceReason.ZWISCHENZEUGNIS.getDisplayName());
    ReferenceReason positionswechsel = referenceReasonService.getReferenceReasonByReasonName(
        EReferenceReason.POSITIONSWECHSEL.getDisplayName());
    List<ReferenceReason> alleReferenceReasons = Arrays.asList(abschlusszeugnis, zwischenzeugnis,
                                                               positionswechsel);
    List<Gender> allGenders = genderService.getAllGenders();
    TextType textType = textTypeService.getTextTypeByName(ETextType.EINLEITUNG.getDisplayName());

    // Gemeinsame TextTemplates für alle ReferenceReasons
    createTextTemplateIfNotExists("afterName", ", geboren am ", alleReferenceReasons, allGenders,
                                  textType);
    createTextTemplateIfNotExists("afterPosition", " in der Abteilung ", alleReferenceReasons,
                                  allGenders, textType);
    createTextTemplateIfNotExists("afterDepartment", " in unserem Unternehmen beschäftigt.",
                                  alleReferenceReasons, allGenders, textType);

    // Spezifische TextTemplates für Abschlusszeugnis und Positionswechsel
    List<ReferenceReason> abschlussUndPositionswechsel = Arrays.asList(abschlusszeugnis,
                                                                       positionswechsel);
    createTextTemplateIfNotExists("afterDateOfBirth", ", war vom ", abschlussUndPositionswechsel,
                                  allGenders, textType);
    createTextTemplateIfNotExists("afterStartDate", " bis ", abschlussUndPositionswechsel,
                                  allGenders, textType);
    createTextTemplateIfNotExists("afterEndDate", ", als ", abschlussUndPositionswechsel,
                                  allGenders, textType);

    // Spezifisches TextTemplate für Zwischenzeugnis
    List<ReferenceReason> nurZwischenzeugnis = Collections.singletonList(zwischenzeugnis);
    createTextTemplateIfNotExists("afterDateOfBirth", ", ist seit ", nurZwischenzeugnis, allGenders,
                                  textType);
    createTextTemplateIfNotExists("afterStartDate", ", als ", nurZwischenzeugnis, allGenders,
                                  textType);
  }
}
