package com.parking.client;

import com.mashape.unirest.http.Unirest;
import com.parking.client.util.ScreenManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        ScreenManager.open("welcome.fxml", "Welcome");
    }

    @Override
    public void stop() throws IOException {
        // Unirest keeps background threads alive otherwise
        Unirest.shutdown();
    }
}
