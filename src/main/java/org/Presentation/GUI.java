package org.Presentation;

import com.goxr3plus.fxborderlessscene.borderless.BorderlessScene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.cell.ImageGridCell;
import org.w3c.dom.ls.LSOutput;

import java.io.IOException;


/**
 * The type Gui.
 */
public class GUI extends Application {
    private DashboardController dashboardController;

    /**
     * Main.
     */
    public static void main() {
        launch();
    }

    /**
     * Instantiates a new Gui.
     */
    public GUI() {
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("dashboard.fxml"));
        fxmlLoader.load();
        dashboardController = fxmlLoader.getController();
        StackPane stackPane = (StackPane) fxmlLoader.getNamespace().get("stackPane");

        createBorderlessScene(stage,stackPane, "Dashboard", fxmlLoader);

        ImageView closeIcon = (ImageView) fxmlLoader.getNamespace().get("closeIcon");
        closeIcon.setOnMouseClicked(event -> {
            stage.close();
            stop();
        });

        stage.getIcons().add(new Image(GUI.class.getResourceAsStream("images/icon.png")));
        stage.show();
    }

    @Override
    public void stop() {
        dashboardController.onStageDestruction();
    }

    static void createBorderlessScene(Stage stage, Pane pane, String title, FXMLLoader fxmlLoader) {
        BorderlessScene scene = new BorderlessScene(stage, StageStyle.UNDECORATED, pane, 250, 250);
        stage.setScene(scene);
        scene.removeDefaultCSS();
        ImageView minimizeIcon = (ImageView) fxmlLoader.getNamespace().get("minimizeIcon");
        minimizeIcon.setOnMouseClicked(event -> {
            scene.minimizeStage();
        });

        ImageView closeIcon = (ImageView) fxmlLoader.getNamespace().get("closeIcon");
        closeIcon.setOnMouseClicked(event -> {
            stage.close();
        });

        ImageView maximizeIcon = (ImageView) fxmlLoader.getNamespace().get("maximizeIcon");
        Image maxi = new Image(GUI.class.getResourceAsStream("images/window button icons/maximize.png"));
        Image mini = new Image(GUI.class.getResourceAsStream("images/window button icons/minimize.png"));
        FXMLLoader finalRoot = fxmlLoader;
        maximizeIcon.setOnMouseClicked(event -> {
            scene.maximizeStage();
            if (scene.isMaximized()) {
                ((ImageView) finalRoot.getNamespace().get("maximizeIcon")).setImage(mini);
            }
            else {
                ((ImageView) finalRoot.getNamespace().get("maximizeIcon")).setImage(maxi);
            }

        });

        scene.setMoveControl((Parent) fxmlLoader.getNamespace().get("topPanel"));

        stage.setTitle(title);
    }

}