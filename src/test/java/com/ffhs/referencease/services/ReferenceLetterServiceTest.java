package com.ffhs.referencease.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ffhs.referencease.dao.interfaces.IReferenceLetterDAO;
import com.ffhs.referencease.entities.Department;
import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.entities.Gender;
import com.ffhs.referencease.entities.Position;
import com.ffhs.referencease.entities.ReferenceLetter;
import com.ffhs.referencease.entities.ReferenceReason;
import com.ffhs.referencease.entities.TextTemplate;
import com.ffhs.referencease.entities.TextType;
import com.ffhs.referencease.entities.enums.EGender;
import com.ffhs.referencease.entities.enums.EReferenceReason;
import com.ffhs.referencease.entities.enums.ETextType;
import com.ffhs.referencease.services.interfaces.IReferenceReasonService;
import com.ffhs.referencease.services.interfaces.ITextTemplateService;
import com.ffhs.referencease.services.interfaces.ITextTypeService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ReferenceLetterServiceTest {


  @Mock
  private IReferenceLetterDAO referenceLetterDAO;

  @Mock
  private ITextTemplateService textTemplateService;

  @Mock
  private ITextTypeService textTypeService;

  @Mock
  private IReferenceReasonService referenceReasonService;
  @Mock
  private ReferenceLetter refLetter;
  @Mock
  private TextType textType;
  @Mock
  private ReferenceReason referenceReason;
  @Mock
  private ReferenceReason abschlusszeugnis;
  @Mock
  private ReferenceReason zwischenzeugnis;
  @Mock
  private ReferenceReason positionswechsel;
  @Mock
  private Gender maennlich;
  @Mock
  private Gender weiblich;

  private List<TextTemplate> textTemplates;

  private ReferenceLetterService referenceLetterService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    abschlusszeugnis = createMockReferenceReason(
        EReferenceReason.ABSCHLUSSZEUGNIS.getDisplayName());
    zwischenzeugnis = createMockReferenceReason(EReferenceReason.ZWISCHENZEUGNIS.getDisplayName());
    positionswechsel = createMockReferenceReason(
        EReferenceReason.POSITIONSWECHSEL.getDisplayName());
    maennlich = createMockGender(EGender.MAENNLICH.getDisplayName());
    weiblich = createMockGender(EGender.WEIBLICH.getDisplayName());
    referenceLetterService = new ReferenceLetterService(referenceLetterDAO, textTemplateService,
                                                        textTypeService);
    //    refLetter = createMockReferenceLetter();
    textType = createMockTextType();
    textTemplates = initializeMockTextTemplates();
    when(textTypeService.getTextTypeByName(ETextType.EINLEITUNG.getDisplayName())).thenReturn(
        textType);
    when(referenceReasonService.getReferenceReasonByReasonName(
        EReferenceReason.ABSCHLUSSZEUGNIS.getDisplayName())).thenReturn(referenceReason);
  }


  @Test
  void getReferenceLetterByIdTest() {
    UUID id = UUID.randomUUID();
    ReferenceLetter mockLetter = new ReferenceLetter();
    when(referenceLetterDAO.findById(id)).thenReturn(Optional.of(mockLetter));

    ReferenceLetter result = referenceLetterService.getReferenceLetterById(id);

    verify(referenceLetterDAO).findById(id);
    assertSame(mockLetter, result);
  }

  @Test
  void generateIntroductionZwischenzeugnisTest() {
    Position position = new Position();
    position.setPositionName("Projektleiter");
    Department department = new Department();
    department.setDepartmentName("Human Resources");
    Employee employee = createMockEmployee("Max", "Muster", LocalDate.of(1990, 1, 1),
                                           LocalDate.of(2010, 1, 1), position, department,
                                           maennlich);
    refLetter = createMockReferenceLetter(employee, zwischenzeugnis);

    when(textTemplateService.getTextTemplatesForReasonTypeAndGender(zwischenzeugnis, textType,
                                                                    maennlich)).thenReturn(
        initializeMockGetTextTemplatesForReasonTypeAndGender(zwischenzeugnis, textType, maennlich));
    String expected = "Max Muster, geboren am 1. Januar 1990, ist seit 1. Januar 2010 in der Abteilung Human Resources in unserem Unternehmen beschäftigt.";

    String actual = referenceLetterService.generateIntroduction(refLetter);

    assertEquals(expected, actual);
    // Führen Sie hier weitere Überprüfungen durch, basierend auf dem erwarteten Ergebnis des generierten Textes
  }

  @Test
  void generateIntroductionForAbschlusszeugnisTest() {
    Position position = new Position();
    position.setPositionName("Projektleiter");
    Department department = new Department();
    department.setDepartmentName("Human Resources");
    Employee employee = createMockEmployee("Max", "Muster", LocalDate.of(1990, 1, 1),
                                           LocalDate.of(2010, 1, 1), position, department,
                                           maennlich);
    refLetter = createMockReferenceLetter(employee, abschlusszeugnis);
    refLetter.setEndDate(LocalDate.of(2023, 1, 1));

    when(textTemplateService.getTextTemplatesForReasonTypeAndGender(abschlusszeugnis, textType,
                                                                    maennlich)).thenReturn(
        initializeMockGetTextTemplatesForReasonTypeAndGender(abschlusszeugnis, textType,
                                                             maennlich));
    String expected = "Max Muster, geboren am 1. Januar 1990, war vom 1. Januar 2010 bis 1. Januar 2023, als Projektleiter in der Abteilung Human Resources in unserem Unternehmen beschäftigt.";

    String actual = referenceLetterService.generateIntroduction(refLetter);

    assertEquals(expected, actual);
    // Führen Sie hier weitere Überprüfungen durch, basierend auf dem erwarteten Ergebnis des generierten Textes
  }

  private ReferenceLetter createMockReferenceLetter(Employee emp, ReferenceReason reason) {
    ReferenceLetter refLetter = new ReferenceLetter();
    refLetter.setEmployee(emp);
    refLetter.setReferenceReason(reason);
    return refLetter;
  }

  private Employee createMockEmployee(String firstName, String lastName, LocalDate dateOfBirth,
      LocalDate startDate, Position position, Department department, Gender gender) {
    Employee emp = new Employee();
    emp.setFirstName(firstName);
    emp.setLastName(lastName);
    emp.setDateOfBirth(dateOfBirth);
    emp.setStartDate(startDate);
    emp.setPosition(position);
    emp.setDepartment(department);
    emp.setGender(gender);
    return emp;
  }

  private ReferenceReason createMockReferenceReason(String reasonName) {
    ReferenceReason referenceReason = new ReferenceReason();
    referenceReason.setReasonName(reasonName);
    return referenceReason;
  }

  private TextType createMockTextType() {
    TextType textType = new TextType();
    textType.setTextTypeName("Einleitung");
    return textType;
  }


  private TextTemplate createMockTextTemplate(String key, String value,
      List<ReferenceReason> associatedReferenceReasons, List<Gender> associatedGenders,
      TextType textType) {
    TextTemplate textTemplate = new TextTemplate();
    textTemplate.setKey(key);
    textTemplate.setTemplate(value);
    textTemplate.setTextType(textType);
    textTemplate.setReferenceReasons(associatedReferenceReasons);
    textTemplate.setGenders(associatedGenders);
    return textTemplate;
  }

  private List<TextTemplate> initializeMockGetTextTemplatesForReasonTypeAndGender(
      ReferenceReason reason, TextType textType, Gender gender) {

    return textTemplates.stream()
        .filter(textTemplate -> textTemplate.getReferenceReasons().contains(reason))
        .filter(textTemplate -> textTemplate.getTextType() == textType)
        .filter(textTemplate -> textTemplate.getReferenceReasons().contains(reason))
        .filter(textTemplate -> textTemplate.getGenders().contains(gender))
        .collect(Collectors.toList());
  }

  private List<TextTemplate> initializeMockTextTemplates() {
    // Mock ReferenceReasons

    //    when(referenceReasonService.getReferenceReasonByReasonName(
    //        EReferenceReason.ABSCHLUSSZEUGNIS.getDisplayName())).thenReturn(abschlusszeugnis);
    //    when(referenceReasonService.getReferenceReasonByReasonName(
    //        EReferenceReason.ZWISCHENZEUGNIS.getDisplayName())).thenReturn(zwischenzeugnis);
    //    when(referenceReasonService.getReferenceReasonByReasonName(
    //        EReferenceReason.POSITIONSWECHSEL.getDisplayName())).thenReturn(positionswechsel);

    List<ReferenceReason> alleReferenceReasons = Arrays.asList(abschlusszeugnis, zwischenzeugnis,
                                                               positionswechsel);

    List<Gender> allGenders = Arrays.asList(maennlich, weiblich);

    // Mock TextTemplates
    TextTemplate templateAfterName = createMockTextTemplate("afterName", ", geboren am ",
                                                            alleReferenceReasons, allGenders,
                                                            textType);
    // Gemeinsame TextTemplates für alle ReferenceReasons
    TextTemplate textTemplate1 = createMockTextTemplate("afterName", ", geboren am ",
                                                        alleReferenceReasons, allGenders, textType);
    TextTemplate textTemplate2 = createMockTextTemplate("afterPosition", " in der Abteilung ",
                                                        alleReferenceReasons, allGenders, textType);
    TextTemplate textTemplate3 = createMockTextTemplate("afterDepartment",
                                                        " in unserem Unternehmen beschäftigt.",
                                                        alleReferenceReasons, allGenders, textType);

    // Spezifische TextTemplates für Abschlusszeugnis und Positionswechsel
    List<ReferenceReason> abschlussUndPositionswechsel = Arrays.asList(abschlusszeugnis,
                                                                       positionswechsel);
    TextTemplate textTemplate4 = createMockTextTemplate("afterDateOfBirth", ", war vom ",
                                                        abschlussUndPositionswechsel, allGenders,
                                                        textType);
    TextTemplate textTemplate5 = createMockTextTemplate("afterStartDate", " bis ",
                                                        abschlussUndPositionswechsel, allGenders,
                                                        textType);
    TextTemplate textTemplate6 = createMockTextTemplate("afterEndDate", ", als ",
                                                        abschlussUndPositionswechsel, allGenders,
                                                        textType);

    // Spezifisches TextTemplate für Zwischenzeugnis
    List<ReferenceReason> nurZwischenzeugnis = Collections.singletonList(zwischenzeugnis);
    TextTemplate textTemplate7 = createMockTextTemplate("afterDateOfBirth", ", ist seit ",
                                                        nurZwischenzeugnis, allGenders, textType);
    TextTemplate textTemplate8 = createMockTextTemplate("afterStartDate", ", als ",
                                                        nurZwischenzeugnis, allGenders, textType);
    return new ArrayList<>(
        Arrays.asList(textTemplate1, textTemplate2, textTemplate3, textTemplate4, textTemplate5,
                      textTemplate6, textTemplate7, textTemplate8));
  }

  private Gender createMockGender(String genderName) {
    Gender gender = new Gender();
    gender.setGenderName(genderName);
    return gender;
  }
}