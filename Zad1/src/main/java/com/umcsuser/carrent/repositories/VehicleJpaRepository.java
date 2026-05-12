package com.umcsuser.carrent.repositories;

import com.umcsuser.carrent.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleJpaRepository extends JpaRepository<Vehicle, String> {

}
