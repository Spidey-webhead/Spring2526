package com.umcsuser.carrent;


import com.umcsuser.carrent.repositories.impl.RentalJsonRepository;
import com.umcsuser.carrent.repositories.impl.UserJsonRepository;
import com.umcsuser.carrent.repositories.impl.VehicleJsonRepository;
import com.umcsuser.carrent.services.AuthService;


public class Main {
    public static void main(String[] args){
        VehicleJsonRepository vehicleJsonRepository = new VehicleJsonRepository();
        UserJsonRepository userJsonRepository = new UserJsonRepository();
        RentalJsonRepository rentalJsonRepository = new RentalJsonRepository();
        AuthService authService = new AuthService(userJsonRepository);
        UI ui = new UI(vehicleJsonRepository, userJsonRepository, rentalJsonRepository, authService);
        ui.start();
    }

}