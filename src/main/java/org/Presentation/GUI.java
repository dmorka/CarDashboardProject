package org.Presentation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lk.vivoxalabs.customstage.*;
import lk.vivoxalabs.customstage.tools.NavigationType;
import lk.vivoxalabs.customstage.tools.Style;

import java.io.File;
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
        String absolutePath =  (new File("src\\main\\resources\\org\\Presentation\\images").getAbsolutePath())+"\\";
        AnchorPane nav = new AnchorPane();
        nav.setStyle("-fx-background-color: #ffd700;");
        Image image = new Image(String.valueOf(GUI.class.getResource("images/close.png")));
        CustomStage customStage = new CustomStageBuilder()
                .setActionIcons(image, image, image, image)
                .setIcon(String.valueOf(GUI.class.getResource("images/close.png")))
                .setNavigationPane(Style.DYNAMIC, NavigationType.LEFT,nav,50,0,true)
                .build();

        customStage.setScene(scene);
        dashboardController =  fxmlLoader.getController();
        customStage.show();
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