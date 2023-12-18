package com.ffhs.referencease.dao;

import com.ffhs.referencease.dao.interfaces.IPropertyDAO;
import com.ffhs.referencease.entities.Property;
import com.ffhs.referencease.producers.qualifiers.ProdPU;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Stateless
public class PropertyDAO implements IPropertyDAO {

    @PersistenceContext(unitName = "default")
    private EntityManager em;

    @Override
    public Optional<Property> findById(UUID id) {
        return Optional.ofNullable(em.find(Property.class, id));
    }

    @Override
    public Optional<Property> findByName(String name) {
        List<Property> results = em.createQuery("SELECT p FROM Property p WHERE p.propertyName = :name", Property.class)
            .setParameter("name", name)
            .getResultList();
        return results.stream().findFirst();
    }

    @Override
    public List<Property> findAll() {
        return em.createQuery("SELECT p FROM Property p", Property.class).getResultList();
    }

    @Override
    public void create(Property property) {
        em.persist(property);
    }

    // Weitere Methoden nach Bedarf
}
