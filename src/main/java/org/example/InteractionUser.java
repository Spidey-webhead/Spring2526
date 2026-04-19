package org.example;

import org.example.user.*;

import java.util.Scanner;

public class InteractionUser {

    private static final IVehicleRepository vehicleRepo = new VehicleRepositoryImpl();
    private static final IUserRepository userRepo = new UserRepositoryImpl();
    private static final Authentication auth = new Authentication(userRepo);
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n    LOGOWANIE    ");
            System.out.println("1 - Logowanie");
            System.out.println("2 - Rejestracja");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> {
                    System.out.print("Podaj login: ");
                    String login = scanner.nextLine();
                    System.out.print("Podaj haslo: ");
                    String password = scanner.nextLine();

                    User currentUser = auth.authenticateLogs(login, password);

                    if (currentUser != null) {
                        if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
                            adminMenu();
                        } else {
                            userMenu(currentUser.getLogin());
                        }
                    } else {
                        System.out.println("Blad logowania!!!!");
                    }
                }

                case "2" -> {
                    System.out.println("Podaj login do rejestracji: ");
                    String loginRejestracja = scanner.nextLine();
                    System.out.println("Podaj haslo");
                    String passwordRejestracja = scanner.nextLine();
                    boolean sukces = userRepo.add(new User(loginRejestracja, Hasher.hashPassword(passwordRejestracja), "USER", null));
                    if (sukces) {
                        System.out.println("Zalogowano");
                    } else {
                        System.out.println("Blad: login juz istnieje!!!!!!");
                    }
                }

            }
        }
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("\n    ADMIN    ");
            System.out.println("1 - Lista pojazdów");
            System.out.println("2 - Dodaj pojazd");
            System.out.println("3 - Usuń pojazd");
            System.out.println("4 - Lista uzytkownikow");
            System.out.println("5 - Usun uzytkownika");
            System.out.println("6 - Wyloguj");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> vehicleRepo.getVehicles().forEach(System.out::println);
                case "2" -> {
                    System.out.print("ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Marka: ");
                    String b = scanner.nextLine();
                    System.out.print("Model: ");
                    String m = scanner.nextLine();
                    System.out.print("Rok: ");
                    int y = Integer.parseInt(scanner.nextLine());
                    System.out.print("Cena: ");
                    double p = Double.parseDouble(scanner.nextLine());
                    vehicleRepo.add(new Car(id, b, m, y, p, false));
                }
                case "3" -> {
                    System.out.print("Podaj id auta do usunięcia: ");
                    String toRemove = scanner.nextLine();
                    if (userRepo.remove(toRemove)) System.out.println("usunieto.");
                    else System.out.println("Nie można usunac wypożyczone auto");
                }
                case "4" -> {
                    for (User u : userRepo.getUsers()) {
                        System.out.print(u.getLogin() + " [" + u.getRole() + "]");
                        if (u.getRentedVehicleId() != null) {
                            System.out.println(" | Auto: " + vehicleRepo.getVehicle(u.getRentedVehicleId()));
                        } else {
                            System.out.println(" | Brak");
                        }
                    }
                }
                case "5" -> {
                    System.out.print("Podaj login użytkownika do usunięcia: ");
                    String toRemove = scanner.nextLine();
                    if (userRepo.remove(toRemove)) System.out.println("usunieto.");
                    else System.out.println("Nie można usunac brak usera lub ma wypożyczone auto");
                }
                case "6" -> {
                    return;
                }
            }
        }
    }

    private static void userMenu(String login) {
        while (true) {
            User curr = userRepo.getUser(login);

            System.out.println("\n    USER    ");
            System.out.println("1 - Lista pojazdów");
            System.out.println("2 - Wypożycz");
            System.out.println("3 - Zwróć");
            System.out.println("4 - Moje dane");
            System.out.println("5 - Wyloguj");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> vehicleRepo.getVehicles().forEach(System.out::println);
                case "2" -> {
                    if (curr.getRentedVehicleId() != null) {
                        System.out.println("Masz już pojazd");
                        break;
                    }
                    System.out.print("ID pojazdu: ");
                    String id = scanner.nextLine();
                    Vehicle v = vehicleRepo.getVehicle(id);
                    if (v != null && !v.isRented()) {
                        v.setRented(true);
                        curr.setRentedVehicleId(id);
                        vehicleRepo.save();
                        userRepo.update(curr);
                    }
                }
                case "3" -> {
                    if (curr.getRentedVehicleId() == null) break;
                    Vehicle v = vehicleRepo.getVehicle(curr.getRentedVehicleId());
                    if (v != null) v.setRented(false);
                    curr.setRentedVehicleId(null);
                    vehicleRepo.save();
                    userRepo.update(curr);
                }
                case "4" -> {
                    System.out.println(curr);
                    if (curr.getRentedVehicleId() != null) {
                        System.out.println("Auto: " + vehicleRepo.getVehicle(curr.getRentedVehicleId()));
                    }
                }
                case "5" -> {
                    return;
                }
            }
        }
    }
}


//user1 h:user1
//user2 h:user2