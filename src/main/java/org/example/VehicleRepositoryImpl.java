package org.example;

import org.example.user.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleRepositoryImpl implements IVehicleRepository {

    private List<Vehicle> vehicles = new ArrayList<>();
    private static final String FILE = "vehicles.csv";

    public VehicleRepositoryImpl() {
        load();
    }

    @Override
    public void rentVehicle(String id) {
        for (Vehicle v : vehicles) {
            if (v.getId().equals(id) && !v.isRented()) {
                v.setRented(true);
                save();
                return;
            }
        }
    }

    @Override
    public void returnVehicle(String id) {
        for (Vehicle v : vehicles) {
            if (v.getId().equals(id)) {
                v.setRented(false);
                save();
                return;
            }
        }
    }


    @Override
    public void save() {

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE))) {

            for (Vehicle v : vehicles) {
                writer.println(v.toCSV());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void load() {

        vehicles.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(";");

                if (data[0].equals("CAR")) {

                    vehicles.add(new Car(
                            data[1],
                            data[2],
                            data[3],
                            Integer.parseInt(data[4]),
                            Double.parseDouble(data[5]),
                            Boolean.parseBoolean(data[6])
                    ));
                }

                else if (data[0].equals("MOTORCYCLE")) {

                    vehicles.add(new Motorcycle(
                            data[1],
                            data[2],
                            data[3],
                            Integer.parseInt(data[4]),
                            Double.parseDouble(data[5]),
                            Boolean.parseBoolean(data[6]),
                            data[7]
                    ));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void add(Vehicle vehicle) {
        if(getVehicle(vehicle.getId()) ==null){
            vehicles.add(vehicle);
            save();
        } else {
            System.out.println("pojazd o ID " + vehicle.getId() + " już istnieje");
        }

    }

    @Override
    public boolean remove(String id) {
        Vehicle v = getVehicle(id);
        if (v != null && v.getId() == null) {
            vehicles.removeIf(vehicle -> vehicle.getId().equals(id));
            save();
            return true;
        }
        return false;
    }

    @Override
    public Vehicle getVehicle(String id) {
        for (Vehicle v : vehicles) {
            if (v.getId().equals(id)) {
                return v;
            }
        }
        return null;
    }

    @Override
    public List<Vehicle> getVehicles() {
        List<Vehicle> copy = new ArrayList<>();

        for (Vehicle v : vehicles) {

            if (v instanceof Car c) {

                copy.add(new Car(
                        c.getId(),
                        c.getBrand(),
                        c.getModel(),
                        c.getYear(),
                        c.getPrice(),
                        c.isRented()
                ));
            }

            else if (v instanceof Motorcycle m) {

                copy.add(new Motorcycle(
                        m.getId(),
                        m.getBrand(),
                        m.getModel(),
                        m.getYear(),
                        m.getPrice(),
                        m.isRented(),
                        m.getCategory()
                ));
            }

        }

        return copy;
    }
}