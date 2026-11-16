package com.parking.client;

import com.parking.client.model.ParkingBay;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParkingBayTest {

    @Test
    void bayAHas50SlotsInTwoBlocks() {
        List<List<String>> blocks = ParkingBay.A.slotBlocks();

        assertEquals(25, blocks.get(0).size());
        assertEquals(25, blocks.get(1).size());
        assertEquals("A1", blocks.get(0).get(0));
        assertEquals("A50", blocks.get(1).get(24));
    }

    @Test
    void bayBHas36Slots() {
        List<List<String>> blocks = ParkingBay.B.slotBlocks();

        assertEquals(36, blocks.get(0).size() + blocks.get(1).size());
        assertEquals("B21", blocks.get(1).get(0));
    }
}
