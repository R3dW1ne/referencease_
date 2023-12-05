package com.ffhs.referencease.services;

import com.ffhs.referencease.dao.interfaces.IPositionDAO;
import com.ffhs.referencease.entities.Position;
import com.ffhs.referencease.services.interfaces.IPositionService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

@Stateless
public class PositionService implements IPositionService {

//  @Inject
//  private IPositionDAO positionDao;

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


}
