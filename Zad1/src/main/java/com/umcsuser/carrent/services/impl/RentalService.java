package com.umcsuser.carrent.services.impl;

import com.umcsuser.carrent.models.Rental;
import com.umcsuser.carrent.repositories.RentalRepository;
import com.umcsuser.carrent.repositories.VehicleRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class RentalService {
    private final RentalRepository rentalRepository;
    private final VehicleRepository vehicleRepository;

    public RentalService(RentalRepository rentalRepository, VehicleRepository vehicleRepository) {
        this.rentalRepository = rentalRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public void rentVehicle(String userId, String vehicleId) {
    if(findActiveRentalByUserId(userId).isPresent()){
        throw new IllegalStateException("uzytkownik ma juz wypozyczenie");
    }
    if(rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent()){
        throw new IllegalStateException("pojazd jest juz wypozyczony");
    }
    Rental rental = Rental.builder()
            .userId(userId)
            .vehicleId(vehicleId)
            .rentDateTime(LocalDateTime.now().toString())
            .build();
    rentalRepository.save(rental);
    }

    public void returnVehicle(String userId) {
        Rental rental = findActiveRentalByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("brak aktywnego wypozyczenia"));
        rental.setReturnDateTime(LocalDateTime.now().toString());
        rentalRepository.save(rental);
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

    public List<Rental> findAllRentals() {
        return  rentalRepository.findAll();
    }

    public Object vehicleHasActiveRental(String id) {
        return rentalRepository.findByVehicleIdAndReturnDateIsNull(id).isPresent();
    }
}
