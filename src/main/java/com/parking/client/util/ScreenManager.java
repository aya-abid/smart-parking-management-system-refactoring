package com.parking.client.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Consumer;

/** Opens a screen from its FXML file in a new window. */
public final class ScreenManager {

    private static final String VIEWS = "/com/parking/client/views/";
    private static final String CSS = "/com/parking/client/style.css";

    private ScreenManager() {
    }

    public static Stage open(String fxml, String title) {
        return open(fxml, title, controller -> { });
    }

    /** @param init called with the controller before the window is shown (to pass the user name, ...) */
    public static <T> Stage open(String fxml, String title, Consumer<T> init) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenManager.class.getResource(VIEWS + fxml));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(ScreenManager.class.getResource(CSS).toExternalForm());

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            init.accept(loader.getController());
            stage.show();
            return stage;
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot load " + fxml, e);
        }
    }
}
