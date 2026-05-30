package com.umcsuser.carrent.repositories.impl.hibernate;


import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
@Profile("hibernate")
public class UserHibernateRepository implements UserRepository {

   @PersistenceContext
   private EntityManager entityManager;

    @Override
    public List<User> findAll() {
        return entityManager.createQuery("FROM User", User.class).getResultList();    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(entityManager.find(User.class, id));    }

    @Override
    public Optional<User> findByLogin(String login) {
       return entityManager.createQuery("FROM User u WHERE u.login = :login", User.class)
               .setParameter("login", login)
               .getResultStream()
               .findFirst();

    }

    @Override
    public User save(User user) {
        return entityManager.merge(user);
    }

    @Override
    public void deleteById(String id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        }
    }
}
