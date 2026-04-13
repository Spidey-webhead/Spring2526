package com.umcsuser.carrent;

import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.models.Vehicle;
import com.umcsuser.carrent.repositories.RentalRepository;
import com.umcsuser.carrent.repositories.UserRepository;
import com.umcsuser.carrent.repositories.VehicleRepository;
import com.umcsuser.carrent.services.AuthService;

import java.util.Scanner;

public class UI {
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;
    private final AuthService authService;
    private final Scanner scanner = new Scanner(System.in);
    private User currentUser = null;

    public UI(VehicleRepository vehicleRepository, UserRepository userRepository, RentalRepository rentalRepository, AuthService authService) {
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.rentalRepository = rentalRepository;
        this.authService = authService;
    }

    public void start(){
        System.out.println("CAR RENT");
        loginProcess();
        if(currentUser != null){
            menu();
        }
    }

    public void loginProcess(){
        System.out.println("Login: ");
        String login = scanner.nextLine();
        System.out.println("Haslo: ");
        String password = scanner.nextLine();
        authService.login(login, password).ifPresentOrElse(
                user -> {
                    this.currentUser = user;
                    System.out.println("Zalogowano jako " + user.getLogin() + user.getRole());
                },
                () -> System.out.println("Bledny login lub haslo")
        );
    }

    private void menu(){
        while(true){
            System.out.println("\"\\n1. Pokaz samochody\\n2. Wypozycz\\n3. Wyloguj\"");
            String choice = scanner.nextLine();
            switch (choice){
                case "1" -> showVehicles();
                case "3" -> {return;}
                default -> System.out.println("Bledna opcja");
            }
        }
    }

    private void showVehicles(){
        System.out.println("LISTA POJAZDOW");
        for(Vehicle v : vehicleRepository.findAll()){
            System.out.println(v.getBrand() + " " + v.getModel() + " " + v.getYear()+ "Cena: " + v.getPrice());
            if(!v.getAttributes().isEmpty()){
                v.getAttributes().forEach((key, value) ->
                        System.out.println(key + " " +value));
            }

        }

    }


}
