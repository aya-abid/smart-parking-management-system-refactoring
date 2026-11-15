package com.parking.client.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.parking.client.api.ParkingApi;
import com.parking.client.event.ParkingObserver;
import com.parking.client.event.ParkingSubject;
import com.parking.client.model.TicketInfo;
import com.parking.client.service.RentCalculator;
import com.parking.client.util.Alerts;
import com.parking.common.Protocol;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExitVehicleController {

    @FXML
    private TextField ticketField;
    @FXML
    private GridPane exitPanel;
    @FXML
    private TextField vehicleNoField;
    @FXML
    private TextField vehicleTypeField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField entryDateField;
    @FXML
    private TextField entryTimeField;
    @FXML
    private TextField exitDateField;
    @FXML
    private TextField exitTimeField;
    @FXML
    private TextField rentField;

    private final ParkingApi api = new ParkingApi();
    private final ParkingSubject subject = new ParkingSubject();

    @FXML
    private void initialize() {
        exitPanel.setVisible(false);
    }

    /** the bay that opened this window wants to know when the vehicle has exited */
    public void attach(ParkingObserver observer) {
        subject.attach(observer);
    }

    /** Submit button, or Enter in the ticket field */
    @FXML
    private void loadTicket() {
        String ticketNo = ticketField.getText();
        if (ticketNo.isEmpty()) {
            Alerts.info("Enter Ticket No.");
            return;
        }
        try {
            TicketInfo info = api.getTicketInfo(ticketNo);
            if (info == null) {
                Alerts.info("Invalid Ticket No.");
                return;
            }
            exitPanel.setVisible(true);
            vehicleNoField.setText(info.getVehicleNo());
            vehicleTypeField.setText(info.getVehicleType());
            entryDateField.setText(info.getDateOfEntry());
            entryTimeField.setText(info.getTimeOfEntry());
            locationField.setText(info.getLocation());

            Date now = new Date();
            Date entry = new SimpleDateFormat(Protocol.DATE_FORMAT + " " + Protocol.TIME_FORMAT)
                    .parse(info.getDateOfEntry() + " " + info.getTimeOfEntry());
            rentField.setText(String.valueOf(RentCalculator.calculate(entry, now, info.getRentPerHour())));
            exitDateField.setText(new SimpleDateFormat(Protocol.DATE_FORMAT).format(now));
            exitTimeField.setText(new SimpleDateFormat(Protocol.TIME_FORMAT).format(now));
        } catch (UnirestException | ParseException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void exitVehicle() {
        String ticketNo = ticketField.getText();
        try {
            if (api.exitVehicle(ticketNo, exitDateField.getText(), exitTimeField.getText(), rentField.getText())) {
                Alerts.info("Vehicle Exited Successfully!!!!");
                subject.notifyVehicleExited(ticketNo);
                ((Stage) ticketField.getScene().getWindow()).close();
            } else {
                Alerts.info("Error Occured");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
