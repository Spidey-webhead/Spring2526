package com.umcsuser.carrent.repositories.impl.jpa;

import com.umcsuser.carrent.models.Vehicle;
import com.umcsuser.carrent.repositories.VehicleJpaRepository;
import com.umcsuser.carrent.repositories.VehicleRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Profile("jpa")
public abstract class VehicleRepositoryJpaAdapter
        implements VehicleRepository {
    private final VehicleJpaRepository delegate;

    public VehicleRepositoryJpaAdapter(VehicleJpaRepository delegate) {
        this.delegate = delegate;
    }

    public List<Vehicle> findAll() {
        return delegate.findAll();
    }

    public Vehicle save(Vehicle vehicle) {
        if (vehicle.getId() == null) {
            vehicle.setId(UUID.randomUUID().toString());
        }
        return delegate.save(vehicle);
    }
}
