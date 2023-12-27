package com.ffhs.referencease.services;

import com.ffhs.referencease.dao.interfaces.IPositionDAO;
import com.ffhs.referencease.entities.Position;
import com.ffhs.referencease.services.interfaces.IPositionService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Stateless
public class PositionService implements IPositionService {

  private final IPositionDAO positionDao;

  @Inject
  public PositionService(IPositionDAO positionDao) {
    this.positionDao = positionDao;
  }

  @Override
  public Optional<Position> getPositionById(Long id) {
    return positionDao.find(id);
  }

  @Override
  public List<Position> getAllPositions() {
    return positionDao.findAll();
  }

  @Override
  public void createPositionIfNotExists(String positionName) {
    if (positionDao.findByName(positionName).isEmpty()) {
      Position position = new Position();
      position.setPositionName(positionName);
      positionDao.create(position);
    }
  }

  @Override
  public Position getRandomPosition(Random random) {
    List<Position> positions = getAllPositions();

    if (!positions.isEmpty()) {
      return positions.get(random.nextInt(positions.size()));
    } else {
      return null;
    }
  }


}
