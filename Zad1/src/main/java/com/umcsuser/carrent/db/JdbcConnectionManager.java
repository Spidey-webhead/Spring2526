package com.umcsuser.carrent.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnectionManager {

    private final String url;

    private static class InstanceHolder {
        private static final JdbcConnectionManager INSTANCE = new JdbcConnectionManager();
    }

    public static JdbcConnectionManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private JdbcConnectionManager() {
        url = System.getenv("DB_URL");

        if (url == null || url.trim().isEmpty()) {
            throw new IllegalStateException("Zmienna środowiskowa DB_URL nie jest ustawiona.");
        }
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException("Nie udało się nawiązać połączenia z bazą danych.", e);
        }
    }
}