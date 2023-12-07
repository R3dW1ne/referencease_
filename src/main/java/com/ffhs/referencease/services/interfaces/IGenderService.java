package com.ffhs.referencease.services.interfaces;

import com.ffhs.referencease.dto.GenderDTO;
import com.ffhs.referencease.entities.Gender;
import java.util.List;
import java.util.UUID;

public interface IGenderService {

  Gender getGender(GenderDTO genderDTO);

  GenderDTO getGenderDTO(UUID id);

  List<Gender> getAllGenders();
  // Weitere Methoden nach Bedarf
}
