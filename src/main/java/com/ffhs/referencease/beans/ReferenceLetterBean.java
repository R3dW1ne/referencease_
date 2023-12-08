package com.ffhs.referencease.beans;

import com.ffhs.referencease.dto.EmployeeDTO;
import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.entities.ReferenceLetter;
import com.ffhs.referencease.services.interfaces.IReferenceLetterService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Named
@RequestScoped
public class ReferenceLetterBean {

  private final IReferenceLetterService referenceLetterService;

  @Setter
  @Getter
  private ReferenceLetter referenceLetter;

  @Inject
  public ReferenceLetterBean(IReferenceLetterService referenceLetterService) {
    this.referenceLetterService = referenceLetterService;
  }

  public void setEmployee(EmployeeDTO employeeDTO) {
    Employee employee = new Employee(employeeDTO);
    referenceLetter.setEmployee(employee);
  }

  public List<ReferenceLetter> getReferenceLetters() {
    return referenceLetterService.getAllReferenceLetters();
  }

  public void setRLetterEmployee(Employee employee) {
    referenceLetter.setEmployee(employee);
  }
}

