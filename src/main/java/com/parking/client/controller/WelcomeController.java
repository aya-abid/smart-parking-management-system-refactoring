package com.parking.client.controller;

import com.parking.client.util.ScreenManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WelcomeController {

    @FXML
    private ProgressBar progressBar;

    @FXML
    private void initialize() {
        Timeline loading = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(2), new KeyValue(progressBar.progressProperty(), 1)));
        loading.setOnFinished(e -> {
            ScreenManager.open("choose-login.fxml", "Choose Login");
            ((Stage) progressBar.getScene().getWindow()).close();
        });
        loading.play();
    }
}
