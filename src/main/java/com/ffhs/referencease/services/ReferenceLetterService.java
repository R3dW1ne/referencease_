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

@Stateless
public class ReferenceLetterService implements IReferenceLetterService {

  private final IReferenceLetterDAO referenceLetterDAO;

  private final ITextTemplateService textTemplateService;

  private final ITextTypeService textTypeService;

  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. MMMM yyyy",
      Locale.GERMAN);


  @Inject
  public ReferenceLetterService(IReferenceLetterDAO referenceLetterDAO,
      ITextTemplateService textTemplateService, ITextTypeService textTypeService) {
    this.referenceLetterDAO = referenceLetterDAO;
    this.textTemplateService = textTemplateService;
    this.textTypeService = textTypeService;
  }

  @Override
  public ReferenceLetter getReferenceLetterById(UUID id) {
    return referenceLetterDAO.findById(id)
        .orElseThrow(() -> new NoSuchElementException("ReferenceLetter not found with id: " + id));
  }

  @Override
  public List<ReferenceLetter> getAllReferenceLetters() {
    return referenceLetterDAO.findAll();
  }

  @Override
  public void saveReferenceLetter(ReferenceLetter referenceLetter) {
    referenceLetterDAO.create(referenceLetter);
  }

  @Override
  public void updateReferenceLetter(ReferenceLetter referenceLetter) {
    referenceLetterDAO.update(referenceLetter);
  }

  @Override
  public void deleteReferenceLetter(UUID id) {
    referenceLetterDAO.deleteById(id);
  }

  @Override
  public String generateIntroduction(ReferenceLetter referenceLetter) {
    TextType textType = textTypeService.getTextTypeByName("Einleitung");
    List<TextTemplate> templates = getTextTemplatesForReasonTypeAndGender(
        referenceLetter.getReferenceReason(), textType, referenceLetter.getEmployee().getGender());
    EmployeeData data = prepareEmployeeData(referenceLetter.getEmployee(), referenceLetter);

    return generateIntroductionText(data, templates, referenceLetter.getReferenceReason());
  }

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

  private static class EmployeeData {

    String firstName;
    String lastName;
    String dateOfBirth;
    String startDate;
    String endDate;
    String position;
    String department;

    public EmployeeData(String firstName, String lastName, String dateOfBirth, String startDate,
        String endDate, String position, String department) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.dateOfBirth = dateOfBirth;
      this.startDate = startDate;
      this.endDate = endDate;
      this.position = position;
      this.department = department;
    }
  }

  private String generateIntroductionText(EmployeeData data, List<TextTemplate> templates, ReferenceReason reason) {
    boolean isZwischenzeugnis = reason.getName().equals("Zwischenzeugnis");

    StringBuilder introduction = new StringBuilder();
    introduction.append(data.firstName).append(" ").append(data.lastName);

    List<String> keysOrder = Arrays.asList("afterName", "afterDateOfBirth", "afterStartDate", "afterEndDate", "afterPosition", "afterDepartment");

    for (String key : keysOrder) {
      Optional<TextTemplate> templateOpt = templates.stream()
          .filter(t -> t.getKey().equals(key))
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

//  @Override
//  public String generateIntroduction(ReferenceLetter referenceLetter) {
//    TextType textType = textTypeService.getTextTypeByName("Einleitung");
//    // Sammeln der erforderlichen Daten
//    Employee employee = referenceLetter.getEmployee();
//    String firstName = employee.getFirstName();
//    String lastName = employee.getLastName();
//    String dateOfBirth = employee.getDateOfBirth().format(formatter);
//    String startDate = employee.getStartDate().format(formatter);
//    LocalDate endDatePure = referenceLetter.getEndDate();
//    String endDate = endDatePure != null ? endDatePure.format(formatter) : "n/a";
//    String position = employee.getPosition().getPositionName();
//    String department = employee.getDepartment().getDepartmentName();
//    ReferenceReason reason = referenceLetter.getReferenceReason();
//    boolean isZwischenzeugnis = reason.getName().equals("Zwischenzeugnis");
//    Gender gender = employee.getGender();
//
//
//    // Festlegen der Reihenfolge der Schlüssel
//    List<String> keysOrder = Arrays.asList("afterName", "afterDateOfBirth", "afterStartDate",
//        "afterEndDate", "afterPosition", "afterDepartment");
//
//    StringBuilder introduction = new StringBuilder();
//    introduction.append(firstName).append(" ").append(lastName);
//
//    List<TextTemplate> templates = getTextTemplatesForReasonTypeAndGender(reason, textType, gender);
//
//    // Durchgehen der definierten Schlüsselreihenfolge
//    for (String key : keysOrder) {
//      // Überprüfen, ob das aktuelle Schlüssel-Template vorhanden ist
//      Optional<TextTemplate> templateOpt = templates.stream()
//          .filter(t -> t.getKey().equals(key))
//          .findFirst();
//
//      if (templateOpt.isPresent()) {
//        TextTemplate template = templateOpt.get();
//        String value = template.getTemplate();
//
//        // Überprüfen des 'Zwischenzeugnis'-Szenarios
//        if (isZwischenzeugnis && key.equals("afterEndDate")) {
//          continue; // Überspringen, wenn 'Zwischenzeugnis' und 'afterEndDate'
//        }
//
//        // Verarbeitung der verschiedenen Schlüssel
//        switch (key) {
//          case "afterName":
//            introduction.append(value).append(dateOfBirth);
//            break;
//          case "afterDateOfBirth":
//            introduction.append(value).append(startDate);
//            break;
//          case "afterStartDate":
//            if (isZwischenzeugnis) {
//              introduction.append(value).append(position);
//            } else {
//              introduction.append(value).append(endDate);
//            }
//            break;
//          case "afterEndDate":
//            introduction.append(value).append(position);
//            break;
//          case "afterPosition":
//            introduction.append(value).append(department);
//            break;
//          case "afterDepartment":
//            introduction.append(value);
//            break;
//          default:
//            throw new IllegalArgumentException("Unbekannter Schlüssel: " + key);
//        }
//      }
//    }
//
//    return introduction.toString();
//  }


  private List<TextTemplate> getTextTemplatesForReasonTypeAndGender(ReferenceReason reason,
      TextType textType, Gender gender) {
    return textTemplateService.getTextTemplatesForReasonTypeAndGender(reason, textType, gender);
  }

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

  @Override
  public List<ReferenceLetter> findReferenceLettersByEmployeeId(UUID employeeId) {
    return referenceLetterDAO.findReferenceLettersByEmployeeId(employeeId);
  }
}
