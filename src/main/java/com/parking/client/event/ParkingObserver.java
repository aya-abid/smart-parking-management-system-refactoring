package com.parking.client.event;

/** Observer: told when a vehicle has left, so it can refresh what it shows. */
public interface ParkingObserver {

    void onVehicleExited(String ticketNo);
}
