package com.ffhs.referencease.dao.dao_interfaces;

import com.ffhs.referencease.entities.Position;
import java.util.List;
import java.util.Optional;

public interface IPositionDAO {

  Optional<Position> find(Long id);

  List<Position> findAll();
}

