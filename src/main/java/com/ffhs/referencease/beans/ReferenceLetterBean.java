package com.ffhs.referencease.beans;

import com.ffhs.referencease.dto.EmployeeDTO;
import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.entities.ReferenceLetter;
import com.ffhs.referencease.entities.ReferenceReason;
import com.ffhs.referencease.services.interfaces.IReferenceLetterService;
import com.ffhs.referencease.services.interfaces.IReferenceReasonService;
import jakarta.annotation.PostConstruct;
import jakarta.el.MethodExpression;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Named
@SessionScoped
public class ReferenceLetterBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private final transient IReferenceLetterService referenceLetterService;

  private final transient IReferenceReasonService referenceReasonService;

  @Setter
  @Getter
  private Boolean needsEndDate;

  @Setter
  @Getter
  private Boolean allValuesSet;

  @Setter
  @Getter
  private String introductionButtonMessage;

  @Setter
  @Getter
  private ReferenceLetter referenceLetter;

  @Setter
  @Getter
  private ReferenceReason selectedReferenceReason;

  @Setter
  @Getter
  private List<ReferenceReason> referenceReasons;

  @Inject
  public ReferenceLetterBean(IReferenceLetterService referenceLetterService,
      IReferenceReasonService referenceReasonService) {
    this.referenceLetterService = referenceLetterService;
    this.referenceReasonService = referenceReasonService;
  }

  @PostConstruct
  public void init() {
    referenceLetter = new ReferenceLetter();
    referenceReasons = referenceReasonService.getAllReferenceReasons();
    needsEndDate = false;
  }

  public void setEmployee(EmployeeDTO employeeDTO) {
    Employee employee = new Employee(employeeDTO);
    referenceLetter.setEmployee(employee);
  }

  public void setReferenceReason(ReferenceReason referenceReason) {
    referenceLetter.setReferenceReason(referenceReason);
  }

  public void updateSelectedReferenceReason() {
    if (selectedReferenceReason != null) {
      needsEndDate = !selectedReferenceReason.getName().equals("Zwischenzeugnis");
      referenceLetter.setReferenceReason(selectedReferenceReason);
    }
  }

  public void setEndDate(String endDate) {
    referenceLetter.setEndDate(LocalDate.parse(endDate));
  }

  public List<ReferenceLetter> getReferenceLetters() {
    return referenceLetterService.getAllReferenceLetters();
  }

  public void setRLetterEmployee(Employee employee) {
    referenceLetter.setEmployee(employee);
  }

//  public Boolean checkAllValuesSet() {
//    allValuesSet = referenceLetterService.checkReasonAndEmployeeSet(referenceLetter, needsEndDate);
//    introductionButtonMessage = referenceLetterService.setIntroductionButtonMessage(referenceLetter, needsEndDate);
//    return allValuesSet;
//  }

  public void generateIntroduction() {
    if (Boolean.TRUE.equals(referenceLetterService.checkReasonAndEmployeeSet(referenceLetter, needsEndDate))) {
      referenceLetter.setIntroduction(referenceLetterService.generateIntroduction(referenceLetter));
    } else {
      FacesContext.getCurrentInstance().addMessage("referenceLetterForm:generateIntroductionButton",
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              referenceLetterService.setIntroductionButtonMessage(referenceLetter, needsEndDate)));
    }
  }

  public void saveReferenceLetter() {
    referenceLetterService.saveReferenceLetter(referenceLetter);
  }

  public void resetReferenceLetter() {
    needsEndDate = false;
    selectedReferenceReason = null;
    referenceLetter = new ReferenceLetter();
  }

  public String newReferenceLetter() {
    resetReferenceLetter();
    return "/resources/components/sites/secured/stepsToReferenceLetter.xhtml?faces-redirect=true";
  }

  public void loadReferenceLetter(ReferenceLetter referenceLetter) {
    this.referenceLetter = referenceLetter;
  }
}

