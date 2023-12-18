package com.ffhs.referencease.dao.interfaces;

import com.ffhs.referencease.entities.Position;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IPositionDAO {

  Optional<Position> find(Long id);

  List<Position> findAll();

  Optional<Position> findByName(String positionName);

  void create(Position position);
}

