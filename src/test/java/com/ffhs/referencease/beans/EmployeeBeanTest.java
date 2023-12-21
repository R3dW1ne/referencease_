package com.ffhs.referencease.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ffhs.referencease.dto.EmployeeDTO;
import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.entities.ReferenceLetter;
import com.ffhs.referencease.exceptionhandling.DatabaseException;
import com.ffhs.referencease.exceptionhandling.OperationResult;
import com.ffhs.referencease.services.interfaces.IDepartmentService;
import com.ffhs.referencease.services.interfaces.IEmployeeService;
import com.ffhs.referencease.services.interfaces.IGenderService;
import com.ffhs.referencease.services.interfaces.IPositionService;
import com.ffhs.referencease.services.interfaces.IReferenceLetterService;
import com.ffhs.referencease.services.interfaces.IReferenceReasonService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;

class EmployeeBeanTest {


  @AfterEach
  void tearDown() {
  }

  @Mock
  private IEmployeeService employeeServiceMock;
  @Mock
  private IPositionService positionServiceMock;
  @Mock
  private IDepartmentService departmentServiceMock;
  @Mock
  private IGenderService genderServiceMock;
  @Mock
  private IReferenceLetterService referenceLetterServiceMock;
  @Mock
  private ReferenceLetterBean referenceLetterBeanMock;
  @Mock
  private IReferenceReasonService referenceReasonServiceMock;
  private EmployeeDTO employeeDTO;
  private final ModelMapper modelMapper = new ModelMapper();


  String actualMessage = "";

