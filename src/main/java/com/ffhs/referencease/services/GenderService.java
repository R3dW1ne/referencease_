package com.ffhs.referencease.services;


import com.ffhs.referencease.dao.interfaces.IGenderDAO;
import com.ffhs.referencease.dto.GenderDTO;
import com.ffhs.referencease.entities.Gender;
import com.ffhs.referencease.services.interfaces.IGenderService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;

@Stateless
public class GenderService implements IGenderService {


  private final IGenderDAO genderDAO;

  @Inject
  public GenderService(IGenderDAO genderDAO) {
    this.genderDAO = genderDAO;
  }

  private final ModelMapper modelMapper = new ModelMapper();

  @Override
  public Gender getGender(GenderDTO genderDTO) {
    return modelMapper.map(genderDTO, Gender.class);
  }

  @Override
  public GenderDTO getGenderDTO(UUID id) {
    return genderDAO.getGenderById(id)
        .map(gender -> modelMapper.map(gender, GenderDTO.class))
        .orElse(null);
  }

  @Override
  public List<Gender> getAllGenders() {
    // Hier rufen Sie alle Geschlechter aus der Datenbank ab und konvertieren sie in DTOs
    return genderDAO.getAllGenders();
  }

  @Override
  public void createGenderIfNotExists(String displayName) {
    if (genderDAO.findByName(displayName).isEmpty()){
      Gender gender = new Gender();
      gender.setGenderName(displayName);
      genderDAO.create(gender);
    }
  }

//  @Override
//  public List<GenderDTO> getAllGenders() {
//    // Hier rufen Sie alle Geschlechter aus der Datenbank ab und konvertieren sie in DTOs
//    return genderDAO.getAllGenders().stream()
//        .map(gender -> modelMapper.map(gender, GenderDTO.class))
//        .collect(Collectors.toList());
//  }

  // Implementierung weiterer Methoden
}
