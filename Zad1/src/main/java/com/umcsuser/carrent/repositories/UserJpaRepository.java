package com.umcsuser.carrent.repositories;

import com.umcsuser.carrent.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, String> {
    Optional<User> findByLogin(String login);
}
