//package com.ffhs.referencease.converters.jsf;
//
//import com.ffhs.referencease.dto.EmployeeDTO;
//import com.ffhs.referencease.services.interfaces.IEmployeeService;
//import jakarta.faces.component.UIComponent;
//import jakarta.faces.context.FacesContext;
//import jakarta.faces.convert.Converter;
//import jakarta.faces.convert.FacesConverter;
//import jakarta.inject.Inject;
//import java.util.UUID;
//
//
//@FacesConverter("uuidToEmployeeDTOConverter")
//public class UUIDToEmployeeDTOConverter implements Converter {
//
//  @Inject
//  private IEmployeeService employeeService;
//
//  @Override
//  public Object getAsObject(FacesContext context, UIComponent component, String value) {
//    if (value == null || value.isEmpty()) {
//      return null;
//    }
//    try {
//      UUID uuid = UUID.fromString(value);
//      if (uuid != null) {
//        // Hier rufen Sie Ihre Service-Methode auf, um den Mitarbeiter anhand der UUID zu erhalten
//        // In diesem Fall gehe ich davon aus, dass Sie die Methode employeeService.getEmployeeDTOByUUID(UUID uuid) haben
//        return employeeService.getEmployee(uuid);
//      }
//    } catch (IllegalArgumentException e) {
//      // Behandeln Sie die Ausnahme, wenn die UUID nicht geparst werden kann
//    }
//    return null;
//  }
//
//
//  @Override
//  public String getAsString(FacesContext context, UIComponent component, Object value) {
//    if (value instanceof EmployeeDTO) {
//      return ((EmployeeDTO) value).getEmployeeId().toString();
//    }
//    return null;
//  }
//}
