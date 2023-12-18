package com.ffhs.referencease;

import com.ffhs.referencease.entities.Department;
import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.entities.Gender;
import com.ffhs.referencease.entities.Position;
import com.ffhs.referencease.entities.Property;
import com.ffhs.referencease.entities.ReferenceReason;
import com.ffhs.referencease.entities.Role;
import com.ffhs.referencease.entities.TextTemplate;
import com.ffhs.referencease.entities.TextType;
import com.ffhs.referencease.entities.enums.EGender;
import com.ffhs.referencease.entities.enums.EProperty;
import com.ffhs.referencease.entities.enums.EReferenceReason;
import com.ffhs.referencease.entities.enums.ERole;
import com.ffhs.referencease.entities.enums.ETextType;
import com.ffhs.referencease.producers.qualifiers.ProdPU;
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
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;


@Startup
@Singleton
public class AppInitSingleton {

  private final IEmployeeService employeeService;
  private final IGenderService genderService;
  private final IReferenceReasonService referenceReasonService;
  private final ITextTypeService textTypeService;
  private final IPropertyService propertyService;
  private  final IDepartmentService departmentService;
  private final IRoleService roleService;
  private final IPositionService positionService;
  private final ITextTemplateService textTemplateService;

  Random random = new Random();

  @Inject
  public AppInitSingleton(IEmployeeService employeeService,
      IGenderService genderService, IReferenceReasonService referenceReasonService,
      ITextTypeService textTypeService, IPropertyService propertyService,
      IDepartmentService departmentService, IRoleService roleService,
      IPositionService positionService, ITextTemplateService textTemplateService) {
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
    initializeTextTemplates();
    createRandomEmployees(20);
  }

