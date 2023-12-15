package com.ffhs.referencease.valadators;

import com.ffhs.referencease.annotations.UniqueEmail;
import com.ffhs.referencease.services.interfaces.IUserAccountService;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

  private final IUserAccountService userAccountService;
  @Inject
  public UniqueEmailValidator(IUserAccountService userAccountService) {
    this.userAccountService = userAccountService;
  }

  @Override
  public boolean isValid(String email, ConstraintValidatorContext context) {
    if (email == null) {
      return true; // Null-Werte werden von @NotBlank behandelt
    }
    return userAccountService.emailExists(email);
  }
}
