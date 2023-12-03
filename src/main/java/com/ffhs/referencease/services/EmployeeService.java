package com.ffhs.referencease.services;

import com.ffhs.referencease.dao.dao_interfaces.IEmployeeDAO;
import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.services.service_interfaces.IEmployeeService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Stateless
public class EmployeeService implements IEmployeeService {


//  @Inject
//  private IEmployeeDAO employeeDao;

  private final IEmployeeDAO employeeDao;

  @Inject
  public EmployeeService(IEmployeeDAO employeeDao) {
    this.employeeDao = employeeDao;
  }

  @Override
  public Optional<Employee> getEmployee(UUID id) {
    return employeeDao.find(id);
  }

  @Override
  public List<Employee> getAllEmployees() {
    return employeeDao.findAll();
  }

  @Override
  public void saveEmployee(Employee employee) {
    employeeDao.save(employee);
  }

  @Override
  public void deleteEmployee(Employee employee) {
    employeeDao.delete(employee);
  }

  @Override
  public Employee updateEmployee(Employee employee) {
    return employeeDao.update(employee);
  }
}
