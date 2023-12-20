package com.ffhs.referencease.valadators.annotations;

import com.ffhs.referencease.valadators.ValidRegistrationUserDTOValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ValidRegistrationUserDTOValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
    ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE,
    ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRegistrationUserDTO {

  String message() default "Das Benutzerobjekt ist nicht gültig";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}


