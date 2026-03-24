package org.example.user;

import org.example.Vehicle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements IUserRepository {
    private List<User> users = new ArrayList<>();
    private static final String FILE = "users.csv";

    public UserRepositoryImpl() {
        load();
    }

    @Override
    public User getUser(String login) {
        for (User u : users) {
            if (u.getLogin().equals(login)) {
                return new User(
                        u.getLogin(),
                        u.getPassword(),
                        u.getRole(),
                        u.getRentedVehicleId()
                );
            }
        }


        return null;
    }

    @Override
    public List<User> getUsers() {
        List<User> copy = new ArrayList<>();
        for (User u : users) {

            copy.add(new User(
                    u.getLogin(),
                    u.getPassword(),
                    u.getRole(),
                    u.getRentedVehicleId()
            ));
        }
        return copy;
    }

    @Override
    public boolean update(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getLogin().equals(user.getLogin())) {
                users.set(i, user);
                save();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean add(User user) {
        if (getUser(user.getLogin()) != null) {
            return false;
        }
        users.add(user);
        save();
        return true;
    }

    @Override
    public boolean remove(String login) {
        User u = getUser(login);
        if (u != null && u.getRentedVehicleId() == null) {
            users.removeIf(user -> user.getLogin().equals(login));
            save();
            return true;
        }
        return false;
    }

    public void save() {
        try (PrintWriter writer = new PrintWriter((new FileWriter(FILE)))) {
            for (User u : users) {
                String vId = (u.getRentedVehicleId() == null) ? "null" : u.getRentedVehicleId();
                writer.println(u.getLogin() + ";" + u.getPassword() + ";" + u.getRole() + ";" + vId);
            }
        } catch (IOException e) {
            System.out.println("Blad zapisu" + e.getMessage());
        }

    }

    public void load() {
        users.clear();
        File file = new File(FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";", -1);
                if (data.length >= 4) {
                    String login = data[0];
                    String password = data[1];
                    String role = data[2];
                    String rentedId = data[3].equals("null") ? null : data[3];
                    users.add(new User(login, password, role, rentedId));
                }
            }
        } catch (IOException e) {
            System.out.println("blad odczytu" + e.getMessage());
        }

    }
}



