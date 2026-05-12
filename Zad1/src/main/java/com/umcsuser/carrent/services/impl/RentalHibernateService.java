package com.umcsuser.carrent.services.impl;

import com.umcsuser.carrent.db.HibernateConfiguration;
import com.umcsuser.carrent.models.Rental;
import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.models.Vehicle;
import com.umcsuser.carrent.repositories.impl.RentalHibernateRepository;
import com.umcsuser.carrent.repositories.impl.UserHibernateRepository;
import com.umcsuser.carrent.repositories.impl.VehicleHibernateRepository;
import com.umcsuser.carrent.services.RentalServiceInterface;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RentalHibernateService implements RentalServiceInterface {
    private final RentalHibernateRepository rentalRepo;
    private final VehicleHibernateRepository vehicleRepo;
    private final UserHibernateRepository userRepo;

    public RentalHibernateService(RentalHibernateRepository rentalRepo, VehicleHibernateRepository vehicleRepo, UserHibernateRepository userRepo) {
        this.rentalRepo = rentalRepo;
        this.vehicleRepo = vehicleRepo;
        this.userRepo = userRepo;
    }
    private void setSession(Session session) {
        rentalRepo.setSession(session);
        vehicleRepo.setSession(session);
        userRepo.setSession(session);
    }

    @Override
    public Rental rentVehicle(String userId, String vehicleId) {
        Transaction tx = null;
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            setSession(session);

            if (userHasActiveRental(userId)) {
                throw new IllegalStateException("Użytkownik ma już aktywne wypożyczenie.");
            }

            Vehicle vehicle = vehicleRepo.findById(vehicleId)
                    .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono pojazdu."));
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika."));

            if (vehicleHasActiveRental(vehicleId)) {
                throw new IllegalStateException("Pojazd jest już wypożyczony.");
            }

            Rental rental = Rental.builder()
                    .id(UUID.randomUUID().toString())
                    .vehicle(vehicle)
                    .user(user)
                    .rentDateTime(LocalDateTime.now().toString())
                    .build();

            Rental saved = rentalRepo.save(rental);
            tx.commit();
            return saved;
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public Rental returnVehicle(String userId) {
        Transaction tx = null;
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            setSession(session);

            Rental rental = rentalRepo.findByUserIdAndReturnDateIsNull(userId)
                    .orElseThrow(() -> new IllegalStateException("Brak aktywnego wypożyczenia."));

            rental.setReturnDateTime(LocalDateTime.now().toString());
            Rental saved = rentalRepo.save(rental);
            tx.commit();
            return saved;
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public Optional<Rental> findActiveRentalByUserId(String userId) {
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            rentalRepo.setSession(session);
            return rentalRepo.findByUserIdAndReturnDateIsNull(userId);
        }
    }

    @Override
    public List<Rental> findAllRentals() {
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            rentalRepo.setSession(session);
            return rentalRepo.findAll();
        }
    }

    @Override
    public List<Rental> findUserRentals(String userId) {
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            rentalRepo.setSession(session);
            return rentalRepo.findAll().stream()
                    .filter(r -> r.getUserId().equals(userId))
                    .toList();
        }
    }

    @Override
    public boolean userHasActiveRental(String userId) {
        return findActiveRentalByUserId(userId).isPresent();
    }

    @Override
    public boolean vehicleHasActiveRental(String vehicleId) {
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            rentalRepo.setSession(session);
            return rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent();
        }
    }
}
