package com.ffhs.referencease.services;


import com.ffhs.referencease.dao.interfaces.IReferenceLetterDAO;
import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.entities.Gender;
import com.ffhs.referencease.entities.ReferenceLetter;
import com.ffhs.referencease.entities.ReferenceReason;
import com.ffhs.referencease.entities.TextTemplate;
import com.ffhs.referencease.entities.TextType;
import com.ffhs.referencease.services.interfaces.IReferenceLetterService;
import com.ffhs.referencease.services.interfaces.ITextTemplateService;
import com.ffhs.referencease.services.interfaces.ITextTypeService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

/**
 * Bietet Dienste für die Verwaltung von Arbeitszeugnissen. Diese Klasse bietet Methoden für das
 * Auffinden, Speichern, Aktualisieren und Löschen von Arbeitszeugnissen sowie für die Generierung
 * von Texten basierend auf spezifischen Mitarbeiterdaten.
 *
 * @author Chris Wüthrich
 * @version 1.0.0
 */
@Stateless
public class ReferenceLetterService implements IReferenceLetterService {

  private static final String INTRODUCTION_TEXT_TYPE = "Einleitung";
  private static final String INTERIM_REPORT = "Zwischenzeugnis";
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. MMMM yyyy",
                                                                                 Locale.GERMAN);
  private final IReferenceLetterDAO referenceLetterDAO;
  private final ITextTemplateService textTemplateService;
  private final ITextTypeService textTypeService;


  @Inject
  public ReferenceLetterService(IReferenceLetterDAO referenceLetterDAO,
      ITextTemplateService textTemplateService, ITextTypeService textTypeService) {
    this.referenceLetterDAO = referenceLetterDAO;
    this.textTemplateService = textTemplateService;
    this.textTypeService = textTypeService;
  }

  /**
   * Gibt ein Referenzschreiben anhand seiner eindeutigen ID zurück.
   *
   * @param id Die UUID des gesuchten Referenzschreibens.
   * @return Das gefundene Referenzschreiben.
   * @throws NoSuchElementException wenn das Referenzschreiben nicht gefunden wird.
   */
  @Override
  public ReferenceLetter getReferenceLetterById(UUID id) {
    return referenceLetterDAO.findById(id)
        .orElseThrow(() -> new NoSuchElementException("ReferenceLetter not found with id: " + id));
  }

  /**
   * Gibt alle in der Datenbank gespeicherten Referenzschreiben zurück.
   *
   * @return Eine Liste aller Referenzschreiben.
   */
  @Override
  public List<ReferenceLetter> getAllReferenceLetters() {
    return referenceLetterDAO.findAll();
  }

  /**
   * Speichert ein neues Referenzschreiben in der Datenbank. Diese Methode wird verwendet, um ein
   * neu erstelltes Referenzschreiben zu persistieren.
   *
   * @param referenceLetter Das zu speichernde Referenzschreiben.
   */
  @Override
  public void saveReferenceLetter(ReferenceLetter referenceLetter) {
    referenceLetterDAO.create(referenceLetter);
  }

  /**
   * Aktualisiert ein vorhandenes Referenzschreiben in der Datenbank. Diese Methode wird verwendet,
   * um Änderungen an einem bestehenden Referenzschreiben zu speichern.
   *
   * @param referenceLetter Das zu aktualisierende Referenzschreiben.
   */
  @Override
  public void updateReferenceLetter(ReferenceLetter referenceLetter) {
    referenceLetterDAO.update(referenceLetter);
  }

  /**
   * Löscht ein Referenzschreiben anhand seiner ID. Diese Methode wird verwendet, um ein
   * Referenzschreiben aus der Datenbank zu entfernen.
   *
   * @param id Die eindeutige ID des zu löschenden Referenzschreibens.
   */
  @Override
  public void deleteReferenceLetter(UUID id) {
    referenceLetterDAO.deleteById(id);
  }

  /**
   * Erstellt einen Einleitungstext für ein Referenzschreiben basierend auf den Mitarbeiterdaten und
   * dem spezifischen Grund des Referenzschreibens.
   *
   * @param referenceLetter Das Referenzschreiben, für das der Einleitungstext generiert wird.
   * @return Der generierte Einleitungstext.
   */
  @Override
  public String generateIntroduction(ReferenceLetter referenceLetter) {
    TextType textType = textTypeService.getTextTypeByName(INTRODUCTION_TEXT_TYPE);
    List<TextTemplate> templates = getTextTemplatesForReasonTypeAndGender(
        referenceLetter.getReferenceReason(), textType, referenceLetter.getEmployee().getGender());
    EmployeeData data = prepareEmployeeData(referenceLetter.getEmployee(), referenceLetter);

    return generateIntroductionText(data, templates, referenceLetter.getReferenceReason());
  }

  /**
   * Bereitet die Daten eines Mitarbeiters für die Generierung eines Einleitungstextes vor.
   *
   * @param employee        Der Mitarbeiter, dessen Daten vorbereitet werden.
   * @param referenceLetter Das Referenzschreiben, das Daten des Mitarbeiters enthält.
   * @return Ein Objekt, das die vorbereiteten Daten des Mitarbeiters enthält.
   */
  private EmployeeData prepareEmployeeData(Employee employee, ReferenceLetter referenceLetter) {
    String firstName = employee.getFirstName();
    String lastName = employee.getLastName();
    String dateOfBirth = employee.getDateOfBirth().format(formatter);
    String startDate = employee.getStartDate().format(formatter);
    LocalDate endDatePure = referenceLetter.getEndDate();
    String endDate = endDatePure != null ? endDatePure.format(formatter) : "n/a";
    String position = employee.getPosition().getPositionName();
    String department = employee.getDepartment().getDepartmentName();

    return new EmployeeData(firstName, lastName, dateOfBirth, startDate, endDate, position,
                            department);
  }

  /**
   * Generiert einen Einleitungstext für ein Referenzschreiben, basierend auf den Daten eines
   * Mitarbeiters, Vorlagen für Textteile und dem Grund des Referenzschreibens.
   *
   * @param data      Die Daten des Mitarbeiters.
   * @param templates Die Liste der Textvorlagen.
   * @param reason    Der Grund des Referenzschreibens.
   * @return Der generierte Einleitungstext.
   */
  private String generateIntroductionText(EmployeeData data, List<TextTemplate> templates,
      ReferenceReason reason) {
    boolean isZwischenzeugnis = reason.getReasonName().equals(INTERIM_REPORT);

    StringBuilder introduction = new StringBuilder();
    introduction.append(data.firstName).append(" ").append(data.lastName);

    List<String> keysOrder = Arrays.asList("afterName", "afterDateOfBirth", "afterStartDate",
                                           "afterEndDate", "afterPosition", "afterDepartment");

    for (String key : keysOrder) {
      Optional<TextTemplate> templateOpt = templates.stream().filter(t -> t.getKey().equals(key))
          .findFirst();

      if (templateOpt.isPresent()) {
        String value = templateOpt.get().getTemplate();
        switch (key) {
          case "afterName":
            introduction.append(value).append(data.dateOfBirth);
            break;
          case "afterDateOfBirth":
            introduction.append(value).append(data.startDate);
            break;
          case "afterStartDate":
            if (!isZwischenzeugnis) {
              introduction.append(value).append(data.endDate);
            }
            break;
          case "afterEndDate":
            introduction.append(value).append(data.position);
            break;
          case "afterPosition":
            introduction.append(value).append(data.department);
            break;
          case "afterDepartment":
            introduction.append(value);
            break;
          default:
            throw new IllegalArgumentException("Unbekannter Schlüssel: " + key);
        }
      }
    }

    return introduction.toString();
  }

  private List<TextTemplate> getTextTemplatesForReasonTypeAndGender(ReferenceReason reason,
      TextType textType, Gender gender) {
    return textTemplateService.getTextTemplatesForReasonTypeAndGender(reason, textType, gender);
  }

  /**
   * Überprüft, ob alle erforderlichen Informationen für ein Referenzschreiben gesetzt sind.
   *
   * @param referenceLetter Das zu überprüfende Referenzschreiben.
   * @param needsEndDate    Gibt an, ob ein Enddatum benötigt wird.
   * @return true, wenn alle erforderlichen Informationen vorhanden sind, sonst false.
   */
  @Override
  public Boolean checkReasonAndEmployeeSet(ReferenceLetter referenceLetter, Boolean needsEndDate) {
    ReferenceReason reason = referenceLetter.getReferenceReason();
    if (Boolean.TRUE.equals(needsEndDate)) {
      return referenceLetter.getEmployee() != null && reason != null
          && referenceLetter.getEndDate() != null;
    } else {
      return referenceLetter.getEmployee() != null && reason != null;
    }
  }

  /**
   * Generiert eine Fehlermeldung basierend auf den fehlenden Informationen für ein
   * Referenzschreiben.
   *
   * @param referenceLetter Das Referenzschreiben, das überprüft wird.
   * @param needsEndDate    Gibt an, ob ein Enddatum benötigt wird.
   * @return Die generierte Fehlermeldung.
   */
  @Override
  public String setErrorMessage(ReferenceLetter referenceLetter, Boolean needsEndDate) {
    StringBuilder missingFields = new StringBuilder();

    Employee employee = referenceLetter.getEmployee();
    if (employee == null) {
      missingFields.append("\n[Kein Mitarbeiter ausgewählt]");
    } else if (employee.getGender() == null) {
      missingFields.append("\n[Geschlecht des Mitarbeiters nicht ausgewählt]");
    }

    ReferenceReason reason = referenceLetter.getReferenceReason();
    if (reason != null && needsEndDate) {
      LocalDate endDate = referenceLetter.getEndDate();
      if (endDate == null) {
        missingFields.append("\n[Enddatum nicht ausgewählt]");
      }
    } else if (reason == null) {
      missingFields.append("\n[Zeugnisart nicht ausgewählt]");
    }

    if (!missingFields.isEmpty()) {

      return
          "Der Text kann aufgrund der folgenden fehlenden Informationen nicht erstellt werden: \n"
              + missingFields;
    }
    return "Generiert den Text anhand der ausgewählten Informationen";
  }

  /**
   * Findet alle Referenzschreiben, die einem bestimmten Mitarbeiter zugeordnet sind.
   *
   * @param employeeId Die ID des Mitarbeiters.
   * @return Eine Liste von Referenzschreiben, die dem Mitarbeiter zugeordnet sind.
   */
  @Override
  public List<ReferenceLetter> findReferenceLettersByEmployeeId(UUID employeeId) {
    return referenceLetterDAO.findReferenceLettersByEmployeeId(employeeId);
  }

  private record EmployeeData(String firstName, String lastName, String dateOfBirth,
                              String startDate, String endDate, String position,
                              String department) {

  }
}
