package com.umcsuser.carrent.repositories;

import com.umcsuser.carrent.models.Rental;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface RentalJpaRepository extends JpaRepository<Rental, String> {

    Optional<Rental> findByVehicle_IdAndReturnDateTimeIsNull(String vehicleId);

    Optional<Rental> findByUser_IdAndReturnDateTimeIsNull(String userId);
}


