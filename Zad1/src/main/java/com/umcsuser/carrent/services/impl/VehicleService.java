package com.umcsuser.carrent.services.impl;

import com.umcsuser.carrent.models.Vehicle;
import com.umcsuser.carrent.repositories.RentalRepository;
import com.umcsuser.carrent.repositories.VehicleRepository;

import java.util.List;

public class VehicleService {
    private final VehicleValidator vehicleValidator;
    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;

    public VehicleService(VehicleValidator vehicleValidator, VehicleRepository vehicleRepository, RentalRepository rentalRepository) {
        this.vehicleValidator = vehicleValidator;
        this.vehicleRepository = vehicleRepository;
        this.rentalRepository = rentalRepository;
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
        return isVehicleCurrentRented(id);
    }

    public List<Vehicle> findAvailableVehicles() {
        return vehicleRepository.findAll().stream()
                .filter(vehicle -> !isVehicleCurrentRented(vehicle.getId()))
                .toList();
    }

    public Vehicle findById(String vehicleId) {
        return vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Niepoprawne id" + vehicleId));
    }

    public void removeVehicle(String id) {
      if(isVehicleCurrentRented(id)){
          throw new IllegalArgumentException("ten pojazd jest wypozyczony");
      }
      vehicleRepository.deleteById(id);
    }

    public boolean isVehicleCurrentRented(String id){
        return rentalRepository.findByVehicleIdAndReturnDateIsNull(id).isPresent();
    }
}

