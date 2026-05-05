package com.umcsuser.carrent.services.impl;

import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.repositories.RentalRepository;
import com.umcsuser.carrent.repositories.UserRepository;

import java.util.List;

public class UserService {
    private final UserRepository userRepository;
    private final RentalService rentalService;
    private final RentalRepository rentalRepository;


    public UserService(UserRepository userRepository, RentalService rentalService, RentalRepository rentalRepository) {
        this.userRepository = userRepository;
        this.rentalService = rentalService;
        this.rentalRepository = rentalRepository;
    }


    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono uzytkownika" + id));
    }


    public void deleteUser(String id) {
        if(userRentedVehicle(id)){
            throw new IllegalArgumentException("ten user ma wypozyczenie");
        }
        userRepository.deleteById(id);
    }

    public boolean userRentedVehicle(String id){
        return rentalRepository.findByUserIdAndReturnDateIsNull(id).isPresent();
    }

    public List<User> findAllUsers() {
        return  userRepository.findAll();
    }
}
