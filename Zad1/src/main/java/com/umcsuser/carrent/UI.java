package com.umcsuser.carrent;

import com.umcsuser.carrent.models.*;
import com.umcsuser.carrent.services.AuthServiceInterface;
import com.umcsuser.carrent.services.RentalServiceInterface;
import com.umcsuser.carrent.services.UserServiceInterface;
import com.umcsuser.carrent.services.VehicleServiceInterface;
import com.umcsuser.carrent.services.impl.*;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UI {

    private final AuthServiceInterface authService;
    private final VehicleServiceInterface vehicleService;
    private final RentalServiceInterface rentalService;
    private final UserServiceInterface userService;
    private final VehicleCategoryService categoryConfigService;
    private final Scanner scanner = new Scanner(System.in);

    public UI(AuthServiceInterface authService,
              VehicleServiceInterface vehicleService,
              RentalServiceInterface rentalService,
              UserServiceInterface userService,
              VehicleCategoryService categoryService) {
        this.authService = authService;
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
        this.userService = userService;
        this.categoryConfigService = categoryService;
    }

    public void start() {
        while (true) {
            System.out.println("\n=== START ===\n1. Zaloguj\n2. Zarejestruj\n0. Koniec");
            switch (scanner.nextLine().trim()) {
                case "1" -> {
                    User loggedUser = login();
                    if (loggedUser != null) {
                        System.out.println("Zalogowano: " + loggedUser.getLogin() + " (" + loggedUser.getRole() + ")");
                        if (loggedUser.getRole() == Role.ADMIN) adminMenu(loggedUser);
                        else userMenu(loggedUser);
                    } else {
                        System.out.println("Nieprawidlowy login lub haslo.");
                    }
                }
                case "2" -> register();
                case "0" -> { return; }
                default -> System.out.println("Nieprawidlowa opcja.");
            }
        }
    }

    private void register() {
        System.out.println("=== Rejestracja ===");
        if (authService.register(readText("Podaj login: "), readText("Podaj haslo: "))) {
            System.out.println("Zarejestrowano pomyslnie.");
        } else {
            System.out.println("Blad rejestracji. Uzytkownik prawdopodobnie juz istnieje.");
        }
    }

    private User login() {
        System.out.println("=== Logowanie ===");
        return authService.login(readText("Login: "), readText("Haslo: ")).orElse(null);
    }

    private void adminMenu(User loggedUser) {
        while (true) {
            System.out.println("\n=== MENU ADMINA ===");
            System.out.println("1. Pokaz pojazdy | 2. Dodaj pojazd | 3. Usun pojazd | 4. Pokaz uzytkownikow | 5. Usun uzytkownika | 6. Moje dane | 7. Historia wypozyczen | 0. Wyloguj");

            switch (scanner.nextLine().trim()) {
                case "1" -> vehicleService.findAllVehicles().forEach(v ->
                        System.out.println(v + " [Wypozyczony: " + rentalService.vehicleHasActiveRental(v.getId()) + "]"));
                case "2" -> addVehicle();
                case "3" -> deleteVehicle();
                case "4" -> showAllUsers();
                case "5" -> deleteUser(loggedUser);
                case "6" -> showCurrentUserData(loggedUser);
                case "7" -> showRentalHistory();
                case "0" -> { return; }
                default -> System.out.println("Nieprawidlowa opcja.");
            }
        }
    }

    private void userMenu(User loggedUser) {
        while (true) {
            System.out.println("\n=== MENU USERA ===");
            System.out.println("1. Dostepne pojazdy | 2. Wypozycz | 3. Zwroc | 4. Moje dane | 5. Moja historia | 0. Wyloguj");

            switch (scanner.nextLine().trim()) {
                case "1" -> vehicleService.findAvailableVehicles().forEach(System.out::println);
                case "2" -> rentVehicle(loggedUser);
                case "3" -> returnVehicle(loggedUser);
                case "4" -> showCurrentUserData(loggedUser);
                case "5" -> {
                    List<Rental> rentals = rentalService.findUserRentals(loggedUser.getId());
                    if (rentals.isEmpty()) System.out.println("Brak historii wypozyczen.");
                    else rentals.forEach(this::printRentalDetails);
                }
                case "0" -> { return; }
                default -> System.out.println("Nieprawidlowa opcja.");
            }
        }
    }

    private void addVehicle() {
        System.out.println("=== Dodawanie pojazdu ===");
        List<VehicleCategoryConfig> categories = categoryConfigService.findAllCategories();
        if (categories.isEmpty()) {
            System.out.println("Brak skonfigurowanych kategorii pojazdow.");
            return;
        }

        System.out.println("Dostepne kategorie:");
        categories.forEach(c -> System.out.println("- " + c.getCategory()));

        try {
            VehicleCategoryConfig config = categoryConfigService.getByCategory(readText("Podaj kategorie: "));
            Vehicle vehicle = Vehicle.builder()
                    .category(config.getCategory())
                    .brand(readText("Marka: "))
                    .model(readText("Model: "))
                    .year(readInt("Rok: "))
                    .plate(readText("Rejestracja: "))
                    .price(readDouble("Cena: "))
                    .build();

            for (Map.Entry<String, Object> entry : config.getAttributes().entrySet()) {
                vehicle.addAttribute(entry.getKey(), readAttributeValue(entry.getKey(), (String) entry.getValue()));
            }

            System.out.println("Dodano pojazd o ID: " + vehicleService.addVehicle(vehicle).getId());
        } catch (Exception e) {
            System.out.println("Blad: " + e.getMessage());
        }
    }

    private void deleteVehicle() {
        try {
            vehicleService.removeVehicle(readText("ID pojazdu do usuniecia: "));
            System.out.println("Usunieto pomyslnie.");
        } catch (Exception e) {
            System.out.println("Blad: " + e.getMessage());
        }
    }

    private void deleteUser(User loggedUser) {
        try {
            userService.deleteUser(readText("ID usera do usuniecia: "), loggedUser.getId());
            System.out.println("Usunieto pomyslnie.");
        } catch (Exception e) {
            System.out.println("Blad: " + e.getMessage());
        }
    }

    private void showAllUsers() {
        List<User> users = userService.findAllUsers();
        if (users.isEmpty()) {
            System.out.println("Brak uzytkownikow.");
            return;
        }

        users.forEach(user -> {
            System.out.println(user);
            List<Rental> rentals = rentalService.findUserRentals(user.getId());

            if (rentals.isEmpty()) {
                System.out.println("  Historia wypozyczen: brak\n--------------------");
            } else {
                System.out.println("  Historia wypozyczen:");
                rentals.forEach(this::printRentalDetails);
            }
        });
    }

    private void rentVehicle(User loggedUser) {
        try {
            rentalService.rentVehicle(loggedUser.getId(), readText("ID pojazdu do wypozyczenia: "));
            System.out.println("Pojazd zostal wypozyczony.");
        } catch (Exception e) {
            System.out.println("Blad: " + e.getMessage());
        }
    }

    private void returnVehicle(User loggedUser) {
        try {
            rentalService.returnVehicle(loggedUser.getId());
            System.out.println("Pojazd zostal zwrocony.");
        } catch (Exception e) {
            System.out.println("Blad: " + e.getMessage());
        }
    }

    private void showCurrentUserData(User loggedUser) {
        try {
            User u = userService.findById(loggedUser.getId());
            System.out.println("ID: " + u.getId() + " | Login: " + u.getLogin() + " | Rola: " + u.getRole());

            rentalService.findActiveRentalByUserId(u.getId())
                    .ifPresentOrElse(
                            rental -> {
                                try {
                                    System.out.println("Aktualnie wypozyczony pojazd: " + vehicleService.findById(rental.getVehicleId()));
                                } catch (Exception e) {
                                    System.out.println("Aktualnie wypozyczony pojazd: " + rental.getVehicleId() + " (brak szczegolow)");
                                }
                            },
                            () -> System.out.println("Brak aktywnego wypozyczenia.")
                    );
        } catch (Exception e) {
            System.out.println("Nie udalo sie odczytac danych uzytkownika.");
        }
    }

    private void showRentalHistory() {
        List<Rental> rentals = rentalService.findAllRentals();
        if (rentals.isEmpty()) {
            System.out.println("Brak historii wypozyczen.");
            return;
        }
        rentals.forEach(this::printRentalDetails);
    }

    private String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("To pole nie moze byc puste!");
        }
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readText(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Wpisz poprawna liczbe calkowita!");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            try {
                double val = Double.parseDouble(readText(prompt));
                if (val >= 0) return val;
                System.out.println("Wartosc nie moze byc ujemna!");
            } catch (NumberFormatException e) {
                System.out.println("Wpisz poprawna liczbe!");
            }
        }
    }

    private boolean readBoolean(String prompt) {
        while (true) {
            String input = readText(prompt).toLowerCase();
            if (input.equals("true")) return true;
            if (input.equals("false")) return false;
            System.out.println("Wpisz 'true' lub 'false'.");
        }
    }

    private Object readAttributeValue(String attrName, String attrType) {
        return switch (attrType.toLowerCase()) {
            case "string" -> readText(attrName + " (tekst): ");
            case "number" -> readDouble(attrName + " (liczba): ");
            case "boolean" -> readBoolean(attrName + " (true/false): ");
            case "integer" -> readInt(attrName + " (liczba calkowita): ");
            default -> throw new IllegalArgumentException("Nieznany typ: " + attrType);
        };
    }

    private void printRentalDetails(Rental rental) {
        System.out.println(rental);

        String login = "nieznany";
        try {
            login = userService.findById(rental.getUserId()).getLogin();
        } catch (Exception ignored) {}

        String vehicle = "nieznany";
        try {
            vehicle = vehicleService.findById(rental.getVehicleId()).toString();
        } catch (Exception ignored) {}

        System.out.println("  user: " + login);
        System.out.println("  vehicle: " + vehicle);
        System.out.println("--------------------");
    }
}