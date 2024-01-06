package com.ffhs.referencease.beans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * Managed Bean Klasse für die Steuerung des Referenzschreibenerstellungsprozesses in der
 * Benutzeroberfläche. Diese Klasse verwaltet die Navigation zwischen verschiedenen Tabs des
 * Arbeitszeugnis-Erstellungsprozesses. Sie stellt Funktionen bereit, um vorwärts und rückwärts
 * zwischen den Tabs zu navigieren sowie einen bestimmten Tab direkt auszuwählen.
 */
@Data
@Named
@ViewScoped
public class CreateRLetterController implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private int activeIndex;

  /**
   * Initialisiert die Bean nach der Konstruktion. Setzt den aktiven Tab-Index auf 0.
   */
  @PostConstruct
  public void init() {
    activeIndex = 0;
  }

  /**
   * Navigiert zum vorherigen Tab, falls vorhanden.
   */
  public void onTabBack() {
    if (activeIndex > 0) {
      activeIndex--;
    }
  }

  /**
   * Navigiert zum nächsten Tab, falls vorhanden.
   */
  public void onTabFurther() {
    if (activeIndex < 4) {
      activeIndex++;
    }
  }

  /**
   * Navigiert zum Tab mit dem angegebenen Index.
   *
   * @param index Der Index des Tabs, zu dem navigiert werden soll.
   */
  public void onTabSet(int index) {
    activeIndex = index;
  }
}