package org.Presentation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import eu.hansolo.medusa.Gauge;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import org.Logic.Dashboard;

public class DashboardController {
    private Dashboard dashboard;
    public ImageView IVindicatorsTurnRight;
    public ImageView IVindicatorsTurnLeft;
    public ImageView IVparkingLights;
    public ImageView IVheadlightsLowBeam;
    public ImageView IVheadlightsHighBeam;
    public ImageView IVfogLightsBack;
    public ImageView IVfogLightsFront;
    public Gauge speedGauge;
    public Gauge revsGauge;
    public BorderPane BPmain;
    public MenuItem MIexit;
    private Map<String, ImageView> lightsImg;

    @FXML
    private void initialize() {
        this.dashboard = new Dashboard();
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
    private void openNewWindow(ActionEvent actionEvent) throws IOException {
        MenuItem menuItem = (MenuItem)actionEvent.getSource();
        String filename, title;
        switch (menuItem.getId()) {
            case "MIabout":
                    filename = "about.fxml";
                    title = "About";
                break;
            case "MIsettings":
                    filename = "settings.fxml";
                    title = "Settings";
                break;
            default:
                filename ="";
                title ="";
        }

        FXMLLoader root = new FXMLLoader(GUI.class.getResource(filename));
        Scene scene = new Scene(root.load());
        if(filename.equals("settings.fxml")) {
            SettingsController settingsController = root.getController();
            settingsController.loadSettings(this.dashboard.getSettings(), this);
        }

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void lightSwitch(ActionEvent actionEvent) {
        CheckMenuItem source = (CheckMenuItem)actionEvent.getSource();
        String id = source.getId();
        Image newImg;

        if(source.isSelected())
            newImg = new Image(getClass().getResourceAsStream("images/"+id+"On.png"));
        else
            newImg = new Image(getClass().getResourceAsStream("images/"+id+"Off.png"));

        lightsImg.get(id).setImage(newImg);
    }

    public void reload() {
        speedGauge.setMaxValue(dashboard.getSettings().maxSpeed);
        revsGauge.setMaxValue(dashboard.getSettings().maxRevs);
        int color = dashboard.getSettings().dashboardLightIntesity;
        BPmain.setStyle("-fx-background-color: rgb("+color+", "+color+", "+color+");");
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Platform.exit();
    }
}
