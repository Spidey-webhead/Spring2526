package com.umcsuser.carrent.services.impl.hibernate;

import com.umcsuser.carrent.db.HibernateConfiguration;
import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.repositories.impl.hibernate.RentalHibernateRepository;
import com.umcsuser.carrent.repositories.impl.hibernate.UserHibernateRepository;
import com.umcsuser.carrent.services.UserServiceInterface;
import org.hibernate.Session;
import org.hibernate.Transaction;import org.springframework.context.annotation.Profile;import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
@Profile("hibernate")
public class UserHibernateService implements UserServiceInterface {
    private final UserHibernateRepository userRepo;
    private final RentalHibernateRepository rentalRepo;

    public UserHibernateService(UserHibernateRepository userRepo, RentalHibernateRepository rentalRepo) {
        this.userRepo = userRepo;
        this.rentalRepo = rentalRepo;
    }

    @Override
    public List<User> findAllUsers() {
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            userRepo.setSession(session);
            return userRepo.findAll();
        }
    }

    @Override
    public User findById(String id) {
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            userRepo.setSession(session);
            return userRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika: " + id));
        }
    }

    @Override
    public void deleteUser(String id, String loggedUserId) {
        Transaction tx = null;
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            userRepo.setSession(session);
            rentalRepo.setSession(session);

            if (rentalRepo.findByUserIdAndReturnDateIsNull(id).isPresent()) {
                throw new IllegalStateException("Użytkownik posiada aktywne wypożyczenie.");
            }

            userRepo.deleteById(id);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
}
