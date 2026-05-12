package com.umcsuser.carrent.repositories.impl;

import com.umcsuser.carrent.models.Vehicle;
import com.umcsuser.carrent.repositories.VehicleRepository;
import org.hibernate.Session;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
@Profile("hibernate")
public class VehicleHibernateRepository implements VehicleRepository {

    private Session session;

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        return session.merge(vehicle);
    }

    @Override
    public void deleteById(String id) {
        Vehicle vehicle = session.get(Vehicle.class, id);
        if (vehicle != null) {
            session.remove(vehicle);
        }
    }

    @Override
    public List<Vehicle> findAll() {
        return session.createQuery("FROM Vehicle", Vehicle.class).list();
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return Optional.ofNullable(session.get(Vehicle.class, id));
    }
}
