package com.ffhs.referencease.beans;

import com.ffhs.referencease.dto.EmployeeDTO;
import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.entities.ReferenceLetter;
import com.ffhs.referencease.entities.ReferenceReason;
import com.ffhs.referencease.services.ReferenceReasonService;
import com.ffhs.referencease.services.interfaces.IReferenceLetterService;
import com.ffhs.referencease.services.interfaces.IReferenceReasonService;
import jakarta.annotation.PostConstruct;
import jakarta.el.MethodExpression;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
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
      referenceLetter.setReferenceReason(selectedReferenceReason);
    }
  }

  public List<ReferenceLetter> getReferenceLetters() {
    return referenceLetterService.getAllReferenceLetters();
  }

  public void setRLetterEmployee(Employee employee) {
    referenceLetter.setEmployee(employee);
  }

  public void generateIntroduction() {
    referenceLetter.setIntroduction(referenceLetterService.generateIntroduction(referenceLetter));
  }
}

