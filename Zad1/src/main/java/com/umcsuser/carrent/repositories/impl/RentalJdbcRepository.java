package com.umcsuser.carrent.repositories.impl;

import com.umcsuser.carrent.db.JdbcConnectionManager;
import com.umcsuser.carrent.models.Rental;
import com.umcsuser.carrent.models.User;import com.umcsuser.carrent.models.Vehicle;import com.umcsuser.carrent.repositories.RentalRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RentalJdbcRepository implements RentalRepository {

    @Override
    public List<Rental> findAll() {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT id, vehicle_id, user_id, rent_date, return_date FROM rental";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                rentals.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Błąd podczas odczytu wypożyczeń", e);
        }
        return rentals;
    }

    @Override
    public Rental save(Rental rental) {
        Rental toSave = rental.copy();
        if (toSave.getId() == null || toSave.getId().isBlank()) {
            toSave.setId(UUID.randomUUID().toString());
        }

        String sql = "INSERT INTO rental (id, vehicle_id, user_id, rent_date, return_date) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET " +
                "return_date = EXCLUDED.return_date";

        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, toSave.getId());
            stmt.setString(2, toSave.getVehicleId());
            stmt.setString(3, toSave.getUserId());
            stmt.setString(4, toSave.getRentDateTime());
            stmt.setString(5, toSave.getReturnDateTime());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Błąd podczas zapisywania wypożyczenia", e);
        }
        return toSave;
    }

    @Override
    public Optional<Rental> findByVehicleIdAndReturnDateIsNull(String vehicleId) {
        String sql = "SELECT id, vehicle_id, user_id, rent_date, return_date FROM rental WHERE vehicle_id = ? AND return_date IS NULL";
        return findSingle(sql, vehicleId);
    }

    @Override
    public Optional<Rental> findByUserIdAndReturnDateIsNull(String userId) {
        String sql = "SELECT id, vehicle_id, user_id, rent_date, return_date FROM rental WHERE user_id = ? AND return_date IS NULL";
        return findSingle(sql, userId);
    }

    @Override
    public Optional<Rental> findById(String id) {
        String sql = "SELECT id, vehicle_id, user_id, rent_date, return_date FROM rental WHERE id = ?";
        return findSingle(sql, id);
    }

    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM rental WHERE id = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Błąd podczas usuwania wypożyczenia", e);
        }
    }

    private Optional<Rental> findSingle(String sql, String param) {
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, param);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Błąd bazy danych", e);
        }
        return Optional.empty();
    }

    private Rental mapRow(ResultSet rs) throws SQLException {
        Vehicle dummyVehicle = Vehicle.builder().id(rs.getString("vehicle_id")).build();
        User dummyUser = User.builder().id(rs.getString("user_id")).build();

        return Rental.builder()
                .id(rs.getString("id"))
                .vehicle(dummyVehicle)
                .user(dummyUser)
                .rentDateTime(rs.getString("rent_date"))
                .returnDateTime(rs.getString("return_date"))
                .build();
    }
}