package org.Presentation;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;


public class MainController {
    @FXML
    private void onMouseClicked(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        if (button.getId().equals("BTNtui")) {
            TUI.main(new String[]{});
        } else {
            GUI gui = new GUI();
            try {
                gui.start(new Stage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
}
