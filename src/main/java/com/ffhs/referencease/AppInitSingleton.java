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
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
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

  @PersistenceContext
  private EntityManager entityManager;

  Random random = new Random();

  @PostConstruct
  public void init() {
    for (ERole roleEnum : ERole.values()) {
      createRoleIfNotExists(roleEnum.getDisplayName());
    }
    for (EGender genderEnum : EGender.values()) {
      createGenderIfNotExists(genderEnum.getDisplayName());
    }
    for (EReferenceReason reasonEnum : EReferenceReason.values()) {
      createReferenceReasonIfNotExists(reasonEnum.getDisplayName());
    }
    for (ETextType textTypeEnum : ETextType.values()) {
      createTextTypeIfNotExists(textTypeEnum.getDisplayName());
    }
    for (EProperty propertyEnum : EProperty.values()) {
      createPropertyIfNotExists(propertyEnum.getDisplayName());
    }
    createDepartmentIfNotExists("Human Resources");
    createDepartmentIfNotExists("Finance");
    createDepartmentIfNotExists("Shared IT");
    createPositionIfNotExists("Projektleiter");
    createPositionIfNotExists("Lernender");
    createPositionIfNotExists("System Engineer");
    createPositionIfNotExists("Application Engineer");
    createPositionIfNotExists("Elektroinstallateur");
    initializeTextTemplates();
    createRandomEmployees(20);
  }

  private void initializeTextTemplates() {
    ReferenceReason abschlusszeugnis = getReferenceReason(EReferenceReason.ABSCHLUSSZEUGNIS.getDisplayName());
    ReferenceReason zwischenzeugnis = getReferenceReason(EReferenceReason.ZWISCHENZEUGNIS.getDisplayName());
    ReferenceReason positionswechsel = getReferenceReason(EReferenceReason.POSITIONSWECHSEL.getDisplayName());
    List<ReferenceReason> alleReferenceReasons = Arrays.asList(abschlusszeugnis, zwischenzeugnis, positionswechsel);
    List<Gender> allGenders = getAllGenders();
    TextType textType = getTextType(ETextType.EINLEITUNG.getDisplayName());

    // Gemeinsame TextTemplates für alle ReferenceReasons
    createTextTemplateIfNotExists("afterName", ", geboren am ", alleReferenceReasons, allGenders, textType);
    createTextTemplateIfNotExists("afterPosition", " in der Abteilung ", alleReferenceReasons, allGenders, textType);
    createTextTemplateIfNotExists("afterDepartment", " in unserem Unternehmen beschäftigt.", alleReferenceReasons, allGenders, textType);

    // Spezifische TextTemplates für Abschlusszeugnis und Positionswechsel
    List<ReferenceReason> abschlussUndPositionswechsel = Arrays.asList(abschlusszeugnis, positionswechsel);
    createTextTemplateIfNotExists("afterDateOfBirth", ", war vom ", abschlussUndPositionswechsel, allGenders, textType);
    createTextTemplateIfNotExists("afterStartDate", " bis ", abschlussUndPositionswechsel, allGenders, textType);
    createTextTemplateIfNotExists("afterEndDate", ", als ", abschlussUndPositionswechsel, allGenders, textType);

    // Spezifisches TextTemplate für Zwischenzeugnis
    List<ReferenceReason> nurZwischenzeugnis = Collections.singletonList(zwischenzeugnis);
    createTextTemplateIfNotExists("afterDateOfBirth", ", ist seit ", nurZwischenzeugnis, allGenders, textType);
    createTextTemplateIfNotExists("afterStartDate", ", als ", nurZwischenzeugnis, allGenders, textType);
  }

  private List<Gender> getAllGenders() {
    return entityManager.createQuery("SELECT g FROM Gender g", Gender.class)
        .getResultList();
  }

  private ReferenceReason getReferenceReason(String name) {
    try {
      return entityManager.createQuery("SELECT r FROM ReferenceReason r WHERE r.name = :name", ReferenceReason.class)
          .setParameter("name", name)
          .getSingleResult();
    } catch (NoResultException e) {
      // ReferenceReason mit diesem Namen wurde nicht gefunden, Sie können ihn erstellen oder eine Ausnahme werfen
      ReferenceReason referenceReason = new ReferenceReason();
      referenceReason.setName(name);
      entityManager.persist(referenceReason);
      return referenceReason;
    }
  }

  private TextType getTextType(String typeName) {
    try {
      return entityManager.createQuery("SELECT t FROM TextType t WHERE t.textTypeName = :typeName", TextType.class)
          .setParameter("typeName", typeName)
          .getSingleResult();
    } catch (NoResultException e) {
      // TextType mit diesem typeName wurde nicht gefunden, Sie können ihn erstellen oder eine Ausnahme werfen
      TextType textType = new TextType();
      textType.setTextTypeName(typeName);
      entityManager.persist(textType);
      return textType;
    }
  }

  private void createTextTemplateIfNotExists(String key, String template, List<ReferenceReason> referenceReasons, List<Gender> genders, TextType textType) {
    // Erstellt eine Query, die alle Bedingungen überprüft
    String queryStr = "SELECT t FROM TextTemplate t WHERE t.key = :key AND t.template = :template AND t.textType = :textType";
    TypedQuery<TextTemplate> query = entityManager.createQuery(queryStr, TextTemplate.class)
        .setParameter("key", key)
        .setParameter("template", template)
        .setParameter("textType", textType);

    // Prüft, ob ein TextTemplate mit den gegebenen Kriterien existiert
    boolean exists = query.getResultList().stream().anyMatch(textTemplate ->
        new HashSet<>(textTemplate.getReferenceReasons()).containsAll(referenceReasons) &&
            new HashSet<>(textTemplate.getGenders()).containsAll(genders));

    if (!exists) {
      TextTemplate newTextTemplate = new TextTemplate();
      newTextTemplate.setKey(key);
      newTextTemplate.setTemplate(template);
      newTextTemplate.setReferenceReasons(referenceReasons);
      newTextTemplate.setGenders(genders);
      newTextTemplate.setTextType(textType);

      entityManager.persist(newTextTemplate);
    }
  }

  private void createReferenceReasonIfNotExists(String reasonName) {
    if (entityManager.createQuery("SELECT r FROM ReferenceReason r WHERE r.name = :reasonName", ReferenceReason.class)
        .setParameter("reasonName", reasonName)
        .getResultList().isEmpty()) {
      ReferenceReason referenceReason = new ReferenceReason();
      referenceReason.setName(reasonName);
      entityManager.persist(referenceReason);
    }
  }

  private void createTextTypeIfNotExists(String typeName) {
    if (entityManager.createQuery("SELECT t FROM TextType t WHERE t.textTypeName = :typeName", TextType.class)
        .setParameter("typeName", typeName)
        .getResultList().isEmpty()) {
      TextType textType = new TextType();
      textType.setTextTypeName(typeName);
      entityManager.persist(textType);
    }
  }

  private void createPropertyIfNotExists(String propertyName) {
    if (entityManager.createQuery("SELECT p FROM Property p WHERE p.name = :propertyName", Property.class)
        .setParameter("propertyName", propertyName)
        .getResultList().isEmpty()) {
      Property property = new Property();
      property.setName(propertyName);
      entityManager.persist(property);
    }
  }

  private void createGenderIfNotExists(String genderName) {
    if (entityManager.createQuery("SELECT g FROM Gender g WHERE g.genderName = :genderName", Gender.class)
        .setParameter("genderName", genderName)
        .getResultList().isEmpty()) {
      Gender gender = new Gender();
      gender.setGenderName(genderName);
      entityManager.persist(gender);
    }
  }

  private void createRoleIfNotExists(String roleName) {
    if (entityManager.createQuery("SELECT r FROM Role r WHERE r.roleName = :roleName", Role.class)
        .setParameter("roleName", roleName)
        .getResultList().isEmpty()) {
      Role role = new Role(roleName);
      entityManager.persist(role);
    }
  }

  private void createDepartmentIfNotExists(String departmentName) {
    if (entityManager.createQuery("SELECT d FROM Department d WHERE d.departmentName = :departmentName", Department.class)
        .setParameter("departmentName", departmentName)
        .getResultList().isEmpty()) {
      Department department = new Department();
      department.setDepartmentName(departmentName);
      entityManager.persist(department);
    }
  }

  private void createPositionIfNotExists(String positionName) {
    if (entityManager.createQuery("SELECT p FROM Position p WHERE p.positionName = :positionName", Position.class)
        .setParameter("positionName", positionName)
        .getResultList().isEmpty()) {
      Position position = new Position();
      position.setPositionName(positionName);
      entityManager.persist(position);
    }
  }

  private void createRandomEmployees(int count) {
    // Überprüfen, ob bereits Mitarbeiter in der Datenbank existieren
    long employeeCount = (long) entityManager.createQuery("SELECT COUNT(e) FROM Employee e").getSingleResult();
    if (employeeCount > 0) {
      // Es gibt bereits Mitarbeiter, also keine neuen hinzufügen
      return;
    }

    for (int i = 0; i < count; i++) {
      Employee employee = new Employee();
      employee.setEmployeeNumber(UUID.randomUUID().toString().substring(0, 8)); // Zufällige Mitarbeiternummer
      employee.setFirstName("Firstname" + i);
      employee.setLastName("Lastname" + i);
      employee.setDateOfBirth(LocalDate.now().minusYears(random.nextInt(40) + 18)); // Zufälliges Geburtsdatum zwischen 18 und 58 Jahren
      employee.setPhone("123-456-7890"); // Beispieltelefonnummer
      employee.setStartDate(LocalDate.now().minusYears(random.nextInt(10))); // Zufälliges Anfangsdatum der Beschäftigung in den letzten 10 Jahren

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

      entityManager.persist(employee);
    }
  }

  private Gender getRandomGender() {
    // Annahme: Alle vorhandenen Geschlechter abrufen
    List<Gender> genders = entityManager.createQuery("SELECT g FROM Gender g", Gender.class)
        .getResultList();

    if (!genders.isEmpty()) {
      return genders.get(random.nextInt(genders.size()));
    } else {
      return null;
    }
  }


  // Methode zum Abrufen einer zufälligen Abteilung aus der Datenbank
  private Department getRandomDepartment() {
    // Annahme: Alle vorhandenen Abteilungen abrufen
    List<Department> departments = entityManager.createQuery("SELECT d FROM Department d", Department.class)
        .getResultList();

    if (!departments.isEmpty()) {
      return departments.get(random.nextInt(departments.size()));
    } else {
      return null;
    }
  }

  // Methode zum Abrufen einer zufälligen Position aus der Datenbank
  private Position getRandomPosition() {
    // Annahme: Alle vorhandenen Positionen abrufen
    List<Position> positions = entityManager.createQuery("SELECT p FROM Position p", Position.class)
        .getResultList();

    if (!positions.isEmpty()) {
      return positions.get(random.nextInt(positions.size()));
    } else {
      return null;
    }
  }
}
