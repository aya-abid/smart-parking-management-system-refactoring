package com.parking.client.service;

import java.util.Date;

/** Rent of a parked vehicle: full hours parked times the rent per hour, at least one hour. */
public final class RentCalculator {

    private RentCalculator() {
    }

    public static int calculate(Date entry, Date exit, int rentPerHour) {
        long diff = exit.getTime() - entry.getTime();
        int hours = (int) (diff / 1000 / 60 / 60);
        if (hours == 0) {
            hours = 1;
        }
        return hours * rentPerHour;
    }
}
