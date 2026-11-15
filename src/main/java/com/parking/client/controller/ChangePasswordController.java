package com.parking.client.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.parking.client.api.ParkingApi;
import com.parking.client.model.AccountType;
import com.parking.client.util.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/** Change password screen, used for both admins and employees. */
public class ChangePasswordController {

    @FXML
    private Label titleLabel;
    @FXML
    private Label userLabel;
    @FXML
    private TextField userField;
    @FXML
    private PasswordField oldPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;

    private final ParkingApi api = new ParkingApi();
    private AccountType type;
    private String user;

    public void init(AccountType type, String user) {
        this.type = type;
        this.user = user;
        titleLabel.setText(type.getChangePasswordTitle());
        userLabel.setText(type.getUserLabel());
        userField.setText(user);
    }

    @FXML
    private void changePassword() {
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Alerts.info("all fields are mandatory");
        } else if (!newPassword.equals(confirmPassword)) {
            Alerts.info("new password and confirm password not matched");
        } else {
            try {
                boolean changed = type == AccountType.ADMIN
                        ? api.adminChangePassword(user, oldPassword, newPassword)
                        : api.employeeChangePassword(user, oldPassword, newPassword);
                Alerts.info(changed ? "password changed successfully" : "invalid old password");
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        }
    }
}
