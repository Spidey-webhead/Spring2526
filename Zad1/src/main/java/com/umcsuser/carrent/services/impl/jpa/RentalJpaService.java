package com.umcsuser.carrent.services.impl.jpa;
import com.umcsuser.carrent.models.Rental;
import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.models.Vehicle;
import com.umcsuser.carrent.repositories.RentalRepository;
import com.umcsuser.carrent.repositories.UserRepository;
import com.umcsuser.carrent.repositories.VehicleRepository;
import com.umcsuser.carrent.services.RentalServiceInterface;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Profile("jpa")
@Transactional
public class RentalJpaService implements RentalServiceInterface {

    private final RentalRepository rentalRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    public RentalJpaService(RentalRepository rentalRepository, VehicleRepository vehicleRepository, UserRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Rental rentVehicle(String userId, String vehicleId) {
        if (userHasActiveRental(userId)) {
            throw new IllegalStateException("Użytkownik ma już aktywne wypożyczenie.");
        }

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono pojazdu."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika."));

        if (vehicleHasActiveRental(vehicleId)) {
            throw new IllegalStateException("Pojazd jest już wypożyczony.");
        }

        Rental rental = Rental.builder()
                .id(UUID.randomUUID().toString())
                .vehicle(vehicle)
                .user(user)
                .rentDateTime(LocalDateTime.now().toString())
                .build();

        return rentalRepository.save(rental);
    }

    @Override
    public Rental returnVehicle(String userId) {
        Rental rental = rentalRepository.findByUserIdAndReturnDateIsNull(userId)
                .orElseThrow(() -> new IllegalStateException("Brak aktywnego wypożyczenia."));

        rental.setReturnDateTime(LocalDateTime.now().toString());
        return rentalRepository.save(rental);
    }

    @Override
    public Optional<Rental> findActiveRentalByUserId(String userId) {
        return rentalRepository.findByUserIdAndReturnDateIsNull(userId);
    }

    @Override
    public List<Rental> findAllRentals() {
        return rentalRepository.findAll();
    }

    @Override
    public List<Rental> findUserRentals(String userId) {
        return rentalRepository.findAll().stream()
                .filter(r -> r.getUserId().equals(userId))
                .toList();
    }

    @Override
    public boolean userHasActiveRental(String userId) {
        return rentalRepository.findByUserIdAndReturnDateIsNull(userId).isPresent();
    }

    @Override
    public boolean vehicleHasActiveRental(String vehicleId) {
        return rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent();
    }
}