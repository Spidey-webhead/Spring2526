package com.umcsuser.carrent.models;

import java.util.*;

public class Vehicle {

    private String id;
    private String category;
    private String brand;
    private String model;
    private int year;
    private String plate;
    private double price;
    private Map<String, Object> attributes = new HashMap<>();

    public Vehicle(String id, String category, String brand, String model, int year, String plate, double price, Map<String, Object> attributes) {
        this.id = id;
        this.category = category;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.plate = plate;
        this.price = price;
        this.attributes = attributes == null ? new HashMap<>() new HashMap<>(attributes);
    }

    public Map<String, Object> getAttributes(){
        return Collections.unmodifiableMap(attributes);
    }

    public Object getAttributes(String key) {
        return attributes.get(key);
    }
    public void addAttribute(String key, Object value){
        attributes.put(key, value);
    }
    public Object removeAttributes(String key) {
        return attributes.remove(key);
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setPlate(String plate) {
        this.plate = plate;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    public String getCategory() {
        return category;
    }
    public String getPlate() {
        return plate;
    }
    public String getId() {
        return id;
    }
    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public double getPrice() {
        return price;
    }

    public Vehicle copy() {
        return new Vehicle(
                this.id,
                this.category,
                this.brand,
                this.model,
                this.year,
                this.plate,
                this.price,
                new HashMap<>(this.attributes)
        );
    }
    @Override
    public String toString() {
        return "Vehicle{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", plate='" + plate + '\'' +
                ", price=" + price +
                ", attributes=" + attributes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle vehicle)) return false;
        return Objects.equals(id, vehicle.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}