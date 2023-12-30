package com.ffhs.referencease.beans;

import com.ffhs.referencease.dto.EmployeeDTO;
import com.ffhs.referencease.entities.ReferenceLetter;
import com.ffhs.referencease.entities.ReferenceReason;
import com.ffhs.referencease.services.interfaces.IEmployeeService;
import com.ffhs.referencease.services.interfaces.IReferenceLetterService;
import com.ffhs.referencease.services.interfaces.IReferenceReasonService;
import com.ffhs.referencease.utils.FrontendMessages;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@Setter
@Getter
@SessionScoped
public class ReferenceLetterBean implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = LogManager.getLogger(ReferenceLetterBean.class);

  private final transient IReferenceLetterService referenceLetterService;
  private final transient IReferenceReasonService referenceReasonService;
  private final transient IEmployeeService employeeService;


  private Boolean needsEndDate;
  private Boolean allValuesSet;
  private String introductionButtonMessage;
  private ReferenceLetter referenceLetter;
  private List<ReferenceLetter> referenceLetters;
  private ReferenceReason selectedReferenceReason;
  private List<ReferenceReason> referenceReasons;
  private Boolean editMode;
  private LocalDate selectedDeliveryDate;

  @Inject
  public ReferenceLetterBean(IReferenceLetterService referenceLetterService,
      IReferenceReasonService referenceReasonService, IEmployeeService employeeService) {
    this.referenceLetterService = referenceLetterService;
    this.referenceReasonService = referenceReasonService;
    this.employeeService = employeeService;
  }

  @PostConstruct
  public void init() {
    referenceLetter = new ReferenceLetter();
    referenceReasons = referenceReasonService.getAllReferenceReasons();
    needsEndDate = false;
  }

  public void setEmployee(EmployeeDTO employeeDTO) {
    referenceLetter.setEmployee(employeeService.convertToEntity(employeeDTO));
  }

  public void updateSelectedReferenceReason() {
    if (selectedReferenceReason != null) {
      needsEndDate = !selectedReferenceReason.getReasonName().equals("Zwischenzeugnis");
      referenceLetter.setReferenceReason(selectedReferenceReason);
    }
  }

  public void updateSelectedDeliveryDate() {
    if (selectedDeliveryDate != null) {
      referenceLetter.setDeliveryDate(selectedDeliveryDate);
    }
  }

  public List<ReferenceLetter> getReferenceLetters() {
    return referenceLetterService.getAllReferenceLetters();
  }

  public void generateIntroduction() {
    if (Boolean.TRUE.equals(
        referenceLetterService.checkReasonAndEmployeeSet(referenceLetter, needsEndDate))) {
      referenceLetter.setIntroduction(referenceLetterService.generateIntroduction(referenceLetter));
    } else {
      FrontendMessages.sendErrorMessageToFrontend("referenceLetterForm:generateIntroductionButton",
                                                  referenceLetterService.setErrorMessage(
                                                      referenceLetter, needsEndDate), null);
    }
  }


  public void saveReferenceLetter() {
    String message;
    if (referenceLetter.getEmployee() == null || referenceLetter.getReferenceReason() == null) {
      message = "Bitte wählen Sie mindestens einen Mitarbeiter und einen Grund für das Arbeitszeugnis aus.";
      FrontendMessages.sendErrorMessageToFrontend(null, "Error", message);
    } else {
      referenceLetter = referenceLetterService.updateReferenceLetter(referenceLetter);
      editMode = true;
      message = referenceLetter.getReferenceReason().getReasonName() + " von "
          + referenceLetter.getEmployee().getFirstName() + " " + referenceLetter.getEmployee()
          .getLastName() + " wurde erfolgreich gespeichert.";
      LOGGER.info(message);
      FrontendMessages.sendInfoMessageToFrontend(null, "Info", message);
    }
  }

  public void resetReferenceLetter() {
    needsEndDate = false;
    editMode = false;
    selectedReferenceReason = null;
    referenceLetter = new ReferenceLetter();
    selectedDeliveryDate = null;
  }

  public String newReferenceLetter() {
    resetReferenceLetter();
    return "/resources/components/sites/secured/stepsToReferenceLetter.xhtml?faces-redirect=true";
  }

  public String loadReferenceLetter(ReferenceLetter referenceLetter) {
    this.referenceLetter = referenceLetter;
    this.selectedReferenceReason = referenceLetter.getReferenceReason();
    this.selectedDeliveryDate = referenceLetter.getDeliveryDate();
    needsEndDate = !selectedReferenceReason.getReasonName().equals("Zwischenzeugnis");
    return "/resources/components/sites/secured/stepsToReferenceLetter.xhtml?faces-redirect=true";
  }

  public void deleteReferenceLetter(ReferenceLetter referenceLetter) {
    referenceLetterService.deleteReferenceLetter(referenceLetter.getReferenceId());
  }

  public EmployeeDTO getEmployeeAsDTO() {
    return employeeService.convertToDTO(referenceLetter.getEmployee());
  }
}

