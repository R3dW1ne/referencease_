package com.ffhs.referencease.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

@Data
@Named
@ViewScoped
public class CreateRLetterController implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private int activeIndex;

  @PostConstruct
  public void init() {
    activeIndex = 0;
  }

  public void onTabBack() {
    if (activeIndex > 0) {
      activeIndex--;
    }
  }

  public void onTabFurther() {
    if (activeIndex < 4) {
      activeIndex++;
    }
  }

  public void onTabSet(int index) {
    activeIndex = index;
  }
}