package com.ffhs.referencease.dao.interfaces;

import com.ffhs.referencease.entities.TextType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITextTypeDAO {

  Optional<TextType> findByName(String name);

  Optional<TextType> findById(UUID id);

  List<TextType> findAll();
}
