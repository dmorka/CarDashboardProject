package org.Presentation;

import com.goxr3plus.fxborderlessscene.borderless.BorderlessScene;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import eu.hansolo.medusa.Gauge;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.Data.LoadFilesFromDisk;
import org.Data.Serialization;
import org.Logic.*;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * The type Dashboard controller.
 */
public class DashboardController extends UIController {
    /**
     * Instantiates a new Dashboard controller.
     */
    public DashboardController() {
    }

    private FlashingSignalThread flashingSignalThread;
    private SpeedThread speedThread;
    private Timeline progressBar;
    @FXML
    private CheckMenuItem cruiseControl;
    @FXML
    private Menu McruiseControl;
    @FXML
    private StackPane stackPane;
    @FXML
    private HashMap<String, Image[]> lights;
    @FXML
    private ImageView IVindicatorsTurnRight;
    @FXML
    private ImageView IVindicatorsTurnLeft;
    @FXML
    private ImageView IVparkingLights;
    @FXML
    private ImageView IVheadlightsLowBeam;
    @FXML
    private ImageView IVheadlightsHighBeam;
    @FXML
    private ImageView IVfogLightsBack;
    @FXML
    private ImageView IVfogLightsFront;
    @FXML
    private ImageView IVcruiseControl;
    @FXML
    private Gauge speedGauge;
    @FXML
    private Gauge revsGauge;
    @FXML
    private GridPane GPmain;
    @FXML
    private MenuItem MIexit;
    @FXML
    private MenuItem MIstartEngine;
    @FXML
    private MenuItem MIstopEngine;
    @FXML
    private MenuItem MIsettings;
    @FXML
    private Menu Mindicators;
    @FXML
    private Menu Mimport;
    @FXML
    private CheckMenuItem indicatorsTurnLeft;
    @FXML
    private CheckMenuItem indicatorsTurnRight;
    @FXML
    private CheckMenuItem headlightsLowBeam;
    @FXML
    private CheckMenuItem headlightsHighBeam;
    @FXML
    private CheckMenuItem fogLightsFront;
    @FXML
    private CheckMenuItem fogLightsBack;
    @FXML
    private CheckMenuItem parkingLights;
    @FXML
    private Text TXTclock;
    @FXML
    private Text TXTgear;
    @FXML
    private Text TXTavgSpeed;
    @FXML
    private Text TXTmaxSpeed;
    @FXML
    private Text TXTavgFuelUsage;
    @FXML
    private Text TXTmaxFuelUsage;
    @FXML
    private Text TXTmainCounter;
    @FXML
    private Text TXTdayCounter1;
    @FXML
    private Text TXTdayCounter2;
    @FXML
    private Text TXTjourneyDistance;
    @FXML
    private Text TXTjourneyTime;
    @FXML
    private Label LtitleMP;
    @FXML
    private Label LartistMP;
    @FXML
    private Slider SLvolume;
    @FXML
    private Polygon PolyPlay;
    @FXML
    private Rectangle RecPause1;
    @FXML
    private Rectangle RecPause2;
    @FXML
    private ProgressBar PBsongDuration;

    @FXML
    private void initialize() {
        LoadFilesFromDisk loadFilesFromDisk = new LoadFilesFromDisk();
        lights = loadFilesFromDisk.loadLights();
        this.dashboard.getMusicPlayer().setAutoPlayNext(this::nextSongMP);
        initClock();
        reloadAfterSettings();
        refresh();
        progressBar = null;
        setTitleArtist();
    }

    @FXML
    private void playPauseMP() {
        if (dashboard.getMusicPlayer().isEmpty())
            return;

        if (!dashboard.getMusicPlayer().isPlaying()) {
            musicPlayButtonSwitch(false);
            dashboard.getMusicPlayer().playSong();
            LtitleMP.setOpacity(1);
            LartistMP.setOpacity(1);
            progressBarMP(false, false);
            dashboard.getMusicPlayer().setAutoPlayNext(this::nextSongMP);
        } else {
            musicPlayButtonSwitch(true);
            dashboard.getMusicPlayer().pauseSong();
            progressBarMP(false, true);
        }
        setTitleArtist();
    }

    private void musicPlayButtonSwitch(boolean showPlay) {
        PolyPlay.setVisible(showPlay);
        RecPause1.setVisible(!showPlay);
        RecPause2.setVisible(!showPlay);
    }

