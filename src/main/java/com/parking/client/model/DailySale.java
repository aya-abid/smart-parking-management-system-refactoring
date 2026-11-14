package com.parking.client.model;

public class DailySale {

    private final String parkingBay;
    private final int sale;

    public DailySale(String parkingBay, int sale) {
        this.parkingBay = parkingBay;
        this.sale = sale;
    }

    public String getParkingBay() {
        return parkingBay;
    }

    public int getSale() {
        return sale;
    }
}
