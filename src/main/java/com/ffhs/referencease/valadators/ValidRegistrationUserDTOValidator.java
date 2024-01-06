package com.ffhs.referencease.valadators;

import com.ffhs.referencease.dto.UserAccountDTO;
import com.ffhs.referencease.valadators.annotations.ValidRegistrationUserDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator-Klasse zur Überprüfung der Gültigkeit eines UserAccountDTO-Objekts im Kontext der
 * Benutzerregistrierung. Diese Klasse implementiert das ConstraintValidator-Interface und definiert
 * die Logik zur Überprüfung der Konsistenz und Vollständigkeit der Benutzerregistrierungsdaten,
 * speziell die Übereinstimmung von Passwort und Passwortbestätigung.
 */
public class ValidRegistrationUserDTOValidator implements
    ConstraintValidator<ValidRegistrationUserDTO, UserAccountDTO> {

  /**
   * Initialisiert den Validator. Diese Methode wird vom Validierungsframework aufgerufen.
   *
   * @param constraintAnnotation Die Annotation, die den Validator definiert.
   */
  @Override
  public void initialize(ValidRegistrationUserDTO constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  /**
   * Überprüft die Gültigkeit des übergebenen UserAccountDTO-Objekts.
   *
   * @param value   Das zu validierende UserAccountDTO-Objekt.
   * @param context Kontext, in dem die Überprüfung stattfindet.
   * @return true, wenn das UserAccountDTO-Objekt gültig ist, sonst false.
   */
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
