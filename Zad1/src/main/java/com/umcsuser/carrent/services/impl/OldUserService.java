package com.umcsuser.carrent.services.impl;

import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.repositories.RentalRepository;
import com.umcsuser.carrent.repositories.UserRepository;
import com.umcsuser.carrent.services.RentalServiceInterface;
import com.umcsuser.carrent.services.UserServiceInterface;

import java.util.List;

public class OldUserService implements UserServiceInterface {
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;
    private final RentalServiceInterface rentalService;

    public OldUserService(UserRepository userRepository,
                          RentalServiceInterface rentalService,
                          RentalRepository rentalRepository) {
        this.userRepository = userRepository;
        this.rentalService = rentalService;
        this.rentalRepository = rentalRepository;
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono uzytkownika: " + id));
    }

    @Override
    public void deleteUser(String id, String loggedUserId) {
        if (id.equals(loggedUserId)) {
            throw new IllegalArgumentException("Nie mozesz usunac samego siebie!");
        }

        if (userRentedVehicle(id)) {
            throw new IllegalArgumentException("Ten uzytkownik ma aktywne wypozyczenie!");
        }
        userRepository.deleteById(id);
    }

    public boolean userRentedVehicle(String id) {
        return rentalRepository.findByUserIdAndReturnDateIsNull(id).isPresent();
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}