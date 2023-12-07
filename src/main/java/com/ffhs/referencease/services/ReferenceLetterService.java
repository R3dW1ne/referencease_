package com.ffhs.referencease.services;


import com.ffhs.referencease.dao.interfaces.IReferenceLetterDAO;
import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.entities.ReferenceLetter;
import com.ffhs.referencease.entities.ReferenceReason;
import com.ffhs.referencease.entities.TextTemplate;
import com.ffhs.referencease.services.interfaces.IReferenceLetterService;
import com.ffhs.referencease.services.interfaces.ITextTemplateService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Stateless
public class ReferenceLetterService implements IReferenceLetterService {

  private final IReferenceLetterDAO referenceLetterDAO;

  private final ITextTemplateService textTemplateService;

  @Inject
  public ReferenceLetterService(IReferenceLetterDAO referenceLetterDAO,
      ITextTemplateService textTemplateService) {
    this.referenceLetterDAO = referenceLetterDAO;
    this.textTemplateService = textTemplateService;
  }

  @Override
  public ReferenceLetter getReferenceLetterById(UUID id) {
    return referenceLetterDAO.findById(id).orElseThrow(() -> new NoSuchElementException("ReferenceLetter not found with id: " + id));
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
    ReferenceLetter referenceLetter = getReferenceLetterById(id);
    referenceLetterDAO.delete(referenceLetter);
  }

  @Override
  public String generateIntroduction(ReferenceLetter referenceLetter) {
    Employee employee = referenceLetter.getEmployee();
    ReferenceReason reason = referenceLetter.getReferenceReason();

    StringBuilder introduction = new StringBuilder();
    introduction.append(employee.getFirstName()).append(" ")
        .append(employee.getLastName());

    List<TextTemplate> templates = getTextTemplatesForReason(reason);

    for (TextTemplate template : templates) {
      String key = template.getKey();
      String value = template.getTemplate();
      introduction.append(value);

      switch (key) {
        case "afterName":
          introduction.append(employee.getDateOfBirth());
          break;
        case "afterDateOfBirth":
          introduction.append(employee.getStartDate());
          break;
        case "afterStartDate":
          if (reason.getName().equals("Zwischenzeugnis")) {
            introduction.append(", als ");
          } else {
            introduction.append(" bis ").append(employee.getEndDate());
          }
          break;
        case "afterEndDate":
          introduction.append(employee.getPosition());
          break;
        case "afterPosition":
          introduction.append(" in der Abteilung ").append(employee.getDepartment());
          break;
        case "afterDepartment":
          introduction.append(" in unserem Unternehmen beschäftigt.");
          break;
        default:
          throw new IllegalArgumentException("Unbekannter Schlüssel: " + key);
        // Weitere Fälle nach Bedarf
      }
    }

    return introduction.toString();
  }

  private List<TextTemplate> getTextTemplatesForReason(ReferenceReason reason) {
    return textTemplateService.getTextTemplatesForReason(reason);
  }
}
