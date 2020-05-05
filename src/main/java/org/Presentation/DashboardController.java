package org.Presentation;

import java.io.IOException;
import eu.hansolo.medusa.Gauge;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import org.Logic.Dashboard;
import org.Logic.FlashingSignalThread;
import org.Logic.TurnSignalException;

public class DashboardController {
    private Dashboard dashboard;
    private FlashingSignalThread flashingSignalThread;
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
    public CheckMenuItem indicatorsTurnLeft;
    public CheckMenuItem indicatorsTurnRight;

    @FXML
    private void initialize() {
        this.dashboard = new Dashboard();
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

    public void openDialog(AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public void lightSwitch(ActionEvent actionEvent)  {
        CheckMenuItem checkMenuItem = (CheckMenuItem) actionEvent.getSource();
        String id = checkMenuItem.getId();
        Image newImage;
        boolean enable;
        if (checkMenuItem.isSelected()) {
            newImage = new Image(getClass().getResourceAsStream("images/" + id + "On.png"));
            enable = true;
        } else {
            newImage = new Image(getClass().getResourceAsStream("images/" + id + "Off.png"));
            enable = false;
        }
        switch (id) {
            case "indicatorsTurnLeft":
                try{
                    dashboard.setLeftTurnSignal(enable);
                } catch (TurnSignalException e) {
                    openDialog(AlertType.ERROR,
                            "Error Dialog",
                            "Turn Signal Exception",
                            "You cannot turn on both direction indicators at the same time!");

                    checkMenuItem.setSelected(false);
                    break;
                }
                indicatorSwitch(IVindicatorsTurnLeft, newImage, enable);
                break;

            case "indicatorsTurnRight":
                try{
                    dashboard.setRightTurnSignal(enable);
                } catch (TurnSignalException e) {
                    openDialog(AlertType.ERROR,
                            "Error Dialog",
                            "Turn Signal Exception",
                            "You cannot turn on both direction indicators at the same time!");
                    checkMenuItem.setSelected(false);
                    break;
                }
                indicatorSwitch(IVindicatorsTurnRight, newImage, enable);
                break;

            case "parkingLights":
                dashboard.setPositionLights(enable);
                lightSwitch(IVparkingLights, newImage, enable);
                break;

            case "headlightsLowBeam":
                dashboard.setLowBeam(enable);
                lightSwitch(IVheadlightsLowBeam, newImage, enable);
                break;

            case "headlightsHighBeam":
                dashboard.setHighBeam(enable);
                lightSwitch(IVheadlightsHighBeam, newImage, enable);
                break;

            case "fogLightsFront":
                dashboard.setFrontFogLights(enable);
                lightSwitch(IVfogLightsFront, newImage, enable);
                break;

            case "fogLightsBack":
                dashboard.setRearFogLights(enable);
                lightSwitch(IVfogLightsBack, newImage, enable);
                break;
        }
    }

    public void lightSwitch(ImageView imageView, Image newImage, boolean enable) {
        imageView.setImage(newImage);
        imageView.setOpacity(enable ? 0.88 : 0.2);
    }

    public void indicatorSwitch(ImageView imageView, Image newImage, boolean enable) {
        if (enable) {
            flashingSignalThread = new FlashingSignalThread(imageView, newImage);
            flashingSignalThread.setRunning(true);
            flashingSignalThread.start();
        } else {
            flashingSignalThread.setRunning(false);
            flashingSignalThread.interrupt();
            imageView.setImage(newImage);
            imageView.setOpacity(0.2);
        };
    }

    public void reloadAfterSettings() {
        speedGauge.setMaxValue(dashboard.getSettings().maxSpeed);
        revsGauge.setMaxValue(dashboard.getSettings().maxRevs);
        int color = dashboard.getSettings().dashboardLightIntesity;
        BPmain.setStyle("-fx-background-color: rgb("+color+", "+color+", "+color+");");
        dashboard.setGears();
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.UP) {
            dashboard.setKeyUp(true);
        } else if (event.getCode() == KeyCode.DOWN) {
            dashboard.setKeyDown(true);
        } else if ((event.getCode() == KeyCode.LEFT) && !this.dashboard.isLeftTurnSignal()) {
            try {
                dashboard.setLeftTurnSignal(true);
                indicatorSwitch(IVindicatorsTurnLeft,
                        new Image(getClass().getResourceAsStream("images/indicatorsTurnLeftOn.png")),
                        true);
            } catch (TurnSignalException e) {
                openDialog(AlertType.ERROR,
                        "Error Dialog",
                        "Turn Signal Exception",
                        "You cannot turn on both direction indicators at the same time!");
            }
        } else if ((event.getCode() == KeyCode.RIGHT) && (!this.dashboard.isRightTurnSignal())) {
            try {
                dashboard.setRightTurnSignal(true);
                indicatorSwitch(IVindicatorsTurnRight,
                        new Image(getClass().getResourceAsStream("images/indicatorsTurnRightOn.png")),
                        true);
            } catch (TurnSignalException e) {
                openDialog(AlertType.ERROR,
                        "Error Dialog",
                        "Turn Signal Exception",
                        "You cannot turn on both direction indicators at the same time!");
            }
        }
    }

    @FXML
    private void keyReleased(KeyEvent event) throws TurnSignalException {
        if (event.getCode() == KeyCode.UP) {
            dashboard.setKeyUp(false);
        } else if (event.getCode() == KeyCode.DOWN) {
            dashboard.setKeyDown(false);
        } else if (event.getCode() == KeyCode.LEFT) {
            dashboard.setLeftTurnSignal(false);
            indicatorsTurnLeft.setSelected(false);
            indicatorSwitch(IVindicatorsTurnLeft,
                    new Image(getClass().getResourceAsStream("images/indicatorsTurnLeftOff.png")),
                    false);
        } else if (event.getCode() == KeyCode.RIGHT) {
            dashboard.setRightTurnSignal(false);
            indicatorsTurnRight.setSelected(false);
            indicatorSwitch(IVindicatorsTurnRight,
                    new Image(getClass().getResourceAsStream("images/indicatorsTurnRightOff.png")),
                    false);
        }
    }
}
