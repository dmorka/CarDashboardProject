package org.cardashboardproject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PrimaryController {
    public ImageView IVindicatorsTurnRight;
    public ImageView IVindicatorsTurnLeft;
    public ImageView IVparkingLights;
    public ImageView IVheadlightsLowBeam;
    public ImageView IVheadlightsHighBeam;
    public ImageView IVfogLightsBack;
    public ImageView IVfogLightsFront;
    private Map<String, ImageView> lightsImg;

    @FXML
    public void initialize() {
         lightsImg = new HashMap<>() {
            {
                put("indicatorsTurnRight",IVindicatorsTurnRight);
                put("indicatorsTurnLeft",IVindicatorsTurnLeft);
                put("parkingLights",IVparkingLights);
                put("headlightsLowBeam",IVheadlightsLowBeam);
                put("headlightsHighBeam",IVheadlightsHighBeam);
                put("fogLightsBack",IVfogLightsBack);
                put("fogLightsFront",IVfogLightsFront);
            }
        };
    }

    @FXML
    private void openAbout() throws IOException {
        Parent root = new FXMLLoader(App.class.getResource("about.fxml")).load();
        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setTitle("About");
        stage.setScene(scene);
        stage.show();
    }

    public void lightSwitch(javafx.event.ActionEvent actionEvent) {
        CheckMenuItem source = (CheckMenuItem)actionEvent.getSource();
        String id = source.getId();
        Image newImg;

        if(source.isSelected())
            newImg = new Image(getClass().getResourceAsStream("images/"+id+"On.png"));
        else
            newImg = new Image(getClass().getResourceAsStream("images/"+id+"Off.png"));

        lightsImg.get(id).setImage(newImg);
    }
}
