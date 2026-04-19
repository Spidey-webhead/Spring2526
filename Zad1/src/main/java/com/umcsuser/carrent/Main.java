package com.umcsuser.carrent;

import com.umcsuser.carrent.repositories.RentalRepository;
import com.umcsuser.carrent.repositories.UserRepository;
import com.umcsuser.carrent.repositories.VehicleCategoryRepository;
import com.umcsuser.carrent.repositories.VehicleRepository;
import com.umcsuser.carrent.repositories.impl.RentalJsonRepository;
import com.umcsuser.carrent.repositories.impl.UserJsonRepository;
import com.umcsuser.carrent.repositories.impl.VehicleCategoryConfigJsonRepository;
import com.umcsuser.carrent.repositories.impl.VehicleJsonRepository;
import com.umcsuser.carrent.services.AuthService;
import com.umcsuser.carrent.services.RentalService;
import com.umcsuser.carrent.services.UserService;
import com.umcsuser.carrent.services.VehicleCategoryService;
import com.umcsuser.carrent.services.VehicleService;
import com.umcsuser.carrent.services.VehicleValidator;

public class Main {
    public static void main(String[] args) {
        VehicleRepository vehicleRepository = new VehicleJsonRepository();
        UserRepository userRepository = new UserJsonRepository();
        RentalRepository rentalRepository = new RentalJsonRepository();
        VehicleCategoryRepository categoryConfigRepository = new VehicleCategoryConfigJsonRepository();

        AuthService authService = new AuthService(userRepository);
        VehicleCategoryService categoryConfigService = new VehicleCategoryService(categoryConfigRepository);
        VehicleValidator vehicleValidator = new VehicleValidator(categoryConfigService);
        VehicleService vehicleService = new VehicleService(vehicleValidator, vehicleRepository, rentalRepository);
        RentalService rentalService = new RentalService(rentalRepository, vehicleRepository);
        UserService userService = new UserService(userRepository, rentalService);

        UI ui = new UI(
                authService,
                vehicleService,
                rentalService,
                userService,
                categoryConfigService
        );

        ui.start();
    }
}