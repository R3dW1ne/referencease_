package com.ffhs.referencease.valadators;

import com.ffhs.referencease.dto.UserAccountDTO;
import com.ffhs.referencease.valadators.annotations.ValidRegistrationUserDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidRegistrationUserDTOValidator implements
    ConstraintValidator<ValidRegistrationUserDTO, UserAccountDTO> {

  @Override
  public void initialize(ValidRegistrationUserDTO constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(UserAccountDTO value, ConstraintValidatorContext context) {
    if (value == null) {
      return true; // Null-Werte werden von anderen Validatoren behandelt
    }
    return (value.getEmail() != null && value.getPassword() != null
        && value.getConfirmPassword() != null && value.getPassword()
        .equals(value.getConfirmPassword()));
  }
}
