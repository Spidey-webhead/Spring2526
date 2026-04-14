package com.umcsuser.carrent.services;

import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class AuthService {
    private final UserRepository userRepo;

    public AuthService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public Optional<User> login(String login, String password){
        return userRepo.findByLogin(login)
                .filter(user -> BCrypt.checkpw(password, user.getPasswordHash()))
                .map(User::copy);
    }

    public boolean register(String s, String s1) {
    }
}