  private void initializeTextTemplates() {
    ReferenceReason abschlusszeugnis = referenceReasonService.getReferenceReasonByReasonName(
        EReferenceReason.ABSCHLUSSZEUGNIS.getDisplayName());
    ReferenceReason zwischenzeugnis = referenceReasonService.getReferenceReasonByReasonName(
        EReferenceReason.ZWISCHENZEUGNIS.getDisplayName());
    ReferenceReason positionswechsel = referenceReasonService.getReferenceReasonByReasonName(
        EReferenceReason.POSITIONSWECHSEL.getDisplayName());
    List<ReferenceReason> alleReferenceReasons = Arrays.asList(abschlusszeugnis, zwischenzeugnis,
        positionswechsel);
    List<Gender> allGenders = genderService.getAllGenders();
    TextType textType = textTypeService.getTextTypeByName(ETextType.EINLEITUNG.getDisplayName());

    // Gemeinsame TextTemplates für alle ReferenceReasons
    textTemplateService.createTextTemplateIfNotExists("afterName", ", geboren am ", alleReferenceReasons, allGenders,
        textType);
    textTemplateService.createTextTemplateIfNotExists("afterPosition", " in der Abteilung ", alleReferenceReasons,
        allGenders, textType);
    textTemplateService.createTextTemplateIfNotExists("afterDepartment", " in unserem Unternehmen beschäftigt.",
        alleReferenceReasons, allGenders, textType);

    // Spezifische TextTemplates für Abschlusszeugnis und Positionswechsel
    List<ReferenceReason> abschlussUndPositionswechsel = Arrays.asList(abschlusszeugnis,
        positionswechsel);
    textTemplateService.createTextTemplateIfNotExists("afterDateOfBirth", ", war vom ", abschlussUndPositionswechsel,
        allGenders, textType);
    textTemplateService.createTextTemplateIfNotExists("afterStartDate", " bis ", abschlussUndPositionswechsel,
        allGenders, textType);
    textTemplateService.createTextTemplateIfNotExists("afterEndDate", ", als ", abschlussUndPositionswechsel,
        allGenders, textType);

    // Spezifisches TextTemplate für Zwischenzeugnis
    List<ReferenceReason> nurZwischenzeugnis = Collections.singletonList(zwischenzeugnis);
    textTemplateService.createTextTemplateIfNotExists("afterDateOfBirth", ", ist seit ", nurZwischenzeugnis, allGenders,
        textType);
    textTemplateService.createTextTemplateIfNotExists("afterStartDate", ", als ", nurZwischenzeugnis, allGenders,
        textType);
  }

//  private ReferenceReason getReferenceReasonByReasonName(String name) {
//    try {
//      return em.createQuery("SELECT r FROM ReferenceReason r WHERE r.reasonName = :name",
//              ReferenceReason.class)
//          .setParameter("name", name)
//          .getSingleResult();
//    } catch (NoResultException e) {
//      // ReferenceReason mit diesem Namen wurde nicht gefunden, Sie können ihn erstellen oder eine Ausnahme werfen
//      ReferenceReason referenceReason = new ReferenceReason();
//      referenceReason.setReasonName(name);
//      em.persist(referenceReason);
//      return referenceReason;
//    }
//  }

//  private TextType getTextType(String typeName) {
//    try {
//      return em.createQuery("SELECT t FROM TextType t WHERE t.textTypeName = :typeName",
//              TextType.class)
//          .setParameter("typeName", typeName)
//          .getSingleResult();
//    } catch (NoResultException e) {
//      // TextType mit diesem typeName wurde nicht gefunden, Sie können ihn erstellen oder eine Ausnahme werfen
//      TextType textType = new TextType();
//      textType.setTextTypeName(typeName);
//      em.persist(textType);
//      return textType;
//    }
//  }

//  @Transactional
//  private void createTextTemplateIfNotExists(String key, String template,
//      List<ReferenceReason> referenceReasons, List<Gender> genders, TextType textType) {
//    // Erstellt eine Query, die alle Bedingungen überprüft
//    String queryStr = "SELECT t FROM TextTemplate t WHERE t.key = :key AND t.template = :template AND t.textType = :textType";
//    TypedQuery<TextTemplate> query = em.createQuery(queryStr, TextTemplate.class)
//        .setParameter("key", key)
//        .setParameter("template", template)
//        .setParameter("textType", textType);
//
//    // Prüft, ob ein TextTemplate mit den gegebenen Kriterien existiert
//    boolean exists = query.getResultList().stream().anyMatch(textTemplate ->
//        new HashSet<>(textTemplate.getReferenceReasons()).containsAll(referenceReasons) &&
//            new HashSet<>(textTemplate.getGenders()).containsAll(genders));
//
//    if (!exists) {
//      TextTemplate newTextTemplate = new TextTemplate();
//      newTextTemplate.setKey(key);
//      newTextTemplate.setTemplate(template);
//      newTextTemplate.setReferenceReasons(referenceReasons);
//      newTextTemplate.setGenders(genders);
//      newTextTemplate.setTextType(textType);
//
//      em.persist(newTextTemplate);
//    }
//  }
//  @Transactional
//  private void createReferenceReasonIfNotExists(String reasonName) {
//    if (em.createQuery("SELECT r FROM ReferenceReason r WHERE r.reasonName = :reasonName",
//            ReferenceReason.class)
//        .setParameter("reasonName", reasonName)
//        .getResultList().isEmpty()) {
//      ReferenceReason referenceReason = new ReferenceReason();
//      referenceReason.setReasonName(reasonName);
//      em.persist(referenceReason);
//    }
//  }
//  @Transactional
//  private void createTextTypeIfNotExists(String typeName) {
//    if (em.createQuery("SELECT t FROM TextType t WHERE t.textTypeName = :typeName",
//            TextType.class)
//        .setParameter("typeName", typeName)
//        .getResultList().isEmpty()) {
//      TextType textType = new TextType();
//      textType.setTextTypeName(typeName);
//      em.persist(textType);
//    }
//  }
//  @Transactional
//  private void createPropertyIfNotExists(String propertyName) {
//    if (em.createQuery("SELECT p FROM Property p WHERE p.propertyName = :propertyName",
//            Property.class)
//        .setParameter("propertyName", propertyName)
//        .getResultList().isEmpty()) {
//      Property property = new Property();
//      property.setPropertyName(propertyName);
//      em.persist(property);
//    }
//  }
//  @Transactional
//  private void createGenderIfNotExists(String genderName) {
//    if (em.createQuery("SELECT g FROM Gender g WHERE g.genderName = :genderName",
//            Gender.class)
//        .setParameter("genderName", genderName)
//        .getResultList().isEmpty()) {
//      Gender gender = new Gender();
//      gender.setGenderName(genderName);
//      em.persist(gender);
//    }
//  }
//  @Transactional
//  private void createRoleIfNotExists(String roleName) {
//    if (em.createQuery("SELECT r FROM Role r WHERE r.roleName = :roleName", Role.class)
//        .setParameter("roleName", roleName)
//        .getResultList().isEmpty()) {
//      Role role = new Role(roleName);
//      em.persist(role);
//    }
//  }
//  @Transactional
//  private void createDepartmentIfNotExists(String departmentName) {
//    if (em.createQuery(
//            "SELECT d FROM Department d WHERE d.departmentName = :departmentName", Department.class)
//        .setParameter("departmentName", departmentName)
//        .getResultList().isEmpty()) {
//      Department department = new Department();
//      department.setDepartmentName(departmentName);
//      em.persist(department);
//    }
//  }
//  @Transactional
//  private void createPositionIfNotExists(String positionName) {
//    if (em.createQuery("SELECT p FROM Position p WHERE p.positionName = :positionName",
//            Position.class)
//        .setParameter("positionName", positionName)
//        .getResultList().isEmpty()) {
//      Position position = new Position();
//      position.setPositionName(positionName);
//      em.persist(position);
//    }
//  }
  @Transactional
  private void createRandomEmployees(int count) {
    // Überprüfen, ob bereits Mitarbeiter in der Datenbank existieren
    long employeeCount = employeeService.countEmployees();
    if (employeeCount > 0) {
      // Es gibt bereits Mitarbeiter, also keine neuen hinzufügen
      return;
    }

    for (int i = 0; i < count; i++) {
      Employee employee = new Employee();
      employee.setEmployeeNumber(
          UUID.randomUUID().toString().substring(0, 8)); // Zufällige Mitarbeiternummer
      employee.setFirstName("Firstname" + i);
      employee.setLastName("Lastname" + i);
      employee.setDateOfBirth(LocalDate.now().minusYears(
          random.nextInt(40) + 18)); // Zufälliges Geburtsdatum zwischen 18 und 58 Jahren
      employee.setPhone("123-456-7890"); // Beispieltelefonnummer
      employee.setStartDate(LocalDate.now().minusYears(random.nextInt(
          10))); // Zufälliges Anfangsdatum der Beschäftigung in den letzten 10 Jahren
      // Abteilung kann auch zufällig zugewiesen werden
      Department randomDepartment = getRandomDepartment();
      if (randomDepartment != null) {
        employee.setDepartment(randomDepartment);
      }

      // Zufällige Position auswählen
      Position randomPosition = getRandomPosition();
      if (randomPosition != null) {
        employee.setPosition(randomPosition);
      }

      // Zufälliges Gender auswählen
      Gender randomGender = getRandomGender();
      if (randomGender != null) {
        employee.setGender(randomGender);
      }
      employeeService.saveEmployee(employee);
    }
  }

  private Gender getRandomGender() {
    // Annahme: Alle vorhandenen Geschlechter abrufen
    List<Gender> genders = genderService.getAllGenders();

    if (!genders.isEmpty()) {
      return genders.get(random.nextInt(genders.size()));
    } else {
      return null;
    }
  }


  // Methode zum Abrufen einer zufälligen Abteilung aus der Datenbank
  private Department getRandomDepartment() {
    // Annahme: Alle vorhandenen Abteilungen abrufen
    List<Department> departments = departmentService.getAllDepartments();

    if (!departments.isEmpty()) {
      return departments.get(random.nextInt(departments.size()));
    } else {
      return null;
    }
  }

  // Methode zum Abrufen einer zufälligen Position aus der Datenbank
  private Position getRandomPosition() {
    // Annahme: Alle vorhandenen Positionen abrufen
    List<Position> positions = positionService.getAllPositions();

    if (!positions.isEmpty()) {
      return positions.get(random.nextInt(positions.size()));
    } else {
      return null;
    }
  }
}
