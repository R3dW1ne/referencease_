package com.ffhs.referencease.annotations;

import com.ffhs.referencease.valadators.UniqueEmployeeNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = UniqueEmployeeNumberValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmployeeNumber {
  String message() default "Der Wert ist nicht einzigartig";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
