package com.umcsuser.carrent.services.impl;

import com.umcsuser.carrent.models.Rental;
import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.models.Vehicle;
import com.umcsuser.carrent.repositories.RentalRepository;
import com.umcsuser.carrent.repositories.UserRepository;
import com.umcsuser.carrent.repositories.VehicleRepository;
import com.umcsuser.carrent.services.RentalServiceInterface;import jakarta.transaction.Transactional;import org.springframework.context.annotation.Profile;import org.springframework.stereotype.Repository;import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
@Profile({"json", "jdbc"})
@Transactional

public class OldRentalService implements RentalServiceInterface {
    private final RentalRepository rentalRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    public OldRentalService(RentalRepository rentalRepository, VehicleRepository vehicleRepository, UserRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    public Rental rentVehicle(String userId, String vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono pojazdu"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika"));

        Rental rental = Rental.builder()
                .id(java.util.UUID.randomUUID().toString())
                .user(user)
                .vehicle(vehicle)
                .rentDateTime(java.time.LocalDateTime.now().toString())
                .returnDateTime(null)
                .build();

       return rentalRepository.save(rental);
    }

    public Rental returnVehicle(String userId) {
        Rental rental = findActiveRentalByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("brak aktywnego wypozyczenia"));
        rental.setReturnDateTime(LocalDateTime.now().toString());
        return rentalRepository.save(rental);
    }

    public Optional<Rental> findActiveRentalByUserId(String userId) {
        return rentalRepository.findAll().stream()
                .filter(r -> r.getUserId().equals(userId) && r.isActive())
                .findFirst();
    }

    public List<Rental> findUserRentals(String userId) {
        return rentalRepository.findAll().stream()
                .filter(r -> r.getUserId().equals(userId))
                .toList();
    }

    @Override
    public boolean userHasActiveRental(String userId) {
        return false;
    }

    public List<Rental> findAllRentals() {
        return  rentalRepository.findAll();
    }

    public boolean vehicleHasActiveRental(String id) {
        return rentalRepository.findByVehicleIdAndReturnDateIsNull(id).isPresent();
    }
}
