package com.ffhs.referencease;

import com.ffhs.referencease.entities.Role;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Startup
@Singleton
public class AppInitSingleton {

  @PersistenceContext
  private EntityManager entityManager;

  @PostConstruct
  public void init() {
    Role user = new Role("User");
    entityManager.persist(user);
  }
}
