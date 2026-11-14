package com.parking.client.model;

/** A slot of a parking bay that has a vehicle in it. */
public class BookedSlot {

    private final String location;
    private final String vehicleNo;
    private final String dateOfEntry;
    private final String timeOfEntry;
    private final String employeeId;

    public BookedSlot(String location, String vehicleNo, String dateOfEntry, String timeOfEntry, String employeeId) {
        this.location = location;
        this.vehicleNo = vehicleNo;
        this.dateOfEntry = dateOfEntry;
        this.timeOfEntry = timeOfEntry;
        this.employeeId = employeeId;
    }

    public String getLocation() {
        return location;
    }

    /** text shown when the mouse is over the slot */
    public String describe() {
        return "Vehicle No : " + vehicleNo + "\n"
                + "Date of Entry : " + dateOfEntry + "\n"
                + "Time of Entry : " + timeOfEntry + "\n"
                + "Employee Id : " + employeeId;
    }
}
