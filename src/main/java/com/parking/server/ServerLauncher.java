package com.parking.server;

import javafx.application.Application;

/** Main class of the server (a separate launcher is needed when JavaFX is on the classpath). */
public class ServerLauncher {

    public static void main(String[] args) {
        Application.launch(ServerApp.class, args);
    }
}
