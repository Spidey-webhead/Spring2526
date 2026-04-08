package com.umcsuser.carrent;

import com.umcsuser.carrent.models.Vehicle;

import java.util.List;

public interface IVehicleRepository {
    void rentVehicle(String id);
    void returnVehicle(String id);
    void save();
    void load();
    void add(Vehicle vehicle);
    void remove(String id);
    Vehicle getVehicle(String id);
    List<Vehicle> getVehicles();

}