package com.parking.client.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.parking.client.api.ParkingApi;
import com.parking.client.model.ParkingBay;
import com.parking.client.model.ParkingRent;
import com.parking.client.util.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ManageRentController {

    @FXML
    private ComboBox<String> vehicleTypeBox;
    @FXML
    private TextField rentField;
    @FXML
    private TableView<ParkingRent> rentTable;
    @FXML
    private TableColumn<ParkingRent, String> vehicleTypeColumn;
    @FXML
    private TableColumn<ParkingRent, Integer> rentColumn;

    private final ParkingApi api = new ParkingApi();

    @FXML
    private void initialize() {
        vehicleTypeBox.getItems().addAll(
                ParkingBay.A.getVehicleType(),
                ParkingBay.B.getVehicleType());
        vehicleTypeBox.getSelectionModel().selectFirst();
        vehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        rentColumn.setCellValueFactory(new PropertyValueFactory<>("rentPerHour"));
        loadData();
    }

    private void loadData() {
        try {
            rentTable.getItems().setAll(api.getRents());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addRent() {
        String vehicleType = vehicleTypeBox.getValue();
        if (vehicleType == null || rentField.getText().isEmpty()) {
            Alerts.info("all fields are mandatory");
            return;
        }
        int rent = Integer.parseInt(rentField.getText());
        try {
            if (api.addRent(vehicleType, rent)) {
                Alerts.info("Parking Rent Added");
                loadData();
            } else {
                Alerts.info("Duplicate Vehicle Type");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteRent() {
        ParkingRent selected = rentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alerts.info("Please select a record");
            return;
        }
        try {
            if (api.deleteRent(selected.getVehicleType())) {
                Alerts.info("Parking Rent Deleted");
                loadData();
            } else {
                Alerts.info("Error Delete Parking Rent");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
