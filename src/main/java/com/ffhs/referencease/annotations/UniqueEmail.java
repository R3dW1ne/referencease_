package com.ffhs.referencease.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.ffhs.referencease.valadators.UniqueEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface UniqueEmail {
  String message() default "Email ist bereits vergeben";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

