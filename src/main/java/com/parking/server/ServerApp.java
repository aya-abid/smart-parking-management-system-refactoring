package com.parking.server;

import com.parking.common.Endpoints;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/** Small window to start and stop the parking server. */
public class ServerApp extends Application {

    private final Button startButton = new Button("start server");
    private final Button stopButton = new Button("stop server");
    private final Label statusLabel = new Label();

    private ParkingServer server;

    @Override
    public void start(Stage stage) {
        startButton.setOnAction(e -> startServer());
        stopButton.setOnAction(e -> stopServer());
        startButton.setPrefWidth(140);
        stopButton.setPrefWidth(140);

        VBox root = new VBox(30, statusLabel, startButton, stopButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        updateButtons();

        stage.setTitle("Parking Server");
        stage.setScene(new Scene(root, 500, 400));
        stage.setOnCloseRequest(e -> stopServer());
        stage.show();
    }

    private void startServer() {
        try {
            server = new ParkingServer(Endpoints.PORT);
            System.out.println("server start");
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateButtons();
    }

    private void stopServer() {
        if (server != null) {
            server.shutdown();
            server = null;
            System.out.println("server shutdown");
        }
        updateButtons();
    }

    private void updateButtons() {
        boolean running = server != null;
        startButton.setDisable(running);
        stopButton.setDisable(!running);
        statusLabel.setText(running ? "Server is running on port " + Endpoints.PORT : "Server is stopped");
    }
}
