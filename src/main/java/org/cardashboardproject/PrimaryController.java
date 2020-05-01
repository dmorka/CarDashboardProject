package org.cardashboardproject;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PrimaryController {

    @FXML
    private void openAbout() throws IOException {
        Parent root = new FXMLLoader(App.class.getResource("about.fxml")).load();
        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setTitle("About");
        stage.setScene(scene);
        stage.show();
    }
}
