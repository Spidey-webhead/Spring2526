package com.umcsuser.carrent.services;

import com.umcsuser.carrent.models.Rental;
import com.umcsuser.carrent.repositories.RentalRepository;

import java.util.List;
import java.util.Optional;

public class RentalService {
    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }


    public List<Rental> findUserRentals(String id) {

    }

    public void rentVehicle(String id, String s) {

    }

    public void returnVehicle(String id) {
    }

    public Optional<Object> findActiveRentalByUserId(String id) {
    }

    public List<Rental> findAllRentals() {
    }

    public Object vehicleHasActiveRental(String id) {
    }
}
