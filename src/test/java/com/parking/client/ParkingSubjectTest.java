package com.parking.client;

import com.parking.client.event.ParkingObserver;
import com.parking.client.event.ParkingSubject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParkingSubjectTest {

    @Test
    void attachedObserversAreNotified() {
        List<String> received = new ArrayList<>();
        ParkingObserver observer = received::add;
        ParkingSubject subject = new ParkingSubject();
        subject.attach(observer);

        subject.notifyVehicleExited("12");
        subject.detach(observer);
        subject.notifyVehicleExited("13");

        assertEquals(List.of("12"), received);
    }
}
