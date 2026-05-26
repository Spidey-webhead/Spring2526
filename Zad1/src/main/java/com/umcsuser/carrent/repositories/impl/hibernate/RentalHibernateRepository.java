package com.umcsuser.carrent.repositories.impl.hibernate;


import com.umcsuser.carrent.models.Rental;
import com.umcsuser.carrent.repositories.RentalRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;import org.springframework.context.annotation.Profile;import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
@Profile("hibernate")
public class RentalHibernateRepository implements RentalRepository {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Rental save(Rental rental) {
        return entityManager.merge(rental);
    }

    @Override
    public void deleteById(String id) {
        Rental rental = entityManager.find(Rental.class, id);
        if (rental != null) {
            entityManager.remove(rental);
        }
    }

    @Override
    public List<Rental> findAll() {
        return entityManager.createQuery("FROM Rental", Rental.class).getResultList();
    }

    @Override
    public Optional<Rental> findById(String id) {
        return Optional.ofNullable(entityManager.find(Rental.class, id));
    }

    @Override
    public Optional<Rental> findByVehicleIdAndReturnDateIsNull(String vehicleId) {
        Query<Rental> query = (Query<Rental>) entityManager.createQuery(
                "FROM Rental r WHERE r.vehicle.id = :vehicleId AND r.returnDateTime IS NULL", Rental.class);
        query.setParameter("vehicleId", vehicleId);
        return query.uniqueResultOptional();
    }

    @Override
    public Optional<Rental> findByUserIdAndReturnDateIsNull(String userId) {
        Query<Rental> query = (Query<Rental>) entityManager.createQuery(
                "FROM Rental r WHERE r.user.id = :userId AND r.returnDateTime IS NULL", Rental.class);
        query.setParameter("userId", userId);
        return query.uniqueResultOptional();
    }


}
