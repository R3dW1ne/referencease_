//package com.ffhs.referencease.valadators.annotations;
//
//import com.ffhs.referencease.valadators.CheckConfirmPasswordValidator;
//import jakarta.validation.Constraint;
//import jakarta.validation.Payload;
//import java.lang.annotation.Documented;
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
//@Documented
//@Constraint(validatedBy = CheckConfirmPasswordValidator.class)
//@Target({ElementType.TYPE_USE})
//@Retention(RetentionPolicy.RUNTIME)
//public @interface CheckConfirmPassword {
//  String message() default "Die Passwörter stimmt nicht überein";
//  Class<?>[] groups() default {};
//  Class<? extends Payload>[] payload() default {};
//}
//
//
