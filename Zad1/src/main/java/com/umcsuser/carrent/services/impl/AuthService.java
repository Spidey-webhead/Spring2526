package com.umcsuser.carrent.services.impl;

import com.umcsuser.carrent.models.Role;
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

    public boolean register(String login, String password) {
        if(userRepo.findByLogin(login).isPresent()) {
            return false;
        }
            String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
            User newUser = User.builder()
                    .login(login)
                    .passwordHash(hashed)
                    .role(Role.USER)
                    .build();
            userRepo.save(newUser);
            return true;
        }
    }

