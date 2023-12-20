package com.ffhs.referencease.beans;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ffhs.referencease.dto.EmployeeDTO;
import com.ffhs.referencease.exceptionhandling.OperationResult;
import com.ffhs.referencease.services.interfaces.IDepartmentService;
import com.ffhs.referencease.services.interfaces.IEmployeeService;
import com.ffhs.referencease.services.interfaces.IGenderService;
import com.ffhs.referencease.services.interfaces.IPositionService;
import com.ffhs.referencease.services.interfaces.IReferenceLetterService;
import com.ffhs.referencease.services.interfaces.IReferenceReasonService;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class EmployeeBeanTest {


  @AfterEach
  void tearDown() {
  }

  @Mock
  private IEmployeeService employeeService;
  @Mock
  private IPositionService positionService;
  @Mock
  private IDepartmentService departmentService;
  @Mock
  private IGenderService genderService;
  @Mock
  private IReferenceLetterService referenceLetterService;

  @Mock
  private ReferenceLetterBean referenceLetterBean;

  @Mock
  private IReferenceReasonService referenceReasonService;

  private EmployeeBean employeeBean;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    referenceLetterBean = new ReferenceLetterBean(referenceLetterService, referenceReasonService,
                                                  employeeService);
    employeeBean = new EmployeeBean(employeeService, positionService, departmentService,
                                    genderService, referenceLetterService);
  }

  @Test
  void testSaveOrUpdateEmployee_NewEmployee() {
    EmployeeDTO newEmployee = new EmployeeDTO(); // Setzen Sie hier die erforderlichen Daten
    newEmployee.setEmployeeId(null); // Neue Mitarbeiter haben keine ID

    when(employeeService.saveOrUpdateEmployee(any(EmployeeDTO.class))).thenReturn(
        OperationResult.success(newEmployee));

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

    when(employeeService.saveOrUpdateEmployee(any(EmployeeDTO.class))).thenReturn(
        OperationResult.success(existingEmployee));

    employeeBean.setSelectedEmployee(existingEmployee);
    employeeBean.saveOrUpdateEmployee(referenceLetterBean);

    verify(employeeService).saveOrUpdateEmployee(existingEmployee);
    // Weitere Überprüfungen...
  }
}
