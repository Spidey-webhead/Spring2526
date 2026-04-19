package org.example.user;

public class User {

    private String rentedVehicleId;
    private String login;
    private String password;
    private String role;

    public User(String login, String password, String role, String rentedVehicleId) {
        this.rentedVehicleId = rentedVehicleId;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public String getRentedVehicleId() {
        return rentedVehicleId;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setRentedVehicleId(String rentedVehicleId) {
        this.rentedVehicleId = rentedVehicleId;
    }

    @Override
    public String toString() {
        return "User{" +
                "rentedVehicleId='" + rentedVehicleId + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
