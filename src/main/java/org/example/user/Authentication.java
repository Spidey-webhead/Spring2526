package org.example.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class Authentication {
    private final IUserRepository userRepository;

    public Authentication(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User authenticateLogs(String login, String password) {
        User user = userRepository.getUser(login);
        if (user != null) {
            if (user.getPassword().equals(Hasher.hashPassword(password))) {
                return user;
            }
        }
        return null;
    }
}
