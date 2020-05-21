package org.Presentation;

import com.goxr3plus.fxborderlessscene.borderless.BorderlessScene;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lk.vivoxalabs.customstage.*;
import lk.vivoxalabs.customstage.tools.NavigationType;
import lk.vivoxalabs.customstage.tools.Style;
import org.Logic.TurnSignalException;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.cell.ImageGridCell;

import java.awt.*;
import java.beans.EventHandler;
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
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("dashboard.fxml"));
        scene = new Scene(fxmlLoader.load());
        dashboardController =  fxmlLoader.getController();
        StackPane stackPane = (StackPane) fxmlLoader.getNamespace().get("stackPane");

        //Close Button
        ImageGridCell closeIcon = (ImageGridCell) fxmlLoader.getNamespace().get("closeIcon");
        closeIcon.setOnMouseClicked(event -> {
            stage.close();
        });
                            

        BorderlessScene scene = new BorderlessScene(stage, StageStyle.UNDECORATED, stackPane, 250,250);
        stage.setScene(scene);

        //remove the default css style
        scene.removeDefaultCSS();

        // Maximise (on/off) and minimise the application:
        ImageGridCell minimizeIcon = (ImageGridCell) fxmlLoader.getNamespace().get("minimizeIcon");
        minimizeIcon.setOnMouseClicked(event -> {
            scene.minimizeStage();
        });
        ImageGridCell maximizeIcon = (ImageGridCell) fxmlLoader.getNamespace().get("maximizeIcon");
        Image maxi = new Image(GUI.class.getResourceAsStream("images/maximize.png"));
        Image mini = new Image(GUI.class.getResourceAsStream("images/minimize.png"));
        maximizeIcon.setOnMouseClicked(event -> {
            scene.maximizeStage();
            if(scene.isMaximized())
                ((ImageView)fxmlLoader.getNamespace().get("IVmaximizeApp")).setImage(mini);
            else
                ((ImageView)fxmlLoader.getNamespace().get("IVmaximizeApp")).setImage(maxi);

        });

        // To move the window around by pressing a node:
        scene.setMoveControl((GridPane)fxmlLoader.getNamespace().get("topPanel"));

        //Show
        stage.setTitle("Dashboard");
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