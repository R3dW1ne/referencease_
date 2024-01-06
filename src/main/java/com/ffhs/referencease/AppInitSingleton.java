package com.ffhs.referencease;

import com.ffhs.referencease.entities.enums.EGender;
import com.ffhs.referencease.entities.enums.EProperty;
import com.ffhs.referencease.entities.enums.EReferenceReason;
import com.ffhs.referencease.entities.enums.ERole;
import com.ffhs.referencease.entities.enums.ETextType;
import com.ffhs.referencease.services.interfaces.IDepartmentService;
import com.ffhs.referencease.services.interfaces.IEmployeeService;
import com.ffhs.referencease.services.interfaces.IGenderService;
import com.ffhs.referencease.services.interfaces.IPositionService;
import com.ffhs.referencease.services.interfaces.IPropertyService;
import com.ffhs.referencease.services.interfaces.IReferenceReasonService;
import com.ffhs.referencease.services.interfaces.IRoleService;
import com.ffhs.referencease.services.interfaces.ITextTemplateService;
import com.ffhs.referencease.services.interfaces.ITextTypeService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import java.util.Random;

/**
 * Singleton-Klasse, die bei der Initialisierung der Anwendung ausgeführt wird. Diese Klasse ist
 * verantwortlich für die Einrichtung anfänglicher Daten und Konfigurationen, die für das
 * Funktionieren der Anwendung erforderlich sind. Sie erstellt Standardwerte und Daten für Rollen,
 * Geschlechter, Referenzgründe, Texttypen, Eigenschaften, Abteilungen, Positionen und initialisiert
 * Textvorlagen. Zudem erstellt sie eine definierte Anzahl von zufälligen Mitarbeiterdatensätzen,
 * falls noch keine Mitarbeiter in der Datenbank vorhanden sind.
 */
@Startup
@Singleton
public class AppInitSingleton {

  private final IEmployeeService employeeService;
  private final IGenderService genderService;
  private final IReferenceReasonService referenceReasonService;
  private final ITextTypeService textTypeService;
  private final IPropertyService propertyService;
  private final IDepartmentService departmentService;
  private final IRoleService roleService;
  private final IPositionService positionService;
  private final ITextTemplateService textTemplateService;
  private final Random random = new Random();

  /**
   * Konstruktor, der verschiedene Service-Komponenten injiziert.
   *
   * @param employeeService        Der Service für Mitarbeiterverwaltung.
   * @param genderService          Der Service für Geschlechterverwaltung.
   * @param referenceReasonService Der Service für Referenzgründe.
   * @param textTypeService        Der Service für Texttypen.
   * @param propertyService        Der Service für Eigenschaften.
   * @param departmentService      Der Service für Abteilungen.
   * @param roleService            Der Service für Rollen.
   * @param positionService        Der Service für Positionen.
   * @param textTemplateService    Der Service für Textvorlagen.
   */
  @Inject
  public AppInitSingleton(IEmployeeService employeeService, IGenderService genderService,
      IReferenceReasonService referenceReasonService, ITextTypeService textTypeService,
      IPropertyService propertyService, IDepartmentService departmentService,
      IRoleService roleService, IPositionService positionService,
      ITextTemplateService textTemplateService) {
    this.employeeService = employeeService;
    this.genderService = genderService;
    this.referenceReasonService = referenceReasonService;
    this.textTypeService = textTypeService;
    this.propertyService = propertyService;
    this.departmentService = departmentService;
    this.roleService = roleService;
    this.positionService = positionService;
    this.textTemplateService = textTemplateService;
  }
  /**
   * Initialisiert die Anwendung durch Erstellung von Standarddaten und Konfigurationen.
   * Diese Methode wird automatisch nach der Konstruktion des Singletons aufgerufen.
   */
  @PostConstruct
  public void init() {
    for (ERole roleEnum : ERole.values()) {
      roleService.createRoleIfNotExists(roleEnum.getDisplayName());
    }
    for (EGender genderEnum : EGender.values()) {
      genderService.createGenderIfNotExists(genderEnum.getDisplayName());
    }
    for (EReferenceReason reasonEnum : EReferenceReason.values()) {
      referenceReasonService.createReferenceReasonIfNotExists(reasonEnum.getDisplayName());
    }
    for (ETextType textTypeEnum : ETextType.values()) {
      textTypeService.createTextTypeIfNotExists(textTypeEnum.getDisplayName());
    }
    for (EProperty propertyEnum : EProperty.values()) {
      propertyService.createPropertyIfNotExists(propertyEnum.getDisplayName());
    }
    departmentService.createDepartmentIfNotExists("Human Resources");
    departmentService.createDepartmentIfNotExists("Finance");
    departmentService.createDepartmentIfNotExists("Shared IT");
    positionService.createPositionIfNotExists("Projektleiter");
    positionService.createPositionIfNotExists("Lernender");
    positionService.createPositionIfNotExists("System Engineer");
    positionService.createPositionIfNotExists("Application Engineer");
    positionService.createPositionIfNotExists("Elektroinstallateur");
    textTemplateService.initializeTextTemplates();
    employeeService.createRandomEmployeesIfNotExists(20, random);
  }
}
