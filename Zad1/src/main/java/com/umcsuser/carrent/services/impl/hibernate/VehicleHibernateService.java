package com.umcsuser.carrent.services.impl.hibernate;

import com.umcsuser.carrent.models.Vehicle;
import com.umcsuser.carrent.services.VehicleServiceInterface;
import com.umcsuser.carrent.db.HibernateConfiguration;
import com.umcsuser.carrent.repositories.impl.hibernate.RentalHibernateRepository;
import com.umcsuser.carrent.repositories.impl.hibernate.VehicleHibernateRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;import org.springframework.context.annotation.Profile;import org.springframework.stereotype.Service;
import java.util.List;
@Service
@Profile("hibernate")
public class VehicleHibernateService implements VehicleServiceInterface {
    private final VehicleHibernateRepository vehicleRepo;
    private final RentalHibernateRepository rentalRepo;

    public VehicleHibernateService(VehicleHibernateRepository vehicleRepo, RentalHibernateRepository rentalRepo) {
        this.vehicleRepo = vehicleRepo;
        this.rentalRepo = rentalRepo;
    }

    @Override
    public List<Vehicle> findAllVehicles() {
        return vehicleRepo.findAll();
    }

    @Override
    public List<Vehicle> findAvailableVehicles() {
        return vehicleRepo.findAll().stream()
                .filter(v -> rentalRepo.findByVehicleIdAndReturnDateIsNull(v.getId()).isEmpty())
                .toList();
    }

    @Override
    public Vehicle findById(String id) {
        return vehicleRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono pojazdu: " + id));

    }

    @Override
    public Vehicle addVehicle(Vehicle vehicle) {
        return vehicleRepo.save(vehicle);
    }

    @Override
    public void removeVehicle(String vehicleId) {
        if (rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent()) {
            throw new IllegalStateException("Nie można usunąć wypożyczonego pojazdu.");
        }
        vehicleRepo.deleteById(vehicleId);
    }


    @Override
    public boolean isVehicleRented(String vehicleId) {
        return rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent();
    }
}


