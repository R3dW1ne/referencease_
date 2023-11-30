package com.ffhs.referencease.converters;

import com.ffhs.referencease.entities.Department;
import com.ffhs.referencease.services.service_interfaces.IDepartmentService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

//@FacesConverter(forClass = Department.class)
@FacesConverter(value = "departmentConverter", managed = true)
public class DepartmentConverter implements Converter<Department> {

  @Inject
  private IDepartmentService departmentService;

//  private final IDepartmentService departmentService;
//
//  @Inject
//  public DepartmentConverter(IDepartmentService departmentService) {
//    this.departmentService = departmentService;
//  }

  @Override
  public Department getAsObject(FacesContext context, UIComponent component, String value) {
    if (value == null || value.isEmpty()) {
      return null;
    }
    return departmentService.getDepartmentById(Long.parseLong(value)).orElse(null);
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component, Department value) {
    if (value == null) {
      return "";
    }
    return String.valueOf(value.getDepartmentId());
  }
}
