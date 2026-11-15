package com.parking.client.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.parking.client.api.ParkingApi;
import com.parking.client.service.ConsoleSmsSender;
import com.parking.client.service.SmsSender;
import com.parking.client.util.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;

public class AddEmployeeController {

    @FXML
    private TextField employeeIdField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField fatherNameField;
    @FXML
    private TextArea addressArea;
    @FXML
    private TextField mobileField;
    @FXML
    private ImageView photoView;

    private final ParkingApi api = new ParkingApi();
    private final SmsSender smsSender = new ConsoleSmsSender();
    private File photo;

    @FXML
    private void browsePhoto() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.bmp"));
        File selected = chooser.showOpenDialog(photoView.getScene().getWindow());
        if (selected != null) {
            photo = selected;
            photoView.setImage(new Image(selected.toURI().toString()));
        }
    }

    @FXML
    private void addEmployee() {
        int employeeId;
        long mobile;
        try {
            employeeId = Integer.parseInt(employeeIdField.getText());
            mobile = Long.parseLong(mobileField.getText());
        } catch (NumberFormatException e) {
            Alerts.info("Invalid Employeeid/Mobile NO.");
            return;
        }
        String name = nameField.getText();
        String fatherName = fatherNameField.getText();
        String address = addressArea.getText();
        String password = String.valueOf((int) (10000 + (99999 - 10000) * Math.random()));

        if (name.isEmpty() || fatherName.isEmpty() || address.isEmpty() || photo == null) {
            Alerts.info("all fields are mandatory");
        } else if (mobileField.getText().length() != 10) {
            Alerts.info("enter 10 digit mobile no.");
        } else {
            try {
                if (api.addEmployee(employeeId, name, fatherName, address, mobile, password, photo)) {
                    Alerts.info(" employee added successfully");
                    String sms = "your login detail for employee \n"
                            + "employee id . " + employeeId + "\n"
                            + "password : " + password;
                    smsSender.send(String.valueOf(mobile), sms);
                } else {
                    Alerts.info("Duplicate Employee id");
                }
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        }
    }
}
