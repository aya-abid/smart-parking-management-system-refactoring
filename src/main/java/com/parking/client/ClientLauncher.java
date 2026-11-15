package com.parking.client;

import javafx.application.Application;

/** Main class of the desktop app (a separate launcher is needed when JavaFX is on the classpath). */
public class ClientLauncher {

    public static void main(String[] args) {
        Application.launch(ClientApp.class, args);
    }
}
