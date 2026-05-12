package com.umcsuser.carrent.repositories.impl.json;

import com.google.gson.reflect.TypeToken;
import com.umcsuser.carrent.db.JsonFileStorage;
import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.repositories.UserRepository;import org.springframework.context.annotation.Profile;import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
@Profile("json")
public class UserJsonRepository implements UserRepository {

    private List<User> users;
    private final JsonFileStorage<User> storage =
            new JsonFileStorage<>("users.json",
                    new TypeToken<List<User>>() {}.getType());


    public UserJsonRepository(){
        this.users = new ArrayList<>(storage.load());
    }
    @Override
    public List<User> findAll() {
        return users.stream().map(User::copy).toList();
    }

    @Override
    public Optional<User> findById(String id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .map(User::copy);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return users.stream()
                .filter(u -> u.getLogin().equals(login))
                .findFirst()
                .map(User::copy);
    }

    @Override
    public User save(User user) {
        User toSave = user.copy();
        if (toSave.getId() == null || toSave.getId().isBlank()) {
            toSave.setId(UUID.randomUUID().toString());
        }
        users.removeIf(u -> u.getId().equals(toSave.getId()));
        users.add(toSave);
        storage.save(users);
        return toSave.copy();
    }

    @Override
    public void deleteById(String id) {
       users.removeIf(u -> u.getId().equals(id));
        storage.save(users);
    }
}
