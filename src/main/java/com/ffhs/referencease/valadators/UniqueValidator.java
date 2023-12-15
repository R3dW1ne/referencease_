package com.ffhs.referencease.valadators;

import com.ffhs.referencease.annotations.Unique;
import com.ffhs.referencease.dao.interfaces.IUniqueDAO;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
public class UniqueValidator implements ConstraintValidator<Unique, Object> {

  private final IUniqueDAO uniqueDAO;

  private String fieldName;
  private Class<?> entityClass;

  @Inject
  public UniqueValidator(IUniqueDAO uniqueDAO) {
    this.uniqueDAO = uniqueDAO;
  }


  @Override
  public void initialize(Unique unique) {
    fieldName = unique.fieldName();
    entityClass = unique.entityClass();
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    if (value == null) {
      return true; // Null-Werte werden von anderen Validatoren behandelt
    }
    try {
      Query query = uniqueDAO.getEntityManager().createQuery("SELECT COUNT(e) FROM " + entityClass.getName() + " e WHERE e." + fieldName + " = :value");
      query.setParameter("value", value);
      long count = (long) query.getSingleResult();
      return count == 0;
    } catch (NoResultException e) {
      return true;
    }
  }
}
