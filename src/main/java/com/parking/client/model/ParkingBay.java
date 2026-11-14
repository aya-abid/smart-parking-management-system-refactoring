package com.parking.client.model;

import java.util.ArrayList;
import java.util.List;

/** The two parking bays. The bay screen is the same for both, only these values change. */
public enum ParkingBay {

    A("Parking Bay A", "PARKING BAY A (TWO WHEELER)", "Two Wheeler", "bike.jpg", "A", 25, 50, 5, 60, 40),
    B("Parking Bay B", "PARKING BAY B (FOUR WHEELER)", "Four Wheeler", "car.jpg", "B", 20, 36, 4, 80, 55);

    private final String bayName;
    private final String title;
    private final String vehicleType;
    private final String imageName;
    private final String slotPrefix;
    private final int firstBlockSize;
    private final int totalSlots;
    private final int columns;
    private final double slotWidth;
    private final double slotHeight;

    ParkingBay(String bayName, String title, String vehicleType, String imageName, String slotPrefix,
               int firstBlockSize, int totalSlots, int columns, double slotWidth, double slotHeight) {
        this.bayName = bayName;
        this.title = title;
        this.vehicleType = vehicleType;
        this.imageName = imageName;
        this.slotPrefix = slotPrefix;
        this.firstBlockSize = firstBlockSize;
        this.totalSlots = totalSlots;
        this.columns = columns;
        this.slotWidth = slotWidth;
        this.slotHeight = slotHeight;
    }

    /** name stored in the database, e.g. "Parking Bay A" */
    public String getBayName() {
        return bayName;
    }

    public String getTitle() {
        return title;
    }

    /** vehicle type stored in the database, e.g. "Two Wheeler" */
    public String getVehicleType() {
        return vehicleType;
    }

    public String getImageName() {
        return imageName;
    }

    public int getColumns() {
        return columns;
    }

    public double getSlotWidth() {
        return slotWidth;
    }

    public double getSlotHeight() {
        return slotHeight;
    }

    /** slot names, split in the two blocks shown on screen, e.g. [[A1..A25], [A26..A50]] */
    public List<List<String>> slotBlocks() {
        return List.of(slots(1, firstBlockSize), slots(firstBlockSize + 1, totalSlots));
    }

    private List<String> slots(int from, int to) {
        List<String> names = new ArrayList<>();
        for (int i = from; i <= to; i++) {
            names.add(slotPrefix + i);
        }
        return names;
    }
}