  private EmployeeBean employeeBeanSpy;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    referenceLetterBeanMock = new ReferenceLetterBean(referenceLetterServiceMock,
                                                      referenceReasonServiceMock,
                                                      employeeServiceMock);
    employeeDTO = new EmployeeDTO();
    employeeBeanSpy = spy(
        new EmployeeBean(employeeServiceMock, positionServiceMock, departmentServiceMock,
                         genderServiceMock, referenceLetterServiceMock));
    // Überwachen der sendInfoToFrontend-Methode
    doAnswer(new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        Object[] args = invocation.getArguments();
        actualMessage = (String) args[0];
        return null;
      }
    }).when(employeeBeanSpy).sendInfoToFrontend(anyString());
  }

  @Test
  void testSaveOrUpdateEmployee_NewEmployee_checkPrintMessage() {
    // Arrange
    employeeDTO = new EmployeeDTO();
    employeeDTO.setEmployeeId(null);// Neue Mitarbeiter haben keine ID
    employeeDTO.setFirstName("Max");
    employeeDTO.setLastName("Mustermann");
    actualMessage = "";
    when(employeeServiceMock.saveOrUpdateEmployee(any(EmployeeDTO.class))).thenReturn(
        OperationResult.success(employeeDTO));

    // Act
    employeeBeanSpy.setSelectedEmployee(employeeDTO);
    employeeBeanSpy.saveOrUpdateEmployee(referenceLetterBeanMock);
    // Assert
    assertEquals("Mitarbeiter Max Mustermann erfolgreich gespeichert.", actualMessage);
  }

  @Test
  void testSaveOrUpdateEmployee_NewEmployee_VerifyCallOf_employeeService_saveOrUpdateEmployee() {
    // Arrange
    employeeDTO = new EmployeeDTO();
    employeeDTO.setEmployeeId(null);// Neue Mitarbeiter haben keine ID
    when(employeeServiceMock.saveOrUpdateEmployee(any(EmployeeDTO.class))).thenReturn(
        OperationResult.success(employeeDTO));

    // Act
    employeeBeanSpy.setSelectedEmployee(employeeDTO);
    employeeBeanSpy.saveOrUpdateEmployee(referenceLetterBeanMock);

    // Assert
    verify(employeeServiceMock).saveOrUpdateEmployee(employeeDTO);
  }

  @Test
  void testSaveOrUpdateEmployee_NewEmployee_CheckChangeOfEditModeBoolean() {
    // Arrange
    employeeDTO = new EmployeeDTO();
    employeeDTO.setEmployeeId(null);// Neue Mitarbeiter haben keine ID
    when(employeeServiceMock.saveOrUpdateEmployee(any(EmployeeDTO.class))).thenReturn(
        OperationResult.success(employeeDTO));
    boolean expectedEditMode = true;

    // Act
    employeeBeanSpy.setSelectedEmployee(employeeDTO);
    employeeBeanSpy.saveOrUpdateEmployee(referenceLetterBeanMock);
    boolean actualEditMode = employeeBeanSpy.getEditMode();

    // Assert
    assertEquals(expectedEditMode, actualEditMode);
  }


  @Test
  void testSaveOrUpdateEmployee_NewEmployee_VerifyMethodCallWhen_listSelectionNeeded_IsTrue() {
    // Arrange
    EmployeeDTO newEmployeeDTO = new EmployeeDTO();
    employeeDTO.setEmployeeId(null);// Neue Mitarbeiter haben keine ID
    Employee newEmployee = modelMapper.map(newEmployeeDTO, Employee.class);
    ReferenceLetter referenceLetterMock = mock(ReferenceLetter.class);
    ReferenceLetterBean referenceLetterBeanMock = mock(ReferenceLetterBean.class);
    when(employeeServiceMock.saveOrUpdateEmployee(any(EmployeeDTO.class))).thenReturn(
        OperationResult.success(newEmployeeDTO));
    when(referenceLetterBeanMock.getReferenceLetter()).thenReturn(referenceLetterMock);
    when(employeeServiceMock.convertToEntity(any(EmployeeDTO.class))).thenReturn(newEmployee);

    // Act
    // Setzen der Bedingungen für den Test
    employeeBeanSpy.setListSelectionNeeded(true);
    employeeBeanSpy.setSelectedEmployee(newEmployeeDTO);
    // Ausführen der Methode, die getestet wird
    employeeBeanSpy.saveOrUpdateEmployee(referenceLetterBeanMock);

    // Assert
    verify(referenceLetterBeanMock).getReferenceLetter();
    verify(referenceLetterMock).setEmployee(newEmployee);
  }

  @Test
  void testSaveOrUpdateEmployee_NewEmployee_CheckEmployeeListsRefresh() {
    // Arrange
    employeeDTO = new EmployeeDTO();
    employeeDTO.setEmployeeId(null);// Neue Mitarbeiter haben keine ID
    employeeBeanSpy.setEmployees(List.of(new EmployeeDTO(), new EmployeeDTO()));
    employeeBeanSpy.setFilteredEmployees(List.of(new EmployeeDTO(), new EmployeeDTO()));
    List<EmployeeDTO> storedEmployees = new java.util.ArrayList<>(
        List.of(new EmployeeDTO(), new EmployeeDTO()));
    when(employeeServiceMock.saveOrUpdateEmployee(any(EmployeeDTO.class))).thenAnswer(
        invocation -> {
          EmployeeDTO employeeDTO = invocation.getArgument(0);
          storedEmployees.add(employeeDTO);
          return OperationResult.success(employeeDTO);
        });
    when(employeeServiceMock.getAllEmployees()).thenReturn(storedEmployees);
    int expectedSize = 3;

    // Act
    employeeBeanSpy.setSelectedEmployee(employeeDTO);
    employeeBeanSpy.saveOrUpdateEmployee(referenceLetterBeanMock);
    int actualSize = employeeBeanSpy.getEmployees().size();
    int actualFilteredSize = employeeBeanSpy.getFilteredEmployees().size();

    // Assert
    assertEquals(expectedSize, actualSize);
    assertEquals(expectedSize, actualFilteredSize);
  }

  @Test
  void testSaveOrUpdateEmployee_NewEmployee_checkPrintMessage_OnDatabaseException() {
    // Arrange
    employeeDTO = new EmployeeDTO();
    employeeDTO.setEmployeeId(null);// Neue Mitarbeiter haben keine ID
    employeeDTO.setFirstName("Max");
    employeeDTO.setLastName("Mustermann");
    actualMessage = "";
    when(employeeServiceMock.saveOrUpdateEmployee(any(EmployeeDTO.class))).thenThrow(
        new DatabaseException("Fehler beim Speichern des Mitarbeiters."));

    // Act
    employeeBeanSpy.setSelectedEmployee(employeeDTO);
    employeeBeanSpy.saveOrUpdateEmployee(referenceLetterBeanMock);
    // Assert
    assertEquals("Fehler beim Speichern des Mitarbeiters.", actualMessage);
  }

  @Test
  void testSaveOrUpdateEmployee_UpdateEmployee() {
    // Arrange
    employeeDTO = new EmployeeDTO();
    employeeDTO.setEmployeeId(UUID.randomUUID());// Neue Mitarbeiter haben keine ID
    employeeDTO.setFirstName("Max");
    employeeDTO.setLastName("Mustermann");
    actualMessage = "";
    when(employeeServiceMock.saveOrUpdateEmployee(any(EmployeeDTO.class))).thenReturn(
        OperationResult.success(employeeDTO));

    // Act
    employeeBeanSpy.setSelectedEmployee(employeeDTO);
    employeeBeanSpy.saveOrUpdateEmployee(referenceLetterBeanMock);

    // Assert
    verify(employeeServiceMock).saveOrUpdateEmployee(employeeDTO);
    assertEquals("Mitarbeiter Max Mustermann erfolgreich aktualisiert.", actualMessage);
  }


}
