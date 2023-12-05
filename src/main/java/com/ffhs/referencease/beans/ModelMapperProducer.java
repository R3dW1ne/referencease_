package com.ffhs.referencease.beans;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.modelmapper.ModelMapper;

@ApplicationScoped
public class ModelMapperProducer {

  @Produces
  @ApplicationScoped
  public ModelMapper createModelMapper() {
    return new ModelMapper();
  }
}
