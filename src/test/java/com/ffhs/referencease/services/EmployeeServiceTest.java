package com.ffhs.referencease.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ffhs.referencease.dao.interfaces.IEmployeeDAO;
import com.ffhs.referencease.dto.EmployeeDTO;
import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.exceptionhandling.OperationResult;
import com.ffhs.referencease.services.interfaces.IReferenceLetterService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

class EmployeeServiceTest {

  @Mock
  private IEmployeeDAO employeeDao;

  @Mock
  private IReferenceLetterService referenceLetterService;

  @Mock
  private ModelMapper modelMapper;

  @InjectMocks
  private EmployeeService employeeService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetEmployee() {
    UUID employeeId = UUID.randomUUID();
    Employee mockEmployee = new Employee();
    mockEmployee.setEmployeeId(employeeId);

    when(employeeDao.find(employeeId)).thenReturn(Optional.of(mockEmployee));

    EmployeeDTO result = employeeService.getEmployee(employeeId);

    assertNotNull(result);
    assertEquals(employeeId, result.getEmployeeId());
    verify(employeeDao).find(employeeId);
  }

  @Test
  void testGetAllEmployees() {
    // Mocken des Verhaltens der DAO
    when(employeeDao.findAll()).thenReturn(Arrays.asList(new Employee(), new Employee()));

    List<EmployeeDTO> result = employeeService.getAllEmployees();

    assertNotNull(result);
    assertEquals(2, result.size());
  }

  @Test
  void testCountEmployees() {
    when(employeeDao.countEmployees()).thenReturn(10L);

    long count = employeeService.countEmployees();

    assertEquals(10, count);
    verify(employeeDao).countEmployees();
  }


  @Test
  void testSaveOrUpdateEmployee_NewEmployee() {
    EmployeeDTO employeeDTO = new EmployeeDTO();
    employeeDTO.setEmployeeNumber("123");

    // Vorbereiten der Mocks
    when(employeeDao.findByEmployeeNumber(anyString())).thenReturn(Optional.empty());
    when(modelMapper.map(any(EmployeeDTO.class), eq(Employee.class))).thenReturn(new Employee());

    // Ausführen der zu testenden Methode
    OperationResult<EmployeeDTO> result = employeeService.saveOrUpdateEmployee(employeeDTO);

    // Überprüfen der Ergebnisse
    assertTrue(result.isSuccess());
    assertNotNull(result.getData());

    // Verifizieren, dass die save-Methode des DAO aufgerufen wurde
    verify(employeeDao).save(any(Employee.class));
  }

  @Test
  void testDeleteEmployee() {
    UUID employeeId = UUID.randomUUID();
    EmployeeDTO employeeDTO = new EmployeeDTO();
    employeeDTO.setEmployeeId(employeeId);

    doNothing().when(referenceLetterService).deleteReferenceLetter(any());
    doNothing().when(employeeDao).deleteById(any(UUID.class));

    employeeService.deleteEmployee(employeeDTO);

    verify(employeeDao).deleteById(employeeId);
  }

  @Test
  void testEmployeeNumberExists() {
    String employeeNumber = "12345";
    when(employeeDao.employeeNumberExists(employeeNumber)).thenReturn(true);

    boolean exists = employeeService.employeeNumberExists(employeeNumber);

    assertTrue(exists);
    verify(employeeDao).employeeNumberExists(employeeNumber);
  }
}