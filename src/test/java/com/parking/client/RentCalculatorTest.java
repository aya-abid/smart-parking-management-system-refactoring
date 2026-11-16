package com.parking.client;

import com.parking.client.service.RentCalculator;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RentCalculatorTest {

    private static final long MINUTE = 60_000;
    private final Date entry = new Date(0);

    @Test
    void lessThanOneHourCostsOneHour() {
        assertEquals(20, RentCalculator.calculate(entry, new Date(25 * MINUTE), 20));
    }

    @Test
    void onlyFullHoursAreCounted() {
        assertEquals(20, RentCalculator.calculate(entry, new Date(119 * MINUTE), 20));
        assertEquals(40, RentCalculator.calculate(entry, new Date(125 * MINUTE), 20));
    }
}
