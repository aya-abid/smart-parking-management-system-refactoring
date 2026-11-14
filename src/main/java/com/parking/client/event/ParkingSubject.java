package com.parking.client.event;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject of the observer pattern. The exit screen does not need to know which bay screen opened it:
 * it only notifies the observers that registered.
 */
public class ParkingSubject {

    private final List<ParkingObserver> observers = new ArrayList<>();

    public void attach(ParkingObserver observer) {
        observers.add(observer);
    }

    public void detach(ParkingObserver observer) {
        observers.remove(observer);
    }

    public void notifyVehicleExited(String ticketNo) {
        for (ParkingObserver observer : new ArrayList<>(observers)) {
            observer.onVehicleExited(ticketNo);
        }
    }
}
