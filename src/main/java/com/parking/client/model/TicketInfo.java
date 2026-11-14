package com.parking.client.model;

/** What the exit screen gets from the server for a ticket number. */
public class TicketInfo {

    private final String vehicleNo;
    private final String vehicleType;
    private final String dateOfEntry;
    private final String timeOfEntry;
    private final String location;
    private final int rentPerHour;

    public TicketInfo(String vehicleNo, String vehicleType, String dateOfEntry, String timeOfEntry,
                      String location, int rentPerHour) {
        this.vehicleNo = vehicleNo;
        this.vehicleType = vehicleType;
        this.dateOfEntry = dateOfEntry;
        this.timeOfEntry = timeOfEntry;
        this.location = location;
        this.rentPerHour = rentPerHour;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getDateOfEntry() {
        return dateOfEntry;
    }

    public String getTimeOfEntry() {
        return timeOfEntry;
    }

    public String getLocation() {
        return location;
    }

    public int getRentPerHour() {
        return rentPerHour;
    }
}
