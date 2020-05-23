package org.Presentation;

import com.goxr3plus.fxborderlessscene.borderless.BorderlessScene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("main.fxml"));
        fxmlLoader.load();

        VBox vbox = (VBox) fxmlLoader.getNamespace().get("vbox");

        ImageView closeIcon = (ImageView) fxmlLoader.getNamespace().get("closeIcon");
        closeIcon.setOnMouseClicked(event -> {
            stage.close();
        });


        BorderlessScene scene = new BorderlessScene(stage, StageStyle.UNDECORATED, vbox, 300, 160);
        stage.setScene(scene);

        scene.removeDefaultCSS();

        scene.setMoveControl((HBox) fxmlLoader.getNamespace().get("topPanel"));
        scene.setResizable(false);
        //Show
        stage.setTitle("Dashboard mode");
        stage.show();


    }


}
