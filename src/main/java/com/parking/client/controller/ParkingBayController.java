package com.parking.client.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.parking.client.api.ParkingApi;
import com.parking.client.event.ParkingObserver;
import com.parking.client.model.BookedSlot;
import com.parking.client.model.ParkingBay;
import com.parking.client.model.ParkingTicket;
import com.parking.client.service.TicketPrinter;
import com.parking.client.util.Alerts;
import com.parking.client.util.ScreenManager;
import com.parking.common.Protocol;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Parking bay screen, used for both bays: green slots are free, red slots are booked. */
public class ParkingBayController implements ParkingObserver {

    private static final String FREE = "slot-free";
    private static final String BOOKED = "slot-booked";

    @FXML
    private Label titleLabel;
    @FXML
    private Label welcomeLabel;
    @FXML
    private HBox slotsBox;

    private final ParkingApi api = new ParkingApi();
    private final TicketPrinter printer = new TicketPrinter();
    private final Map<String, Button> slots = new LinkedHashMap<>();
    private ParkingBay bay;
    private String empId;

    public void init(ParkingBay bay, String empId) {
        this.bay = bay;
        this.empId = empId;
        titleLabel.setText(bay.getTitle());
        welcomeLabel.setText("Welcome, " + empId);
        for (List<String> block : bay.slotBlocks()) {
            slotsBox.getChildren().add(createBlock(block));
        }
        load();
    }

    private TilePane createBlock(List<String> locations) {
        TilePane block = new TilePane(8, 8);
        block.setPrefColumns(bay.getColumns());
        block.getStyleClass().add("bay-section");
        for (String location : locations) {
            Button slot = new Button(location);
            slot.setPrefSize(bay.getSlotWidth(), bay.getSlotHeight());
            slot.setOnAction(e -> {
                if (slot.getStyleClass().contains(FREE)) {
                    parkVehicle(location);
                }
            });
            slots.put(location, slot);
            block.getChildren().add(slot);
        }
        return block;
    }

    /** colours all slots green, then the booked ones red */
    private void load() {
        for (Button slot : slots.values()) {
            setStyle(slot, FREE);
            slot.setTooltip(null);
        }
        try {
            for (BookedSlot booked : api.getBookedSlots(bay.getBayName())) {
                Button slot = slots.get(booked.getLocation());
                if (slot != null) {
                    setStyle(slot, BOOKED);
                    slot.setTooltip(new Tooltip(booked.describe()));
                }
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    private static void setStyle(Button slot, String style) {
        slot.getStyleClass().removeAll(FREE, BOOKED);
        slot.getStyleClass().add(style);
    }

    private void parkVehicle(String location) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(bay.getBayName());
        dialog.setHeaderText(null);
        dialog.setContentText("Enter Vehicle No.");
        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) {
            return; // cancelled
        }
        if (result.get().isEmpty()) {
            Alerts.info("Enter vehicle no.");
            return;
        }

        Date now = new Date();
        ParkingTicket.Builder ticket = ParkingTicket.builder()
                .vehicleNo(result.get().toUpperCase())
                .location(location)
                .vehicleType(bay.getVehicleType())
                .parkingBay(bay.getBayName())
                .entryDate(new SimpleDateFormat(Protocol.DATE_FORMAT).format(now))
                .entryTime(new SimpleDateFormat(Protocol.TIME_FORMAT).format(now))
                .employeeId(empId);
        try {
            String ticketNo = api.parkVehicle(ticket.build());
            if (ticketNo == null) {
                Alerts.info("Vehicle Entry Failed!!!");
                return;
            }
            Alerts.info("Vehicle Entry Success");
            load();
            printer.print(ticket.ticketNo(ticketNo).build());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void exitVehicle() {
        ScreenManager.<ExitVehicleController>open("exit-vehicle.fxml", "Exit Parking", c -> c.attach(this));
        stage().hide();
    }

    @Override
    public void onVehicleExited(String ticketNo) {
        load();
        stage().show();
    }

    private Stage stage() {
        return (Stage) titleLabel.getScene().getWindow();
    }
}
