package com.ffhs.referencease.valadators.annotations;

import com.ffhs.referencease.valadators.ValidRegistrationUserDTOValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation zur Validierung eines UserAccountDTO-Objekts. Diese Annotation kann auf Klassen,
 * Felder, Methoden, Parameter, Konstruktoren, lokale Variablen und andere Annotationen angewendet
 * werden. Sie verwendet den ValidRegistrationUserDTOValidator, um die Gültigkeit eines
 * UserAccountDTO-Objekts zu überprüfen, insbesondere im Kontext der Benutzerregistrierung. Die
 * Validierung umfasst unter anderem die Überprüfung der Übereinstimmung von Passwort und
 * Passwortbestätigung.
 */
@Documented
@Constraint(validatedBy = ValidRegistrationUserDTOValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
    ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE,
    ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRegistrationUserDTO {

  /**
   * Standardnachricht, die zurückgegeben wird, wenn die Validierung fehlschlägt.
   *
   * @return Die Standardfehlermeldung.
   */
  String message() default "Das Benutzerobjekt ist nicht gültig";

  /**
   * Spezifiziert Validierungsgruppen, zu denen diese Beschränkung gehört.
   *
   * @return Die Validierungsgruppen.
   */
  Class<?>[] groups() default {};

  /**
   * Kann verwendet werden, um benutzerdefinierte Payload-Daten anzugeben, die bei der Validierung
   * berücksichtigt werden sollen.
   *
   * @return Die Payload-Klassen.
   */
  Class<? extends Payload>[] payload() default {};
}


