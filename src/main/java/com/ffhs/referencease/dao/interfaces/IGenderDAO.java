package com.ffhs.referencease.dao.interfaces;

import com.ffhs.referencease.entities.Gender;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IGenderDAO {

  Optional<Gender> getGenderById(UUID genderId);

  List<Gender> getAllGenders();

  void saveGender(Gender gender);

  void updateGender(Gender gender);

  void deleteGender(UUID genderId);

  Optional<Gender> findByName(String genderName);

  void create(Gender gender);
}
