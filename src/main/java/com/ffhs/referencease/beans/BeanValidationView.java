package com.ffhs.referencease.beans;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Date;
import lombok.Data;

@Named
@RequestScoped
@Data
public class BeanValidationView {

  @Size(min = 2, max = 5)
  private String name;

  @Min(10)
  @Max(20)
  private Integer age;

  @DecimalMax(value = "99.9", message = "Shold not exceed 99.9")
  private Double amount;

  @Digits(integer = 3, fraction = 2)
  private Double amount2;

  @AssertTrue
  private boolean checked;

  @Past
  private Date pastDate;

  @Future
  private Date futureDate;

  @Pattern(regexp = "^[-+]?\\d+$")
  private String pattern;

  @NotNull
  private Boolean bool;

}
