package org.example;

import java.util.List;
import java.util.Scanner;

public class InteractionUser {

    public static void main(String[] args) {

        IVehicleRepository repo = new VehicleRepositoryImpl();
        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("\n    WYPOŻYCZALNIA POJAZDÓW    ");
            System.out.println("1 - Lista pojazdów");
            System.out.println("2 - Wypożycz pojazd");
            System.out.println("3 - Zwróć pojazd");
            System.out.println("4 - Wyjście");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {

                case 1:

                    List<Vehicle> vehicles = repo.getVehicles();

                    for (Vehicle v : vehicles) {
                        System.out.println(v);
                    }

                    break;

                case 2:

                    System.out.println("Podaj ID pojazdu do wypożyczenia:");
                    String rentId = scanner.nextLine();

                    repo.rentVehicle(rentId);

                    break;

                case 3:

                    System.out.println("Podaj ID pojazdu do zwrotu:");
                    String returnId = scanner.nextLine();

                    repo.returnVehicle(returnId);

                    break;

                case 4:

                    System.out.println("Koniec programu");
                    return;
            }
        }
    }
}