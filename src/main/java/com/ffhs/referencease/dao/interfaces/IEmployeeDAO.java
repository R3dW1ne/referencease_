package com.ffhs.referencease.dao.interfaces;

import com.ffhs.referencease.entities.Employee;
import com.ffhs.referencease.exceptionhandling.DatabaseException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface IEmployeeDAO {

  Optional<Employee> find(UUID id);

  Employee findByEmployeeId(UUID id);

  List<Employee> findAll();

  @Transactional
  Employee saveOrUpdateEmployee(Employee employee) throws DatabaseException;

  void save(Employee employee);

  void delete(Employee employee);

  Employee update(Employee employee);

  void deleteById(UUID id);

  boolean employeeNumberExists(String employeeNumber);

  boolean employeeIdExists(UUID uuid) throws DatabaseException;

  Optional<Employee> findByEmployeeNumber(String employeeNumber);

  Long countEmployees();
}

