package com.umcsuser.carrent.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnectionManager {
    private static JdbcConnectionManager instance;

    private JdbcConnectionManager() {
    }

    public static JdbcConnectionManager getInstance() {
        if (instance == null) {
            instance = new JdbcConnectionManager();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            for (int i = 0; i < 3; i++) {
                try {
                    return DriverManager.getConnection(
                            "jdbc:postgresql://ep-sweet-pond-al4245wa.c-3.eu-central-1.aws.neon.tech/neondb?sslmode=require",
                            "neondb_owner",
                            "npg_8qam4FyhTQdb"
                    );
                } catch (SQLException e) {
                    System.out.println("Retry " + (i + 1));
                    Thread.sleep(1000);
                }
            }
            throw new RuntimeException("DB connection failed after retries");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}