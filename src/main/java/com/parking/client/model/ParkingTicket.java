package com.parking.client.model;

/**
 * Data of a parked vehicle: sent to the server on entry and printed on the receipt.
 * It has 8 text values, so it is created with a {@link Builder} that names each one. The same builder
 * is reused to add the ticket number once the server returns it.
 */
public class ParkingTicket {

    private final String ticketNo;
    private final String vehicleNo;
    private final String vehicleType;
    private final String location;
    private final String parkingBay;
    private final String entryDate;
    private final String entryTime;
    private final String employeeId;

    private ParkingTicket(Builder builder) {
        this.ticketNo = builder.ticketNo;
        this.vehicleNo = builder.vehicleNo;
        this.vehicleType = builder.vehicleType;
        this.location = builder.location;
        this.parkingBay = builder.parkingBay;
        this.entryDate = builder.entryDate;
        this.entryTime = builder.entryTime;
        this.employeeId = builder.employeeId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getLocation() {
        return location;
    }

    public String getParkingBay() {
        return parkingBay;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public static class Builder {
        private String ticketNo;
        private String vehicleNo;
        private String vehicleType;
        private String location;
        private String parkingBay;
        private String entryDate;
        private String entryTime;
        private String employeeId;

        public Builder ticketNo(String ticketNo) {
            this.ticketNo = ticketNo;
            return this;
        }

        public Builder vehicleNo(String vehicleNo) {
            this.vehicleNo = vehicleNo;
            return this;
        }

        public Builder vehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder parkingBay(String parkingBay) {
            this.parkingBay = parkingBay;
            return this;
        }

        public Builder entryDate(String entryDate) {
            this.entryDate = entryDate;
            return this;
        }

        public Builder entryTime(String entryTime) {
            this.entryTime = entryTime;
            return this;
        }

        public Builder employeeId(String employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        public ParkingTicket build() {
            if (vehicleNo == null || location == null || parkingBay == null) {
                throw new IllegalStateException("vehicle no, location and parking bay are required");
            }
            return new ParkingTicket(this);
        }
    }
}
