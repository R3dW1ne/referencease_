package com.ffhs.referencease.dao;

import com.ffhs.referencease.dao.interfaces.IUserAccountDAO;
import com.ffhs.referencease.entities.Role;
import com.ffhs.referencease.entities.UserAccount;
import com.ffhs.referencease.utils.PBKDF2Hash;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Stateless
public class UserAccountDAO implements IUserAccountDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Optional<UserAccount> findById(UUID id) {
    return Optional.ofNullable(entityManager.find(UserAccount.class, id));
  }

  @Override
  public Optional<UserAccount> findByEmail(String email) {
    TypedQuery<UserAccount> query = entityManager.createQuery(
        "SELECT u FROM UserAccount u WHERE u.email = :email", UserAccount.class);
    query.setParameter("email", email);
    try {
      UserAccount userAccount = query.getSingleResult();
      return Optional.of(userAccount);
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }


  @Override
  @Transactional
  public void save(UserAccount userAccount) {
    if (userAccount.getUserId() == null) {
      entityManager.persist(userAccount);
    } else {
      entityManager.merge(userAccount);
    }
  }

  @Override
  public boolean emailExists(String email) {
    TypedQuery<Long> query = entityManager.createQuery(
        "SELECT COUNT(u) FROM UserAccount u WHERE u.email = :email", Long.class);
    query.setParameter("email", email);
    Long count = query.getSingleResult();
    return count > 0;
  }

  @Override
  @Transactional
  public boolean passwordMatches(String email, String password) {
    TypedQuery<UserAccount> query = entityManager.createQuery(
        "SELECT u FROM UserAccount u WHERE u.email = :email", UserAccount.class);
    query.setParameter("email", email);
    try {
      UserAccount userAccount = query.getSingleResult();
      return PBKDF2Hash.checkPassword(password, userAccount.getPassword());
    } catch (NoResultException e) {
      return false;
    }
  }

  public void assignRolesToUser(String userEmail, Set<String> roleNames) {
    UserAccount user = entityManager.createQuery("SELECT u FROM UserAccount u WHERE u.email = :email", UserAccount.class)
        .setParameter("email", userEmail)
        .getSingleResult();

    Set<Role> roles = user.getRoles();
    for (String roleName : roleNames) {
      Role role = entityManager.createQuery("SELECT r FROM Role r WHERE r.roleName = :name", Role.class)
          .setParameter("name", roleName)
          .getSingleResult();
      roles.add(role);
    }

    user.setRoles(roles);
    entityManager.merge(user);
  }
}
