package com.ffhs.referencease.beans;

import jakarta.ejb.Singleton;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Named;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@Named
@Singleton
public class LoggerProducer {

  @Produces
  public Logger produceLogger(InjectionPoint injectionPoint) {
    return LoggerFactory.getLogger(injectionPoint.getMember()
        .getDeclaringClass().getName());
  }
}
