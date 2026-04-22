package com.umcsuser.carrent.repositories.impl;

import com.google.gson.reflect.TypeToken;
import com.umcsuser.carrent.db.JsonFileStorage;
import com.umcsuser.carrent.models.Rental;
import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.models.Vehicle;
import com.umcsuser.carrent.repositories.RentalRepository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RentalJsonRepository implements RentalRepository {
    private final List<Rental> rentals;
    private final JsonFileStorage<Rental> storage = new JsonFileStorage<>("rentals.json",
            new TypeToken<List<Rental>>() {}.getType());
    public RentalJsonRepository(){
        this.rentals = new ArrayList<>(storage.load());
    }
    @Override
    public List<Rental> findAll() {
        return rentals.stream().map(Rental::copy).toList();
    }

    @Override
    public Optional<Rental> findById(String id) {
        return  rentals.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .map(Rental::copy);
    }

    @Override
    public Rental save(Rental rental) {
        Rental toSave = rental.copy();
        if (toSave.getId() == null || toSave.getId().isBlank()) {
            toSave.setId(UUID.randomUUID().toString());
        }
        rentals.removeIf(u -> u.getId().equals(toSave.getId()));
        rentals.add(toSave);
        storage.save(rentals);
        return toSave.copy();
    }

    @Override
    public void deleteById(String id) {
        rentals.removeIf(vehicle -> vehicle.getId().equals(id));
        storage.save(rentals);
    }

    @Override
    public Optional<Rental> findByVehicleIdAndReturnDateIsNull(String vehicleId) {
        return rentals.stream()
                .filter(rental -> rental.getVehicleId().equals(vehicleId) && rental.isActive())
                .findFirst()
                .map(Rental::copy);
    }
    public Optional<Rental> findByUserIdAndReturnDateIsNull(String userId) {
        return rentals.stream()
                .filter(rental -> rental.getUserId().equals(userId) && rental.isActive())
                .findFirst()
                .map(Rental::copy);
    }


}
