package com.umcsuser.carrent.services;

import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.repositories.UserRepository;
import com.umcsuser.carrent.repositories.VehicleRepository;

import java.util.List;

public class UserService {
    private final UserValidator userValidator;
    private final UserRepository userRepository;

    public UserService(UserValidator userValidator, UserRepository userRepository) {
        this.userValidator = userValidator;
        this.userRepository = userRepository;
    }


    public User findById(String id) {
    }

    public void deleteUser(String s, String id) {
    }

    public List<User> findAllUsers() {
    }
}
