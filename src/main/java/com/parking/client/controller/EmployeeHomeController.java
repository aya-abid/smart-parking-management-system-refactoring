package com.parking.client.controller;

import com.parking.client.model.AccountType;
import com.parking.client.model.ParkingBay;
import com.parking.client.util.ScreenManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EmployeeHomeController {

    @FXML
    private Label welcomeLabel;
    @FXML
    private Button bayAButton;
    @FXML
    private Button bayBButton;

    private String empId;

    @FXML
    private void initialize() {
        setPicture(bayAButton, ParkingBay.A);
        setPicture(bayBButton, ParkingBay.B);
    }

    public void init(String empId) {
        this.empId = empId;
        welcomeLabel.setText("Welcome, " + empId);
    }

    private void setPicture(Button button, ParkingBay bay) {
        String image = "/com/parking/client/images/" + bay.getImageName();
        ImageView view = new ImageView(new Image(getClass().getResource(image).toExternalForm()));
        view.setFitHeight(120);
        view.setPreserveRatio(true);
        button.setGraphic(view);
    }

    @FXML
    private void changePassword() {
        ScreenManager.<ChangePasswordController>open("change-password.fxml", "Change Password",
                c -> c.init(AccountType.EMPLOYEE, empId));
    }

    @FXML
    private void openBayA() {
        openBay(ParkingBay.A);
    }

    @FXML
    private void openBayB() {
        openBay(ParkingBay.B);
    }

    private void openBay(ParkingBay bay) {
        ScreenManager.<ParkingBayController>open("parking-bay.fxml", bay.getBayName(), c -> c.init(bay, empId));
    }
}
