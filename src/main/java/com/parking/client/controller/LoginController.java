package com.parking.client.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.parking.client.api.ParkingApi;
import com.parking.client.model.AccountType;
import com.parking.client.util.Alerts;
import com.parking.client.util.ScreenManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/** Login screen, used for both admins and employees. */
public class LoginController {

    @FXML
    private Label titleLabel;
    @FXML
    private Label userLabel;
    @FXML
    private TextField userField;
    @FXML
    private PasswordField passwordField;

    private final ParkingApi api = new ParkingApi();
    private AccountType type;

    public void init(AccountType type) {
        this.type = type;
        titleLabel.setText(type.getLoginTitle());
        userLabel.setText(type.getUserLabel());
    }

    @FXML
    private void login() {
        String user = userField.getText();
        String password = passwordField.getText();
        if (user.isEmpty() || password.isEmpty()) {
            Alerts.info("empty username or password");
            return;
        }
        try {
            boolean success = type == AccountType.ADMIN
                    ? api.adminLogin(user, password)
                    : api.employeeLogin(user, password);
            if (!success) {
                Alerts.info("Login Failed");
                return;
            }
            Alerts.info("Login Success");
            if (type == AccountType.ADMIN) {
                ScreenManager.<AdminHomeController>open("admin-home.fxml", "Admin Home", c -> c.init(user));
            } else {
                ScreenManager.<EmployeeHomeController>open("employee-home.fxml", "Employee Home", c -> c.init(user));
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
