package com.umcsuser.carrent.services.impl.jpa;

import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.repositories.RentalRepository;
import com.umcsuser.carrent.repositories.UserRepository;
import com.umcsuser.carrent.services.UserServiceInterface;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
@Profile("jpa")
@Transactional
public class UserJpaService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;

    public UserJpaService(UserRepository userRepository, RentalRepository rentalRepository) {
        this.userRepository = userRepository;
        this.rentalRepository = rentalRepository;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika: " + id));
    }

    @Override
    public void deleteUser(String id, String loggedUserId) {
        if (rentalRepository.findByUserIdAndReturnDateIsNull(id).isPresent()) {
            throw new IllegalStateException("Użytkownik posiada aktywne wypożyczenie.");
        }
        userRepository.deleteById(id);
    }
}