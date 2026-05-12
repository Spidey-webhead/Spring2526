package com.umcsuser.carrent.repositories.impl.jpa;

import com.umcsuser.carrent.models.Rental;
import com.umcsuser.carrent.repositories.RentalRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class RentalRepositoryJpaAdapter implements RentalRepository {

@Override
    public List<Rental> findAll() {
        return List.of();
    }@Override
    public Optional<Rental> findById(String id) {
        return Optional.empty();
    }@Override
    public Rental save(Rental rental) {
        return null;
    }@Override
    public void deleteById(String id) {

    }@Override
    public Optional<Rental> findByVehicleIdAndReturnDateIsNull(String vehicleId) {
        return Optional.empty();
    }@Override
    public Optional<Rental> findByUserIdAndReturnDateIsNull(String userId) {
        return Optional.empty();
    }}
