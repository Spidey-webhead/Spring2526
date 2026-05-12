package com.umcsuser.carrent.services.impl;

import com.umcsuser.carrent.models.Vehicle;
import com.umcsuser.carrent.services.VehicleServiceInterface;
import com.umcsuser.carrent.db.HibernateConfiguration;
import com.umcsuser.carrent.models.Vehicle;
import com.umcsuser.carrent.repositories.impl.RentalHibernateRepository;
import com.umcsuser.carrent.repositories.impl.VehicleHibernateRepository;
import com.umcsuser.carrent.services.VehicleServiceInterface;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class VehicleHibernateService implements VehicleServiceInterface {
    private final VehicleHibernateRepository vehicleRepo;
    private final RentalHibernateRepository rentalRepo;

    public VehicleHibernateService(VehicleHibernateRepository vehicleRepo, RentalHibernateRepository rentalRepo) {
        this.vehicleRepo = vehicleRepo;
        this.rentalRepo = rentalRepo;
    }

    @Override
    public List<Vehicle> findAllVehicles() {
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            vehicleRepo.setSession(session);
            return vehicleRepo.findAll();
        }
    }

    @Override
    public List<Vehicle> findAvailableVehicles() {
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            vehicleRepo.setSession(session);
            rentalRepo.setSession(session);
            return vehicleRepo.findAll().stream()
                    .filter(v -> rentalRepo.findByVehicleIdAndReturnDateIsNull(v.getId()).isEmpty())
                    .toList();
        }
    }

    @Override
    public Vehicle findById(String id) {
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            vehicleRepo.setSession(session);
            return vehicleRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono pojazdu: " + id));
        }
    }

    @Override
    public Vehicle addVehicle(Vehicle vehicle) {
        Transaction tx = null;
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            vehicleRepo.setSession(session);
            Vehicle saved = vehicleRepo.save(vehicle);
            tx.commit();
            return saved;
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public void removeVehicle(String vehicleId) {
        Transaction tx = null;
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            vehicleRepo.setSession(session);
            rentalRepo.setSession(session);

            if (rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent()) {
                throw new IllegalStateException("Nie można usunąć wypożyczonego pojazdu.");
            }

            vehicleRepo.deleteById(vehicleId);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public boolean isVehicleRented(String vehicleId) {
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            rentalRepo.setSession(session);
            return rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent();
        }
    }
}
