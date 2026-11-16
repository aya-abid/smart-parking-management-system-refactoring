package com.parking.client;

import com.parking.client.model.ParkingTicket;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParkingTicketTest {

    @Test
    void builderIsReusedToAddTheTicketNumber() {
        ParkingTicket.Builder builder = ParkingTicket.builder()
                .vehicleNo("PB10AB1234")
                .location("A3")
                .parkingBay("Parking Bay A")
                .vehicleType("Two Wheeler");

        ParkingTicket beforeEntry = builder.build();
        ParkingTicket printed = builder.ticketNo("7").build();

        assertNull(beforeEntry.getTicketNo());
        assertEquals("7", printed.getTicketNo());
        assertEquals("A3", printed.getLocation());
    }

    @Test
    void vehicleNumberIsRequired() {
        assertThrows(IllegalStateException.class,
                () -> ParkingTicket.builder().location("A1").parkingBay("Parking Bay A").build());
    }
}
