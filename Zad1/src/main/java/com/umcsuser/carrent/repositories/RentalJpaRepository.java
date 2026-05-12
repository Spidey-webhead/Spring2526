package com.umcsuser.carrent.repositories;

import com.umcsuser.carrent.models.Rental;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface RentalJpaRepository extends JpaRepository<Rental, String> {

    Optional<Rental> findVehicle_IdAndReturnDateTimeIsNull(String vehicleId);
}

