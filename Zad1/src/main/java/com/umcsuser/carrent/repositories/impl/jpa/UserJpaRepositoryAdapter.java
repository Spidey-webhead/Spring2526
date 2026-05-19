package com.umcsuser.carrent.repositories.impl.jpa;

import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.repositories.UserJpaRepository;
import com.umcsuser.carrent.repositories.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
@Profile("jpa")
public class UserJpaRepositoryAdapter implements UserRepository {
    private final UserJpaRepository delegate;

    public UserJpaRepositoryAdapter(UserJpaRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.empty();
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }
}
