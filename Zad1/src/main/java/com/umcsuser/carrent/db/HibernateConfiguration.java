package com.umcsuser.carrent.db;

import com.umcsuser.carrent.models.Rental;
import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.models.Vehicle;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateConfiguration {

    @Getter
    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();

            configuration.setProperty("hibernate.connection.url", System.getenv("DB_URL"));
            configuration.setProperty("hibernate.connection.username", "neondb_owner");
            configuration.setProperty("hibernate.connection.password", "npg_pgexINhv7wS9");
            configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
            configuration.setProperty("hibernate.connection.ssl", "true");
            configuration.setProperty("hibernate.connection.sslfactory", "org.postgresql.ssl.NonValidatingFactory");
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            configuration.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Vehicle.class);
            configuration.addAnnotatedClass(Rental.class);

            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("Initialize Hibernate ERROR: " + ex);
        }
    }

    private HibernateConfiguration() {}
}