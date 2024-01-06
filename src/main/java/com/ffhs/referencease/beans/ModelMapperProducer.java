package com.ffhs.referencease.beans;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.modelmapper.ModelMapper;

/**
 * Produces a ModelMapper instance for dependency injection.
 */
@ApplicationScoped
public class ModelMapperProducer {

  /**
   * Creates a ModelMapper instance.
   *
   * @return The ModelMapper instance.
   */
  @Produces
  @ApplicationScoped
  public ModelMapper createModelMapper() {
    return new ModelMapper();
  }
}
