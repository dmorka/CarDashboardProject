package org.Presentation;

import com.goxr3plus.fxborderlessscene.borderless.BorderlessScene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.cell.ImageGridCell;
import java.io.IOException;


public class GUI extends Application {
    private DashboardController dashboardController;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("dashboard.fxml"));
        fxmlLoader.load();
        dashboardController =  fxmlLoader.getController();
        StackPane stackPane = (StackPane) fxmlLoader.getNamespace().get("stackPane");

        ImageGridCell closeIcon = (ImageGridCell) fxmlLoader.getNamespace().get("closeIcon");
        closeIcon.setOnMouseClicked(event -> {
            stage.close();
        });
                            

        BorderlessScene scene = new BorderlessScene(stage, StageStyle.UNDECORATED, stackPane, 250,250);
        stage.setScene(scene);

        scene.removeDefaultCSS();

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

        scene.setMoveControl((GridPane)fxmlLoader.getNamespace().get("topPanel"));

        //Show
        stage.setTitle("Dashboard");
        stage.show();
    }

    @Override
    public void stop() {
        dashboardController.onStageDestruction();
    }


    private static FXMLLoader loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource(fxml + ".fxml"));

        return fxmlLoader;
    }

    public static void main() {
        launch();
    }

}