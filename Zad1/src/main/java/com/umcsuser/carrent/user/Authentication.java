package com.umcsuser.carrent.user;

import com.umcsuser.carrent.models.User;

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
