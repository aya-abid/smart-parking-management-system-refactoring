package com.parking.client.util;

import javafx.scene.control.Alert;

/** Simple information message box. */
public final class Alerts {

    private Alerts() {
    }

    public static void info(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setHeaderText(null);
        alert.setTitle("Message");
        alert.showAndWait();
    }
}
