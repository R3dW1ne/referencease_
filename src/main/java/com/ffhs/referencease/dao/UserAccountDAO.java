package com.ffhs.referencease.dao;

import com.ffhs.referencease.dao.interfaces.IUserAccountDAO;
import com.ffhs.referencease.entities.Role;
import com.ffhs.referencease.entities.UserAccount;
import com.ffhs.referencease.utils.PWHash;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Stateless
public class UserAccountDAO implements IUserAccountDAO {

  @PersistenceContext(unitName = "default")
  private EntityManager em;

  private static final String FIND_USER_BY_EMAIL_QUERY = "SELECT u FROM UserAccount u WHERE u.email = :email";
  private static final String EMAIL = "email";

  @Override
  public Optional<UserAccount> findById(UUID id) {
    return Optional.ofNullable(em.find(UserAccount.class, id));
  }

  @Override
  public Optional<UserAccount> findByEmail(String email) {
    TypedQuery<UserAccount> query = em.createQuery(FIND_USER_BY_EMAIL_QUERY, UserAccount.class);
    query.setParameter(EMAIL, email);
    List<UserAccount> results = query.getResultList();
    return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
  }


  @Override
  @Transactional
  public void save(UserAccount userAccount) {
    if (userAccount.getUserId() == null) {
      em.persist(userAccount);
    } else {
      em.merge(userAccount);
    }
  }

  @Override
  public boolean emailExists(String email) {
    if (email == null) {
      return false;
    }
    try {
      UserAccount userAccount = em.createQuery(FIND_USER_BY_EMAIL_QUERY, UserAccount.class)
          .setParameter(EMAIL, email).getSingleResult();
      return userAccount != null;
    } catch (NoResultException e) {
      return false;
    }
  }

  @Override
  @Transactional
  public boolean passwordMatches(String email, String password) {
    TypedQuery<UserAccount> query = em.createQuery(FIND_USER_BY_EMAIL_QUERY, UserAccount.class);
    query.setParameter(EMAIL, email);
    try {
      UserAccount userAccount = query.getSingleResult();
      return PWHash.checkPassword(password, userAccount.getPassword());
    } catch (NoResultException e) {
      return false;
    }
  }

  public void assignRolesToUser(String userEmail, Set<String> roleNames) {
    UserAccount user = em.createQuery(FIND_USER_BY_EMAIL_QUERY, UserAccount.class)
        .setParameter(EMAIL, userEmail)
        .getSingleResult();

    Set<Role> roles = user.getRoles();
    for (String roleName : roleNames) {
      Role role = em.createQuery("SELECT r FROM Role r WHERE r.roleName = :name", Role.class)
          .setParameter("name", roleName).getSingleResult();
      roles.add(role);
    }

    user.setRoles(roles);
    em.merge(user);
  }
}
