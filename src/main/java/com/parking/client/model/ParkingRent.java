package com.parking.client.model;

public class ParkingRent {

    private final String vehicleType;
    private final int rentPerHour;

    public ParkingRent(String vehicleType, int rentPerHour) {
        this.vehicleType = vehicleType;
        this.rentPerHour = rentPerHour;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public int getRentPerHour() {
        return rentPerHour;
    }
}
