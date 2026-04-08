package com.umcsuser.carrent;

import com.umcsuser.carrent.models.Vehicle;

public class Car extends Vehicle {

    public Car(String id, String brand, String model, int year, double price, boolean rented) {
        super(id, brand, model, year, price, rented);
    }

    @Override
    public String toCSV(){
        return "CAR;" + super.toCSV();
    }

    @Override
    public String toString() {
        return "Car{" +
                "id='" + id + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", rented=" + rented +
                '}';
    }
}