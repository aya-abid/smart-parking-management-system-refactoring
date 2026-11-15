package com.parking.client.controller;

import com.parking.client.model.AccountType;
import com.parking.client.util.ScreenManager;
import javafx.fxml.FXML;

public class ChooseLoginController {

    @FXML
    private void adminLogin() {
        openLogin(AccountType.ADMIN);
    }

    @FXML
    private void employeeLogin() {
        openLogin(AccountType.EMPLOYEE);
    }

    private void openLogin(AccountType type) {
        ScreenManager.<LoginController>open("login.fxml", type.getLoginTitle(), c -> c.init(type));
    }
}
