package com.ffhs.referencease.services.interfaces;

import com.ffhs.referencease.entities.Position;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public interface IPositionService {
  Optional<Position> getPositionById(Long id);
  List<Position> getAllPositions();
  void createPositionIfNotExists(String positionName);

  Position getRandomPosition(Random random);
}