    @FXML
    private void progressBarMP(boolean resetProgress, boolean pause) {
        if (resetProgress && progressBar != null) {
            progressBar.stop();
            progressBar = null;
            PBsongDuration.setProgress(0.0);
        }
        if (dashboard.getMusicPlayer().isEmpty())
            return;
        if (!pause) {
            double progressValue = 1 / dashboard.getMusicPlayer().getTotalDuration().toSeconds();
            if (progressBar == null && !Double.isNaN(progressValue)) {
                progressBar = new Timeline(new KeyFrame(Duration.ZERO, e -> {
                    PBsongDuration.setProgress(PBsongDuration.getProgress() + progressValue);
                }), new KeyFrame(Duration.seconds(1)));
                progressBar.setCycleCount((int) dashboard.getMusicPlayer().getTotalDuration().toSeconds() + 1);
            }
            if (progressBar != null)
                progressBar.play();
        } else if (progressBar != null)
            progressBar.pause();
    }

    @FXML
    private void nextSongMP() {
        dashboard.getMusicPlayer().nextSong();
        progressBarMP(true, PolyPlay.isVisible());
        setTitleArtist();
    }

    @FXML
    private void previousSong() {
        dashboard.getMusicPlayer().previousSong();
        progressBarMP(true, PolyPlay.isVisible());
        setTitleArtist();
    }

    @FXML
    private void changeVolumeMP() {
        dashboard.getMusicPlayer().changeVolume(SLvolume.getValue() / 100);
    }

    @FXML
    private void setTitleArtist() {
        LtitleMP.setText(dashboard.getMusicPlayer().getTitle());
        LartistMP.setText(dashboard.getMusicPlayer().getArtist());
    }

    @FXML
    private void openNewWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader root = null;
        Stage stage = new Stage();
        MenuItem menuItem = (MenuItem) actionEvent.getSource();
        String title;
        DatabaseImportController dbController = null;
        switch (menuItem.getId()) {
            case "MIabout":
                root = loadFXML("about.fxml");
                title = "About";
                break;
            case "MIsettings":
                root = loadFXML("settings.fxml");
                SettingsController settingsController = root.getController();
                settingsController.lockSettings(MIstartEngine.isDisable());
                settingsController.loadSettings(this.dashboard.getSettings(), this);
                title = "Settings";
                break;
            case "MIdatabaseImport":
                root = loadFXML("databaseImport.fxml");
                dbController = root.getController();
                dbController.loadDB(dashboard.readFromDB());
                title = "SQL Import";
                break;
            case "MIkeyShortcuts":
                root = loadFXML("keyShortcuts.fxml");
                title = "Key shortcuts";
                break;
            default:
                title = "";
        }

        assert root != null;
        Pane pane =  (Pane) root.getNamespace().get("mainPane");

        GUI.createBorderlessScene(stage,pane,title,root);

        stage.show();

