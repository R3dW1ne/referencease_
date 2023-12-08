package com.ffhs.referencease.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import lombok.Data;

@Data
@Named
@SessionScoped
public class CreateRLetterController implements Serializable {

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

  public void onTabSet() {
    if (activeIndex < 4) {
      activeIndex++;
    }
  }

  public void onTabSet(int index) {
    activeIndex = index;
  }
}