package com.umcsuser.carrent.services.impl.jpa;

import com.umcsuser.carrent.models.Vehicle;
import com.umcsuser.carrent.repositories.RentalRepository;
import com.umcsuser.carrent.repositories.VehicleRepository;
import com.umcsuser.carrent.services.VehicleServiceInterface;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Profile("jpa")
@Transactional
public class VehicleJpaService implements VehicleServiceInterface {

    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;

    public VehicleJpaService(VehicleRepository vehicleRepository, RentalRepository rentalRepository) {
        this.vehicleRepository = vehicleRepository;
        this.rentalRepository = rentalRepository;
    }

    @Override
    public List<Vehicle> findAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    public List<Vehicle> findAvailableVehicles() {
        return vehicleRepository.findAll().stream()
                .filter(v -> rentalRepository.findByVehicleIdAndReturnDateIsNull(v.getId()).isEmpty())
                .toList();
    }

    @Override
    public Vehicle findById(String id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono pojazdu: " + id));
    }

    @Override
    public Vehicle addVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @Override
    public void removeVehicle(String vehicleId) {
        if (rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent()) {
            throw new IllegalStateException("Nie można usunąć wypożyczonego pojazdu.");
        }
        vehicleRepository.deleteById(vehicleId);
    }

    @Override
    public boolean isVehicleRented(String vehicleId) {
        return rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent();
    }
}
