package org.Presentation;

import java.io.IOException;
import javafx.fxml.FXML;
import org.Logic.Settings;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}