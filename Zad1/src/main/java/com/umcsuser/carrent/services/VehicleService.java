package com.umcsuser.carrent.services;

import com.umcsuser.carrent.models.Vehicle;
import com.umcsuser.carrent.repositories.VehicleRepository;

import java.util.List;
import java.util.Map;

public class VehicleService {
    private final VehicleValidator vehicleValidator;
    private final VehicleRepository vehicleRepository;


    public VehicleService(VehicleValidator vehicleValidator, VehicleRepository vehicleRepository) {
        this.vehicleValidator = vehicleValidator;
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle addVehicle(Vehicle vehicle) {
        vehicleValidator.validate(vehicle);
        vehicleRepository.save(vehicle);
        return vehicle;
    }
    public List<Vehicle> findAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Object isVehicleRented(String id) {
        return


    }

    public Map<Object, Object> findAvailableVehicles(String id) {
        return
    }

    public Object findById(String vehicleId) {
        return vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Niepoprawne id" + vehicleId));
    }

    public void removeVehicle(String s) {

    }
}

