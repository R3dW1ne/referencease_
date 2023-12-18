package com.ffhs.referencease.beans;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ffhs.referencease.dto.EmployeeDTO;
import com.ffhs.referencease.exceptionhandling.OperationResult;
import com.ffhs.referencease.services.interfaces.IEmployeeService;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class EmployeeBeanTest {



  @AfterEach
  void tearDown() {
  }

  @Mock
  private IEmployeeService employeeService;

  @Mock
  private ReferenceLetterBean referenceLetterBean;

  @InjectMocks
  private EmployeeBean employeeBean;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testSaveOrUpdateEmployee_NewEmployee() {
    EmployeeDTO newEmployee = new EmployeeDTO(); // Setzen Sie hier die erforderlichen Daten
    newEmployee.setEmployeeId(null); // Neue Mitarbeiter haben keine ID

    when(employeeService.saveOrUpdateEmployee(any(EmployeeDTO.class)))
        .thenReturn(OperationResult.success(newEmployee));

    employeeBean.setSelectedEmployee(newEmployee);
    employeeBean.saveOrUpdateEmployee(referenceLetterBean);

    verify(employeeService).saveOrUpdateEmployee(newEmployee);
    // Weitere Überprüfungen...
  }

  @Test
  void testSaveOrUpdateEmployee_UpdateEmployee() {
    EmployeeDTO existingEmployee = new EmployeeDTO();
    // Setzen Sie hier die erforderlichen Daten
    existingEmployee.setEmployeeId(UUID.randomUUID()); // Bestehende Mitarbeiter haben eine ID

    when(employeeService.saveOrUpdateEmployee(any(EmployeeDTO.class)))
        .thenReturn(OperationResult.success(existingEmployee));

    employeeBean.setSelectedEmployee(existingEmployee);
    employeeBean.saveOrUpdateEmployee(referenceLetterBean);

    verify(employeeService).saveOrUpdateEmployee(existingEmployee);
    // Weitere Überprüfungen...
  }
}
