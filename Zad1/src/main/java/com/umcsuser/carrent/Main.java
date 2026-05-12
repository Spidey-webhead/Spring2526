package com.umcsuser.carrent;

import com.umcsuser.carrent.db.HibernateConfiguration;
import com.umcsuser.carrent.repositories.*;
import com.umcsuser.carrent.repositories.impl.*;
import com.umcsuser.carrent.services.*;
import com.umcsuser.carrent.services.impl.*;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository;
        VehicleRepository vehicleRepository;
        RentalRepository rentalRepository;

        AuthServiceInterface authService;
        RentalServiceInterface rentalService;
        VehicleServiceInterface vehicleService;
        UserServiceInterface userService;

        VehicleCategoryRepository categoryRepository = new VehicleCategoryConfigJsonRepository();
        VehicleCategoryService categoryService = new VehicleCategoryService(categoryRepository);
        VehicleValidator vehicleValidator = new VehicleValidator(categoryService);

        String mode = args.length > 0 ? args[0].toLowerCase() : "json";

        if (mode.equals("hibernate")) {
            System.out.println("HIBERNATE");
            HibernateConfiguration.getSessionFactory();

            UserHibernateRepository userRepo = new UserHibernateRepository();
            VehicleHibernateRepository vehicleRepo = new VehicleHibernateRepository();
            RentalHibernateRepository rentalRepo = new RentalHibernateRepository();

            authService = new AuthHibernateService(userRepo);
            rentalService = new RentalHibernateService(rentalRepo, vehicleRepo, userRepo);
            vehicleService = new VehicleHibernateService(vehicleRepo, rentalRepo);
            userService = new UserHibernateService(userRepo, rentalRepo);

        } else if (mode.equals("jdbc")) {
            System.out.println("JDBC");
            userRepository = new UserJdbcRepository();
            vehicleRepository = new VehicleJdbcRepository();
            rentalRepository = new RentalJdbcRepository();

            authService = new OldAuthService(userRepository);
            rentalService = new OldRentalService(rentalRepository, vehicleRepository, userRepository);
            vehicleService = new OldVehicleService(vehicleValidator, vehicleRepository, rentalRepository);
            userService = new OldUserService(userRepository, rentalService, rentalRepository);
        } else {
            System.out.println("JSON");
            userRepository = new UserJsonRepository();
            vehicleRepository = new VehicleJsonRepository();
            rentalRepository = new RentalJsonRepository();

            authService = new OldAuthService(userRepository);
            rentalService = new OldRentalService(rentalRepository, vehicleRepository, userRepository);
            vehicleService = new OldVehicleService(vehicleValidator, vehicleRepository, rentalRepository);
            userService = new OldUserService(userRepository, rentalService, rentalRepository);        }

        UI ui = new UI(authService, vehicleService, rentalService, userService, categoryService);
        ui.start();
    }
}