package org.Presentation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


/**
 * JavaFX GUI
 */
public class GUI extends Application {
    private static Scene scene;
    private DashboardController dashboardController;

    @Override
    public void start(Stage stage) throws IOException {
        //TODO: Try to remove windows borders
        //stage.initStyle(StageStyle.UNDECORATED);
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("dashboard.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        dashboardController =  fxmlLoader.getController();
        stage.show();
    }

    @Override
    public void stop() {
        dashboardController.onStageDestruction();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml).load());
    }

    private static FXMLLoader loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource(fxml + ".fxml"));

        return fxmlLoader;
    }

    public static void main(String[] args) {
        launch();
    }

}