        DatabaseImportController finalDbController = dbController;
        stage.setOnCloseRequest(e -> {
            if (stage.getTitle().equals("SQL Import")) {
                assert finalDbController != null;
                if (finalDbController.getSelectedRecord() != null) {
                    dashboard.updateDashboard(finalDbController.getSelectedRecord());
                    refresh();
                }
            }
        });

    }

    @FXML
    private FXMLLoader loadFXML(String filename) {
        FXMLLoader root = new FXMLLoader(GUI.class.getResource(filename));
        try {
            root.load();
        } catch (IOException e) {
            openDialog(e.getClass().getSimpleName(), e.getMessage());
        }
        return root;
    }

    @FXML
    private void importFromXML(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML file (*.xml)", "*.xml"));
        File selectedDirectory = fileChooser.showOpenDialog(GPmain.getScene().getWindow());
        if (selectedDirectory != null) {
            try {
                dashboard.readFromXml(selectedDirectory.getPath());
                refresh();
            } catch (IOException | XMLStreamException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void exportToXML(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML file (*.xml)", "*.xml"));
        File selectedDirectory = fileChooser.showSaveDialog(GPmain.getScene().getWindow());
        if (selectedDirectory != null) {
            try {
                dashboard.writeToXml(selectedDirectory.getPath());
            } catch (IOException | XMLStreamException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void openDialog(String header, String message) {
        BoxBlur blur = new BoxBlur(4, 4, 4);
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.getStyleClass().add("dialog-layout");
        dialogLayout.setHeading(new Text(header));
        dialogLayout.setBody(new Text(message));
        JFXDialog dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.TOP);
        dialog.getStyleClass().add("dialog-dialog");
        JFXButton button = new JFXButton("Okay");
        button.getStyleClass().add("dialog-button");
        button.setOnAction(
                event -> dialog.close());
        button.setButtonType(JFXButton.ButtonType.RAISED);
        dialogLayout.setActions(button);
        GPmain.setDisable(true);
        dialog.show();
        dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
            GPmain.setEffect(null);
            GPmain.setDisable(false);
        });
        GPmain.setEffect(blur);
    }

    @FXML
    private void menuLightSwitch(ActionEvent actionEvent) {
        CheckMenuItem checkMenuItem = (CheckMenuItem) actionEvent.getSource();
        lightSwitch(checkMenuItem.getId(), checkMenuItem.isSelected());
    }

    private void lightSwitch(String lightName, boolean enable) {
        Image newImage = lights.get(lightName)[(enable) ? 1 : 0];

        switch (lightName) {
            case "indicatorsTurnLeft":
                try {
                    dashboard.setLeftTurnSignal(enable);
                    indicatorsTurnLeft.setSelected(enable);
                } catch (TurnSignalException e) {
                    openDialog(e.getClass().getSimpleName(), e.getMessage());
                    indicatorsTurnLeft.setSelected(false);
                    dashboard.setKeyUp(false);
                    break;
                }
                indicatorSwitch(IVindicatorsTurnLeft, newImage, enable);
                break;

            case "indicatorsTurnRight":
                try {
                    dashboard.setRightTurnSignal(enable);
                    indicatorsTurnRight.setSelected(enable);
                } catch (TurnSignalException e) {
                    openDialog(e.getClass().getSimpleName(), e.getMessage());
                    indicatorsTurnRight.setSelected(false);
                    dashboard.setKeyUp(false);
                    break;
                }
                indicatorSwitch(IVindicatorsTurnRight, newImage, enable);
                break;

            case "parkingLights":
                parkingLights.setSelected(enable);
                dashboard.setPositionLights(enable);
                lightSwitch(IVparkingLights, newImage, enable);
                break;

            case "headlightsLowBeam":
                headlightsLowBeam.setSelected(enable);
                dashboard.setLowBeam(enable);
                lightSwitch(IVheadlightsLowBeam, newImage, enable);
                break;

            case "headlightsHighBeam":
                headlightsHighBeam.setSelected(enable);
                dashboard.setHighBeam(enable);
                lightSwitch(IVheadlightsHighBeam, newImage, enable);
                break;

            case "fogLightsFront":
                fogLightsFront.setSelected(enable);
                dashboard.setFrontFogLights(enable);
                lightSwitch(IVfogLightsFront, newImage, enable);
                break;

            case "fogLightsBack":
                fogLightsBack.setSelected(enable);
                dashboard.setRearFogLights(enable);
                lightSwitch(IVfogLightsBack, newImage, enable);
                break;

            case "cruiseControl":
                try {
                    if (MIstartEngine.isDisable()) {
                        dashboard.setCruiseSpeed(dashboard.getSpeed());
                        cruiseControl.setSelected(enable);
                        dashboard.setCruiseControl(enable);
                        lightSwitch(IVcruiseControl, newImage, enable);
                    }
                } catch (CruiseControlException e) {
                    openDialog(e.getClass().getSimpleName(), e.getMessage());
                    cruiseControl.setSelected(false);
                    dashboard.setKeyUp(false);
                }
                break;
        }
    }

    private void lightSwitch(ImageView imageView, Image newImage, boolean enable) {
        imageView.setImage(newImage);
        imageView.setOpacity(enable ? 0.88 : 0.2);
    }

    private void indicatorSwitch(ImageView imageView, Image newImage, boolean enable) {
        if (enable) {
            flashingSignalThread = new FlashingSignalThread(imageView, newImage);
            flashingSignalThread.setRunning(true);
            flashingSignalThread.start();
        } else {
            flashingSignalThread.setRunning(false);
            flashingSignalThread.interrupt();
            imageView.setImage(newImage);
            imageView.setOpacity(0.2);
        }
        ;
    }

    private void startupLightSwitch() {
        if (dashboard.isPositionLights())
            lightSwitch("parkingLights", true);
        if (dashboard.isLowBeam())
            lightSwitch("headlightsLowBeam", true);
        if (dashboard.isHighBeam())
            lightSwitch("headlightsHighBeam", true);
        if (dashboard.isFrontFogLights())
            lightSwitch("fogLightsFront", true);
        if (dashboard.isBackFogLights())
            lightSwitch("fogLightsBack", true);
    }

    public void reloadAfterSettings() {
        if (dashboard.getMusicPlayer() != null) {
            dashboard.getMusicPlayer().dispose();
            try {
                dashboard.getMusicPlayer().loadSongs(dashboard.getSettings().getPlaylistDirectoryPath());
            } catch (IOException e) {
                openDialog(e.getClass().getSimpleName(), e.getMessage());
            }

            progressBarMP(true, true);
            if (dashboard.getSettings().isShuffleMode())
                dashboard.getMusicPlayer().shufflePlaylist();

            PolyPlay.setVisible(true);
            RecPause1.setVisible(false);
            RecPause2.setVisible(false);

            setTitleArtist();
        }
        TXTgear.setText(String.valueOf(dashboard.getCurrentGear()));
        speedGauge.setMaxValue(dashboard.getSettings().getMaxSpeed());
        revsGauge.setMaxValue(dashboard.getSettings().getMaxRevs());
        int color = dashboard.getSettings().getDashboardLightIntesity();
        GPmain.setStyle("-fx-background-color: rgb(" + color + ", " + color + ", " + color + ");");
        dashboard.setGears();
        if (MIstartEngine.isDisable()) {
            lightSwitch("headlightsLowBeam", true);
        }
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        onStageDestruction();
        Platform.exit();
    }

    /**
     * Key pressed.
     *
     * @param event the event
     */
    @FXML
    void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.UP && !dashboard.isKeyUp()) {
            dashboard.setKeyUp(true);
        } else if (event.getCode() == KeyCode.DOWN) {
            dashboard.setKeyDown(true);
        } else if ((event.getCode() == KeyCode.LEFT) && !this.dashboard.isLeftTurnSignal() && !Mindicators.isDisable()) {
            try {
                dashboard.setLeftTurnSignal(true);
                indicatorsTurnLeft.setSelected(true);
                lightSwitch("indicatorsTurnLeft", true);
            } catch (TurnSignalException e) {
                openDialog(e.getClass().getSimpleName(), e.getMessage());
                dashboard.setKeyUp(false);
            }
        } else if ((event.getCode() == KeyCode.RIGHT) && (!this.dashboard.isRightTurnSignal()) && !Mindicators.isDisable()) {
            try {
                dashboard.setRightTurnSignal(true);
                indicatorsTurnRight.setSelected(true);
                lightSwitch("indicatorsTurnRight", true);
            } catch (TurnSignalException e) {
                openDialog(e.getClass().getSimpleName(), e.getMessage());
                dashboard.setKeyUp(false);
            }
        } else if (event.getText().compareTo("0") >= 0 && 0 >= event.getText().compareTo(String.valueOf(dashboard.getSettings().getNumberOfGears()))) {
            try {
                dashboard.setCurrentGear(Short.parseShort(event.getText()), MIstopEngine.isDisable());
                dashboard.playCarSound(Dashboard.CarSound.GEAR_SHIFT, true);
                refresh();
            } catch (GearException e) {
                openDialog(e.getClass().getSimpleName(), e.getMessage());
            }
        } else if (event.getCode() == KeyCode.H) {
            dashboard.playCarSound(Dashboard.CarSound.HONK, true);
        } else if (event.getCode() == KeyCode.I) {
            playPauseMP();
        } else if (event.getCode() == KeyCode.O) {
            nextSongMP();
        } else if (event.getCode() == KeyCode.U) {
            previousSong();
        } else if (event.getCode() == KeyCode.T) {
            SLvolume.setValue(SLvolume.getValue() - 5);
            changeVolumeMP();
        } else if (event.getCode() == KeyCode.Y) {
            SLvolume.setValue(SLvolume.getValue() + 5);
            changeVolumeMP();
        }

    }

    /**
     * Key released.
     *
     * @param event the event
     * @throws TurnSignalException the turn signal exception
     */
    @FXML
    void keyReleased(KeyEvent event) throws TurnSignalException {
        if (event.getCode() == KeyCode.UP) {
            dashboard.setKeyUp(false);
        } else if (event.getCode() == KeyCode.DOWN) {
            dashboard.setKeyDown(false);
        } else if (event.getCode() == KeyCode.LEFT && !Mindicators.isDisable()) {
            dashboard.setLeftTurnSignal(false);
            indicatorsTurnLeft.setSelected(false);
            lightSwitch("indicatorsTurnLeft", false);
            //indicatorSwitch(IVindicatorsTurnLeft,lights.get("indicatorsTurnLeft")[0],false);
        } else if (event.getCode() == KeyCode.RIGHT && !Mindicators.isDisable()) {
            dashboard.setRightTurnSignal(false);
            indicatorsTurnRight.setSelected(false);
            lightSwitch("indicatorsTurnRight", false);
            //indicatorSwitch(IVindicatorsTurnRight, lights.get("indicatorsTurnRight")[0],false);
        } else if (event.getCode() == KeyCode.H) {
            dashboard.playCarSound(Dashboard.CarSound.HONK, false);
        }
    }

    @FXML
    private void exportToDB() {
        try {
            dashboard.writeToDB();
            openDialog("Export finished",
                    "Succesfully exported to database!");
        } catch (Exception e) {
            //e.printStackTrace();
            openDialog(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @FXML
    private void switchAllLights(boolean state) {
        int isOn = (state) ? 1 : 0;
        lightSwitch(IVindicatorsTurnLeft, lights.get("indicatorsTurnLeft")[isOn], state);
        if (indicatorsTurnLeft.isSelected()) lightSwitch("indicatorsTurnLeft", false);
        lightSwitch(IVindicatorsTurnRight, lights.get("indicatorsTurnRight")[isOn], state);
        if (indicatorsTurnRight.isSelected()) lightSwitch("indicatorsTurnRight", false);
        lightSwitch(IVparkingLights, lights.get("parkingLights")[isOn], state);
        parkingLights.setSelected(state);
        lightSwitch(IVheadlightsLowBeam, lights.get("headlightsLowBeam")[isOn], state);
        headlightsLowBeam.setSelected(state);
        lightSwitch(IVheadlightsHighBeam, lights.get("headlightsHighBeam")[isOn], state);
        headlightsHighBeam.setSelected(state);
        lightSwitch(IVfogLightsBack, lights.get("fogLightsBack")[isOn], state);
        fogLightsBack.setSelected(state);
        lightSwitch(IVfogLightsFront, lights.get("fogLightsFront")[isOn], state);
        fogLightsFront.setSelected(state);
        lightSwitch(IVcruiseControl, lights.get("cruiseControl")[isOn], state);
        cruiseControl.setSelected(state);
    }

    @FXML
    private void animateEngineStart(boolean forward) {
        final double revs = dashboard.getSettings().getMaxRevs() / ((forward) ? 100.0 : -100.0);
        final double speed = dashboard.getSettings().getMaxSpeed() / ((forward) ? 100.0 : -100.0);
        Timeline animEngineStartGrow = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            speedGauge.setValue((speedGauge.getValue() + speed > 0) ? speedGauge.getValue() + speed : 0);
            revsGauge.setValue((revsGauge.getValue() + revs > 0) ? revsGauge.getValue() + revs : 0);
        }), new KeyFrame(Duration.millis(8)));
        animEngineStartGrow.setCycleCount(100);
        animEngineStartGrow.setDelay(Duration.millis((forward) ? 500 : 200));
        animEngineStartGrow.play();
        animEngineStartGrow.setOnFinished(f -> {
            if (forward)
                animateEngineStart(false);
        });
        try {
            Thread.sleep((forward) ? 500 : 200);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        switchAllLights(forward);
        if (!forward)
            startupLightSwitch();
        if (dashboard.getSettings().isAutoLowBeam() && !forward) {
            dashboard.setLowBeam(true);
            headlightsLowBeam.setSelected(true);
            lightSwitch(IVheadlightsLowBeam, lights.get("headlightsLowBeam")[1], true);
        }
    }

    @FXML
    public void startStopEngine() {
        if (!MIstartEngine.isDisable()) {
            switchEngine(true, false);
            LtitleMP.setOpacity(1);
            LartistMP.setOpacity(1);
        } else {
            dashboard.setCruiseControl(false);
            switchEngine(false, false);
            LtitleMP.setOpacity(0);
            LartistMP.setOpacity(0);
            musicPlayButtonSwitch(true);
            progressBarMP(true, true);
            dashboard.getMusicPlayer().stopSong();
        }
    }

    public void switchEngine(boolean enable, boolean interrupted) {
        if (enable) {
            if (speedThread != null)
                speedThread.setAnimationToZero(false);
            MIstopEngine.setDisable(false);
            MIstartEngine.setDisable(true);
            McruiseControl.setDisable(false);
            Mindicators.setDisable(false);
            Mimport.setDisable(true);
            dashboard.getOnBoardComputer().startJourneyTime();
            dashboard.playCarSound(Dashboard.CarSound.START_ENGINE, true);
            if (dashboard.getSpeed() == 0)
                animateEngineStart(true);
            else
                startupLightSwitch();
            try {
                if (dashboard.getSpeed() != 0) {
                    speedThread.interrupt();
                    speedThread = new SpeedThread(this, 0);
                } else
                    speedThread = new SpeedThread(this, 1800);
                speedThread.setEngineRunning(true);
                //speedThread.setDaemon(true); //Wątek uruchamiamy w trybie Deamon by zakończył się razem z aplikacją i jej glownym wątkiem
                speedThread.start();
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            }

        } else {
            if (interrupted)
                Platform.runLater(() -> {
                    openDialog("EngineException", "The engine went out!");
                });
            MIstartEngine.setDisable(false);
            Mindicators.setDisable(true);
            MIstopEngine.setDisable(true);
            McruiseControl.setDisable(true);
            Mimport.setDisable(false);
            speedThread.setEngineRunning(false);
            dashboard.getOnBoardComputer().pauseJourneyTime();
            switchAllLights(false);
            // Tworzymy wątek dla przypadku gdy zgasło auto podczas jazdy, by prędkość nadal spadała
            // aż do zera lub ponownego właczenia silnika
            speedThread = new SpeedThread(this, 0);
            speedThread.setAnimationToZero(true);
            speedThread.setDaemon(true);
            speedThread.start();
        }
    }

    @FXML
    private void removeFocus() {
        GPmain.requestFocus();
    }

    public void refresh() {
        TXTgear.setText(String.valueOf(dashboard.getCurrentGear()));
        TXTavgSpeed.setText(String.valueOf(dashboard.getOnBoardComputer().getAvgSpeed()));
        TXTmaxSpeed.setText(String.valueOf(dashboard.getOnBoardComputer().getMaxSpeed()));
        TXTavgFuelUsage.setText(String.valueOf(dashboard.getOnBoardComputer().getAvgCombustion()));
        TXTmaxFuelUsage.setText(String.valueOf(dashboard.getOnBoardComputer().getMaxCombustion()));
        TXTmainCounter.setText(String.valueOf(Math.round(dashboard.getCounter())));
        TXTdayCounter1.setText(String.valueOf(Math.round(dashboard.getDayCounter1() * 10.0) / 10.0f));
        TXTdayCounter2.setText(String.valueOf(Math.round(dashboard.getDayCounter2() * 10.0) / 10.0f));
        TXTjourneyDistance.setText(String.valueOf(Math.round(dashboard.getOnBoardComputer().getJourneyDistance() * 10.0) / 10.0f));
        TXTjourneyTime.setText(dashboard.getOnBoardComputer().getJourneyStartTime());
        try {
            speedGauge.setValue(dashboard.getSpeed());
            revsGauge.setValue(dashboard.getRevs());
        } catch (NullPointerException ignore) {
        }
    }


    @FXML
    private void initClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            TXTclock.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    @FXML
    private void resetDailyCounter(MouseEvent event) {
        Circle circle = (Circle) event.getSource();
        if (circle.getId().equals("CresetDailyCounter1")) {
            dashboard.setDayCounter1(0.0f);
            TXTdayCounter1.setText("0.0");
        } else {
            dashboard.setDayCounter2(0.0f);
            TXTdayCounter2.setText("0.0");
        }
    }

    @FXML
    private void changeSpeedCruiseControl(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String id = menuItem.getId();
        if (id.equals("MIspeedUp"))
            dashboard.cruiseControlSpeedChange(true);
        else
            dashboard.cruiseControlSpeedChange(false);
    }

    public void switchOffCruiseControl() {
        dashboard.setCruiseControl(false);
        this.lightSwitch("cruiseControl", false);
    }

    /**
     * On stage destruction.
     */
    public void onStageDestruction() {
        if (speedThread != null) {
            speedThread.setEngineRunning(false);
        }
        try {
            Serialization.write(dashboard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
