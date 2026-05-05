package com.umcsuser.carrent;

import com.umcsuser.carrent.repositories.*;
import com.umcsuser.carrent.repositories.impl.*;
import com.umcsuser.carrent.services.*;
import org.mindrot.jbcrypt.BCrypt;

public class Main {
    public static void main(String[] args) {
        System.out.println(BCrypt.hashpw("admin123", BCrypt.gensalt()));
        UserRepository userRepository;
        VehicleRepository vehicleRepository;
        RentalRepository rentalRepository;

        VehicleCategoryRepository categoryRepository = new VehicleCategoryConfigJsonRepository();

        boolean useJdbc = args.length > 0 && args[0].equalsIgnoreCase("jdbc");

        if (useJdbc) {
            System.out.println("[SYSTEM] Uruchamianie w trybie BAZY DANYCH (JDBC)");
            userRepository = new UserJdbcRepository();
            vehicleRepository = new VehicleJdbcRepository();
            rentalRepository = new RentalJdbcRepository();
        } else {
            System.out.println("[SYSTEM] Uruchamianie w trybie PLIKÓW (JSON)");
            userRepository = new UserJsonRepository();
            vehicleRepository = new VehicleJsonRepository();
            rentalRepository = new RentalJsonRepository();
        }

        AuthService authService = new AuthService(userRepository);
        VehicleCategoryService categoryService = new VehicleCategoryService(categoryRepository);
        VehicleValidator vehicleValidator = new VehicleValidator(categoryService);

        RentalService rentalService = new RentalService(rentalRepository, vehicleRepository);
        VehicleService vehicleService = new VehicleService(vehicleValidator, vehicleRepository, rentalRepository);
        UserService userService = new UserService(userRepository, rentalService, rentalRepository);

        UI ui = new UI(authService, vehicleService, rentalService, userService, categoryService);
        ui.start();
    }
}