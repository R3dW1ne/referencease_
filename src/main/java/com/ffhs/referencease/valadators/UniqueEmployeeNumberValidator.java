package com.ffhs.referencease.valadators;

import com.ffhs.referencease.annotations.UniqueEmployeeNumber;
import com.ffhs.referencease.services.interfaces.IEmployeeService;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueEmployeeNumberValidator implements ConstraintValidator<UniqueEmployeeNumber, String> {


  private final IEmployeeService employeeService;
  @Inject
  public UniqueEmployeeNumberValidator(IEmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @Override
  public boolean isValid(String employeeNumber, ConstraintValidatorContext context) {
    if (employeeNumber == null) {
      return true; // Null-Werte werden von @NotBlank behandelt
    }
    return employeeService.existsByEmployeeNumber(employeeNumber);
  }
}
