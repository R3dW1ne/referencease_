package com.ffhs.referencease.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ffhs.referencease.dao.interfaces.IEmployeeDAO;
import com.ffhs.referencease.dto.EmployeeDTO;
import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.exceptionhandling.OperationResult;
import com.ffhs.referencease.services.interfaces.IDepartmentService;
import com.ffhs.referencease.services.interfaces.IGenderService;
import com.ffhs.referencease.services.interfaces.IPositionService;
import com.ffhs.referencease.services.interfaces.IReferenceLetterService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

class EmployeeServiceTest {

  @Mock
  private IEmployeeDAO employeeDao;

  @Mock
  private IReferenceLetterService referenceLetterService;
  @Mock
  private IDepartmentService departmentService;
  @Mock
  private IPositionService positionService;
  @Mock
  private IGenderService genderService;

  @Mock
  private ModelMapper modelMapper;

  private EmployeeService employeeService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    modelMapper = new ModelMapper();
    employeeService = new EmployeeService(employeeDao, referenceLetterService, departmentService,
                                          positionService, genderService, modelMapper);
  }

  @Test
  void testGetEmployee() {
    // Arrange
    UUID employeeId = UUID.randomUUID();
    EmployeeDTO mockEmployeeDTO = new EmployeeDTO();
    mockEmployeeDTO.setEmployeeId(employeeId);
    Employee mockEmployee = new Employee();
    mockEmployee.setEmployeeId(employeeId);

    when(employeeDao.find(employeeId)).thenReturn(Optional.of(mockEmployee));

    // Act
    EmployeeDTO result = employeeService.getEmployee(employeeId);

    // Assert
    assertNotNull(result);
    assertEquals(employeeId, result.getEmployeeId());
    verify(employeeDao).find(employeeId);
  }

  @Test
  void testGetAllEmployees() {
    // Arrange
    when(employeeDao.findAll()).thenReturn(Arrays.asList(new Employee(), new Employee()));

    // Act
    List<EmployeeDTO> result = employeeService.getAllEmployees();

    // Assert
    assertNotNull(result);
    assertEquals(2, result.size());
  }

  @Test
  void testCountEmployees() {
    // Arrange
    when(employeeDao.countEmployees()).thenReturn(10L);

    // Act
    long count = employeeService.countEmployees();

    // Assert
    assertEquals(10, count);
    verify(employeeDao).countEmployees();
  }

  @Test
  void testSaveOrUpdateEmployee_NewEmployee() {
    // Arrange
    EmployeeDTO employeeDTO = new EmployeeDTO();
    employeeDTO.setEmployeeNumber("123");

    when(employeeDao.findByEmployeeNumber(anyString())).thenReturn(Optional.empty());
    when(employeeDao.update(any(Employee.class))).thenReturn(new Employee());


    // Act
    OperationResult<EmployeeDTO> result = employeeService.saveOrUpdateEmployee(employeeDTO);

    // Assert
    assertTrue(result.isSuccess());
    assertNotNull(result.getData());
    verify(employeeDao).update(any(Employee.class));
  }

  @Test
  void testDeleteEmployee() {
    // Arrange
    UUID employeeId = UUID.randomUUID();
    EmployeeDTO employeeDTO = new EmployeeDTO();
    employeeDTO.setEmployeeId(employeeId);

    doNothing().when(referenceLetterService).deleteReferenceLetter(any());
    doNothing().when(employeeDao).deleteById(any(UUID.class));

    // Act
    employeeService.deleteEmployee(employeeDTO);

    // Assert
    verify(employeeDao).deleteById(employeeId);
  }

  @Test
  void testEmployeeNumberExists() {
    // Arrange
    String employeeNumber = "12345";
    when(employeeDao.employeeNumberExists(employeeNumber)).thenReturn(true);

    // Act
    boolean exists = employeeService.employeeNumberExists(employeeNumber);

    // Assert
    assertTrue(exists);
    verify(employeeDao).employeeNumberExists(employeeNumber);
  }
}