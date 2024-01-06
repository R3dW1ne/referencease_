package com.ffhs.referencease.beans;

import com.ffhs.referencease.dto.EmployeeDTO;
import com.ffhs.referencease.entities.ReferenceLetter;
import com.ffhs.referencease.entities.ReferenceReason;
import com.ffhs.referencease.services.interfaces.IEmployeeService;
import com.ffhs.referencease.services.interfaces.IReferenceLetterService;
import com.ffhs.referencease.services.interfaces.IReferenceReasonService;
import com.ffhs.referencease.utils.FrontendMessages;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Bean-Klasse zur Verwaltung von Arbeitszeugnisse. Diese Klasse dient als Managed Bean für die
 * JSF-Oberfläche, um CRUD-Operationen für Arbeitszeugnisse durchzuführen und Arbeitszeugnisse für
 * die Benutzeroberfläche bereitzustellen. Sie verwendet verschiedene Service-Klassen, um
 * Geschäftslogik auf Arbeitszeugnisse anzuwenden.
 */
@Named
@Setter
@Getter
@SessionScoped
public class ReferenceLetterBean implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = LogManager.getLogger(ReferenceLetterBean.class);

  private final transient IReferenceLetterService referenceLetterService;
  private final transient IReferenceReasonService referenceReasonService;
  private final transient IEmployeeService employeeService;


  private Boolean needsEndDate;
  private Boolean allValuesSet;
  private String introductionButtonMessage;
  private ReferenceLetter referenceLetter;
  private List<ReferenceLetter> referenceLetters;
  private ReferenceReason selectedReferenceReason;
  private List<ReferenceReason> referenceReasons;
  private Boolean editMode;
  private LocalDate selectedDeliveryDate;
  private LocalDate selectedEndDate;

  /**
   * Konstruktor, der eine Instanz von `IReferenceLetterService` injiziert.
   *
   * @param referenceLetterService Der Service für Referenzschreiben.
   * @param referenceReasonService Der Service für Referenzgründe.
   * @param employeeService Der Service für Mitarbeiter.
   */
  @Inject
  public ReferenceLetterBean(IReferenceLetterService referenceLetterService,
      IReferenceReasonService referenceReasonService, IEmployeeService employeeService) {
    this.referenceLetterService = referenceLetterService;
    this.referenceReasonService = referenceReasonService;
    this.employeeService = employeeService;
  }

  /**
   * Initialisiert die Bean nach der Konstruktion.
   */
  @PostConstruct
  public void init() {
    referenceLetter = new ReferenceLetter();
    referenceReasons = referenceReasonService.getAllReferenceReasons();
    needsEndDate = false;
  }

  /**
   * Setzt den Mitarbeiter für das aktuelle Arbeitszeugnis.
   *
   * @param employeeDTO Das Mitarbeiter-DTO, das dem Arbeitszeugnis zugeordnet werden soll.
   */
  public void setEmployee(EmployeeDTO employeeDTO) {
    referenceLetter.setEmployee(employeeService.convertToEntity(employeeDTO));
  }

  /**
   * Aktualisiert den ausgewählten Arbeitszeugnis-Typ im aktuellen Arbeitszeugnis.
   * Setzt auch, ob ein Enddatum erforderlich ist, basierend auf dem ausgewählten Typ.
   */
  public void updateSelectedReferenceReason() {
    if (selectedReferenceReason != null) {
      needsEndDate = !selectedReferenceReason.getReasonName().equals("Zwischenzeugnis");
      referenceLetter.setReferenceReason(selectedReferenceReason);
    }
  }

  /**
   * Aktualisiert das Enddatum im aktuellen Arbeitszeugnis.
   */
  public void updateSelectedEndDate() {
    if (selectedEndDate != null) {
      referenceLetter.setEndDate(selectedEndDate);
    }
  }

  /**
   * Aktualisiert das Ausstellungsdatum im aktuellen Arbeitszeugnis.
   */
  public void updateSelectedDeliveryDate() {
    if (selectedDeliveryDate != null) {
      referenceLetter.setDeliveryDate(selectedDeliveryDate);
    }
  }

  /**
   * Holt eine Liste aller Arbeitszeugnisse.
   *
   * @return Eine Liste von Arbeitszeugnissen.
   */
  public List<ReferenceLetter> getReferenceLetters() {
    return referenceLetterService.getAllReferenceLetters();
  }

  /**
   * Generiert die Einleitung für das aktuelle Arbeitszeugnis.
   * Überprüft, ob alle erforderlichen Werte gesetzt sind, bevor die Einleitung generiert wird.
   */
  public void generateIntroduction() {
    if (Boolean.TRUE.equals(
        referenceLetterService.checkReasonAndEmployeeSet(referenceLetter, needsEndDate))) {
      referenceLetter.setIntroduction(referenceLetterService.generateIntroduction(referenceLetter));
    } else {
      FrontendMessages.sendErrorMessageToFrontend("referenceLetterForm:generateIntroductionButton",
                                                  referenceLetterService.setErrorMessage(
                                                      referenceLetter, needsEndDate), null);
    }
  }

  /**
   * Speichert das aktuelle Arbeitszeugnis.
   * Überprüft, ob alle notwendigen Informationen vorhanden sind, bevor das Zeugnis gespeichert wird.
   */
  public void saveReferenceLetter() {
    String message;
    if (referenceLetter.getEmployee() == null || referenceLetter.getReferenceReason() == null) {
      message = "Bitte wählen Sie mindestens einen Mitarbeiter und einen Grund für das Arbeitszeugnis aus.";
      FrontendMessages.sendErrorMessageToFrontend(null, "Error", message);
    } else {
      referenceLetter = referenceLetterService.updateReferenceLetter(referenceLetter);
      editMode = true;
      message = referenceLetter.getReferenceReason().getReasonName() + " von "
          + referenceLetter.getEmployee().getFirstName() + " " + referenceLetter.getEmployee()
          .getLastName() + " wurde erfolgreich gespeichert.";
      LOGGER.info(message);
      FrontendMessages.sendInfoMessageToFrontend(null, "Info", message);
    }
  }

  /**
   * Setzt das aktuelle Arbeitszeugnis zurück.
   * Löscht alle gesetzten Werte und bereitet das Bean für ein neues Zeugnis vor.
   */
  public void resetReferenceLetter() {
    needsEndDate = false;
    editMode = false;
    selectedReferenceReason = null;
    referenceLetter = new ReferenceLetter();
    selectedDeliveryDate = null;
    selectedEndDate = null;
  }

  /**
   * Erstellt ein neues Arbeitszeugnis und navigiert zur entsprechenden Seite.
   *
   * @return Der Navigationspfad zur Seite für das Erstellen eines neuen Arbeitszeugnisses.
   */
  public String newReferenceLetter() {
    resetReferenceLetter();
    return "/resources/components/sites/secured/stepsToReferenceLetter.xhtml?faces-redirect=true";
  }

  /**
   * Lädt ein bestehendes Arbeitszeugnis zur Bearbeitung.
   *
   * @param referenceLetter Das zu ladende Arbeitszeugnis.
   * @return Der Navigationspfad zur Bearbeitungsseite des Arbeitszeugnisses.
   */
  public String loadReferenceLetter(ReferenceLetter referenceLetter) {
    this.referenceLetter = referenceLetter;
    this.selectedReferenceReason = referenceLetter.getReferenceReason();
    this.selectedDeliveryDate = referenceLetter.getDeliveryDate();
    this.selectedEndDate = referenceLetter.getEndDate();
    needsEndDate = !selectedReferenceReason.getReasonName().equals("Zwischenzeugnis");
    return "/resources/components/sites/secured/stepsToReferenceLetter.xhtml?faces-redirect=true";
  }

  /**
   * Löscht ein Arbeitszeugnis.
   *
   * @param referenceLetter Das zu löschende Arbeitszeugnis.
   */
  public void deleteReferenceLetter(ReferenceLetter referenceLetter) {
    referenceLetterService.deleteReferenceLetter(referenceLetter.getReferenceId());
  }

  /**
   * Konvertiert die Mitarbeiterinformationen des aktuellen Arbeitszeugnisses in ein DTO.
   *
   * @return Das Mitarbeiter-DTO des aktuellen Arbeitszeugnisses.
   */
  public EmployeeDTO getEmployeeAsDTO() {
    return employeeService.convertToDTO(referenceLetter.getEmployee());
  }
}

