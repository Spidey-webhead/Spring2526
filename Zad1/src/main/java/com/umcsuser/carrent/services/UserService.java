package com.umcsuser.carrent.services;

import com.umcsuser.carrent.models.Rental;
import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.repositories.UserRepository;
import com.umcsuser.carrent.repositories.VehicleRepository;

import java.util.List;

public class UserService {
    private final UserRepository userRepository;
    private final RentalService rentalService;

    public UserService( UserRepository userRepository, RentalService rentalService) {
        this.userRepository = userRepository;
        this.rentalService = rentalService;
    }


    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono uzytkownika" + id));
    }

    public void deleteUser(String userToDelete, String currentId) {
        if (userToDelete.equals(currentId)) {
            throw new IllegalArgumentException("nie mozesz usunac samego siebie");
        }
            boolean hasActive = rentalService.findUserRentals(userToDelete).stream().anyMatch(Rental::isActive);

            if(hasActive){
                throw new IllegalArgumentException("nie mozna usunac uzytkownika z wypozyczeniem");
            }
            userRepository.deleteById(userToDelete);
    }


    public List<User> findAllUsers() {
        return  userRepository.findAll();
    }
}
