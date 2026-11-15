package com.parking.client.controller;

import com.parking.client.model.AccountType;
import com.parking.client.util.ScreenManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdminHomeController {

    @FXML
    private Label welcomeLabel;

    private String username;

    public void init(String username) {
        this.username = username;
        welcomeLabel.setText("Welcome, " + username);
    }

    @FXML
    private void changePassword() {
        ScreenManager.<ChangePasswordController>open("change-password.fxml", "Change Password",
                c -> c.init(AccountType.ADMIN, username));
    }

    @FXML
    private void manageRent() {
        ScreenManager.open("manage-rent.fxml", "Parking Rent");
    }

    @FXML
    private void addEmployee() {
        ScreenManager.open("add-employee.fxml", "Add Employee");
    }

    @FXML
    private void viewEmployees() {
        ScreenManager.open("view-employees.fxml", "View Employees");
    }

    @FXML
    private void dailySale() {
        ScreenManager.open("daily-sale.fxml", "Daily Sale");
    }
}
