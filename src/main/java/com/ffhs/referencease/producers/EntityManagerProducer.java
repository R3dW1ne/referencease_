package com.ffhs.referencease.producers;

import com.ffhs.referencease.utils.PU_Name;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@ApplicationScoped
public class EntityManagerProducer {
  private EntityManagerFactory emf;

  @PostConstruct
  public void init() {
    String puName = PU_Name.getPU_Name();
    emf = Persistence.createEntityManagerFactory(puName);
  }

  @Produces
  public EntityManager createEntityManager() {
    return emf.createEntityManager();
  }

  @PreDestroy
  public void close() {
    if (emf != null && emf.isOpen()) {
      emf.close();
    }
  }
}

