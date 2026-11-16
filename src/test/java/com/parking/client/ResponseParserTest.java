package com.parking.client;

import com.parking.client.api.ResponseParser;
import com.parking.client.model.BookedSlot;
import com.parking.client.model.DailySale;
import com.parking.client.model.ParkingRent;
import com.parking.client.model.TicketInfo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResponseParserTest {

    @Test
    void parsesRents() {
        List<ParkingRent> rents = ResponseParser.rents("Two Wheeler,20~!@Four Wheeler,50~!@");

        assertEquals(2, rents.size());
        assertEquals("Four Wheeler", rents.get(1).getVehicleType());
        assertEquals(50, rents.get(1).getRentPerHour());
    }

    @Test
    void parsesBookedSlots() {
        List<BookedSlot> slots = ResponseParser.bookedSlots(
                "A3####PB10AB1234####15-11-2026####10:30####101~~~~A17####PB10XY99####15-11-2026####11:00####102~~~~");

        assertEquals(2, slots.size());
        assertEquals("A17", slots.get(1).getLocation());
        assertTrue(slots.get(0).describe().contains("PB10AB1234"));
    }

    @Test
    void parsesTicketInfo() {
        TicketInfo info = ResponseParser.ticketInfo("PB10AB1234~~~~Two Wheeler~~~~15-11-2026~~~~10:30~~~~A3~~~~20");

        assertEquals("PB10AB1234", info.getVehicleNo());
        assertEquals("A3", info.getLocation());
        assertEquals(20, info.getRentPerHour());
    }

    @Test
    void invalidTicketGivesNull() {
        assertNull(ResponseParser.ticketInfo("failed"));
    }

    @Test
    void dailySaleOfBayWithoutExitsIsZero() {
        List<DailySale> sales = ResponseParser.dailySales("Parking Bay A#$%100~!@Parking Bay B#$%null~!@");

        assertEquals(100, sales.get(0).getSale());
        assertEquals(0, sales.get(1).getSale());
    }
}
