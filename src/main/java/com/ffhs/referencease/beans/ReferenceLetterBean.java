package com.ffhs.referencease.beans;

import com.ffhs.referencease.dto.EmployeeDTO;
import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.entities.ReferenceLetter;
import com.ffhs.referencease.entities.ReferenceReason;
import com.ffhs.referencease.services.interfaces.IEmployeeService;
import com.ffhs.referencease.services.interfaces.IReferenceLetterService;
import com.ffhs.referencease.services.interfaces.IReferenceReasonService;
import jakarta.annotation.PostConstruct;
import jakarta.el.MethodExpression;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;

@Named
@Setter
@Getter
@ViewScoped
public class ReferenceLetterBean implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
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
    referenceLetterService.updateReferenceLetter(referenceLetter);
    editMode = true;
    String message =
        referenceLetter.getReferenceReason().getName() + " von " + referenceLetter.getEmployee().getFirstName() + " wurde erfolgreich gespeichert.";
    FacesContext.getCurrentInstance().addMessage(null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
//    PrimeFaces.current().ajax()
//        .update("employeeListForm:messages", "employeeListForm:employeeTable");
  }

  public void resetReferenceLetter() {
    needsEndDate = false;
    editMode = false;
    selectedReferenceReason = null;
    referenceLetter = new ReferenceLetter();
  }

  public String newReferenceLetter() {
    resetReferenceLetter();
    return "/resources/components/sites/secured/stepsToReferenceLetter.xhtml?faces-redirect=true";
  }

  public String loadReferenceLetter(ReferenceLetter referenceLetter) {
    this.referenceLetter = referenceLetter;
    this.selectedReferenceReason = referenceLetter.getReferenceReason();
    return "/resources/components/sites/secured/stepsToReferenceLetter.xhtml?faces-redirect=true";
  }

  public void deleteReferenceLetter(ReferenceLetter referenceLetter) {
    referenceLetterService.deleteReferenceLetter(referenceLetter.getReferenceId());
  }

  public EmployeeDTO getEmployeeAsDTO() {
    return employeeService.convertToDTO(referenceLetter.getEmployee());
  }
}

