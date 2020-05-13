package org.Presentation;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import eu.hansolo.medusa.Gauge;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import org.Data.Database;
import org.Data.LoadFilesFromDisk;
import org.Data.RecordModel;
import org.Logic.*;

public class DashboardController extends UIController {
    private FlashingSignalThread flashingSignalThread;
    private SpeedThread speedThread;
    private Timeline progressBar;
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
        try {
            lights = loadFilesFromDisk.loadLights();
        } catch (IOException e) {
            //e.getStackTrace();
            openDialog(AlertType.ERROR, "Error dialog", e.getClass().getSimpleName(), e.getMessage());
        }
        this.dashboard.getMusicPlayer().setAutoPlayNext(this::nextSongMP);
        initClock();
        reloadAfterSettings();
        refresh();
        progressBar = null;
        setTitleArtist();
    }

    @FXML
    private void playPauseMP() {
        if(dashboard.getMusicPlayer().isEmpty())
            return;

        if(!dashboard.getMusicPlayer().isPlaying()) {
            PolyPlay.setVisible(false);
            RecPause1.setVisible(true);
            RecPause2.setVisible(true);
            dashboard.getMusicPlayer().playSong();
            progressBarMP(false, false);
            //dashboard.getMusicPlayer().autoPlayNext(this::nextSongMP);
        } else {
            PolyPlay.setVisible(true);
            RecPause1.setVisible(false);
            RecPause2.setVisible(false);
            dashboard.getMusicPlayer().pauseSong();
            progressBarMP(false, true);
        }
        setTitleArtist();
    }

    @FXML
    private void progressBarMP(boolean resetProgress, boolean pause) {
        if(resetProgress && progressBar!=null) {
            progressBar.stop();
            progressBar = null;
            PBsongDuration.setProgress(0.0);
        }
        if(dashboard.getMusicPlayer().isEmpty())
            return;
        if(!pause) {
            double progressValue = 1/dashboard.getMusicPlayer().getTotalDuration().toSeconds();
            if(progressBar == null && !Double.isNaN(progressValue)) {
                progressBar = new Timeline(new KeyFrame(Duration.ZERO, e -> {
                    PBsongDuration.setProgress(PBsongDuration.getProgress() + progressValue);
                }), new KeyFrame(Duration.seconds(1)));
                progressBar.setCycleCount((int) dashboard.getMusicPlayer().getTotalDuration().toSeconds() + 1);
            }
            if(progressBar != null)
                progressBar.play();
        }
        else if(progressBar != null)
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
        dashboard.getMusicPlayer().changeVolume(SLvolume.getValue()/100);
    }

    @FXML
    private void setTitleArtist() {
        LtitleMP.setText(dashboard.getMusicPlayer().getTitle());
        LartistMP.setText(dashboard.getMusicPlayer().getArtist());
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
            case "MIdatabaseImport":
                    filename = "databaseImport.fxml";
                    title = "Database Import";
                break;
            default:
                filename ="";
                title ="";
        }
        DatabaseImportController dbController = null;
        FXMLLoader root = new FXMLLoader(GUI.class.getResource(filename));
        Scene scene = new Scene(root.load());
        if(filename.equals("settings.fxml")) {
            SettingsController settingsController = root.getController();
            settingsController.lockSettings(MIstartEngine.isDisable());
            settingsController.loadSettings(this.dashboard.getSettings(), this);
        }
        else if(filename.equals("databaseImport.fxml")) {
            dbController = root.getController();
            dbController.loadDB(dashboard.readFromDB());
        }

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
        DatabaseImportController finalDbController = dbController;
        stage.setOnCloseRequest(e->{
            if(filename.equals("databaseImport.fxml")){
                dashboard.updateDashboard(finalDbController.getSelectedRecord());
                refresh();
            }
        });

    }

    @FXML
    private void openDialog(AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @FXML
    private void lightSwitch(ActionEvent actionEvent)  {
        CheckMenuItem checkMenuItem = (CheckMenuItem) actionEvent.getSource();
        String id = checkMenuItem.getId();
        boolean enable = checkMenuItem.isSelected();
        Image newImage = lights.get(id)[(enable) ? 1 : 0];

        switch (id) {
            case "indicatorsTurnLeft":
                try{
                    dashboard.setLeftTurnSignal(enable);
                } catch (TurnSignalException e) {
                    openDialog(AlertType.ERROR, "Error Dialog", e.getClass().getSimpleName(), e.getMessage());
                    checkMenuItem.setSelected(false);
                    break;
                }
                indicatorSwitch(IVindicatorsTurnLeft, newImage, enable);
                break;

            case "indicatorsTurnRight":
                try{
                    dashboard.setRightTurnSignal(enable);
                } catch (TurnSignalException e) {
                    openDialog(AlertType.ERROR, "Error Dialog", e.getClass().getSimpleName(), e.getMessage());
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

    @FXML
    private void lightSwitch(ImageView imageView, Image newImage, boolean enable) {
        imageView.setImage(newImage);
        imageView.setOpacity(enable ? 0.88 : 0.2);
    }

    @FXML
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
        };
    }

    public void reloadAfterSettings() {
        if(dashboard.getMusicPlayer() != null) {
            //boolean wasPlaying = dashboard.getMusicPlayer().isPlaying();
            dashboard.getMusicPlayer().dispose();
            try {
                dashboard.getMusicPlayer().loadSongs(dashboard.getSettings().getPlaylistDirectoryPath());
            } catch (IOException e) {
                openDialog(AlertType.ERROR, "Error dialog", e.getClass().getSimpleName(), e.getMessage());
            }

            //progressBarMP(true, !wasPlaying);
            progressBarMP(true, true);
            if(dashboard.getSettings().isShuffleMode())
                dashboard.getMusicPlayer().shufflePlaylist();


//            if(wasPlaying && !dashboard.getMusicPlayer().isEmpty()) {
//                playPauseMP();
//            } else {
//                PolyPlay.setVisible(true);
//                RecPause1.setVisible(false);
//                RecPause2.setVisible(false);
//            }

            PolyPlay.setVisible(true);
            RecPause1.setVisible(false);
            RecPause2.setVisible(false);

            setTitleArtist();
        }
        speedGauge.setMaxValue(dashboard.getSettings().getMaxSpeed());
        revsGauge.setMaxValue(dashboard.getSettings().getMaxRevs());
        int color = dashboard.getSettings().getDashboardLightIntesity();
        GPmain.setStyle("-fx-background-color: rgb("+color+", "+color+", "+color+");");
        dashboard.setGears();
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.UP && !dashboard.isKeyUp()) {
            dashboard.setKeyUp(true);
        } else if (event.getCode() == KeyCode.DOWN) {
            dashboard.setKeyDown(true);
        } else if ((event.getCode() == KeyCode.LEFT) && !this.dashboard.isLeftTurnSignal()) {
            try {
                dashboard.setLeftTurnSignal(true);
                indicatorsTurnLeft.setSelected(true);
                indicatorSwitch(IVindicatorsTurnLeft, lights.get("indicatorsTurnLeft")[1], true);
            } catch (TurnSignalException e) {
                openDialog(AlertType.ERROR, "Error Dialog", e.getClass().getSimpleName(), e.getMessage());
            }
        } else if ((event.getCode() == KeyCode.RIGHT) && (!this.dashboard.isRightTurnSignal())) {
            try {
                dashboard.setRightTurnSignal(true);
                indicatorsTurnRight.setSelected(true);
                indicatorSwitch(IVindicatorsTurnRight, lights.get("indicatorsTurnRight")[1],true);
            } catch (TurnSignalException e) {
                openDialog(AlertType.ERROR, "Error Dialog", e.getClass().getSimpleName(), e.getMessage());
            }
        } else if(event.getText().compareTo("0") >= 0 && 0 >= event.getText().compareTo("6") && MIstartEngine.isDisable()) {
            try {
                dashboard.setCurrentGear(Short.parseShort(event.getText()));
            } catch (GearException e) {
                openDialog(AlertType.ERROR, "Error dialog", e.getClass().getSimpleName(), e.getMessage());
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
            indicatorSwitch(IVindicatorsTurnLeft,lights.get("indicatorsTurnLeft")[0],false);
        } else if (event.getCode() == KeyCode.RIGHT) {
            dashboard.setRightTurnSignal(false);
            indicatorsTurnRight.setSelected(false);
            indicatorSwitch(IVindicatorsTurnRight, lights.get("indicatorsTurnRight")[0],false);
        }
    }

    @FXML
    private void onImportClicked(ActionEvent actionEvent){
        MenuItem menuItem = (MenuItem) actionEvent.getSource();
        String id = menuItem.getId();
        if(id.equals("MIjdbcDatabase")) {
            try {
                dashboard.writeToDB();
            } catch (Exception e) {
                e.printStackTrace();
                //openDialog(AlertType.ERROR, "Error Dialog", e.getClass().getSimpleName(), e.getMessage());
            }
        }
    }

    @FXML
    private void switchAllLights(boolean state) {
        int isOn = (state) ? 1 : 0;
        lightSwitch(IVindicatorsTurnLeft, lights.get("indicatorsTurnLeft")[isOn], state);
        if(indicatorsTurnLeft.isSelected()) {
            indicatorsTurnLeft.setSelected(false);
            try {
                dashboard.setLeftTurnSignal(false);
            } catch (TurnSignalException e) {
                openDialog(AlertType.ERROR, "Error Dialog", e.getClass().getSimpleName(), e.getMessage());
            }
            indicatorSwitch(IVindicatorsTurnLeft, lights.get("indicatorsTurnLeft")[0], false);
        }
        lightSwitch(IVindicatorsTurnRight, lights.get("indicatorsTurnRight")[isOn], state);
        if(indicatorsTurnRight.isSelected()) {
            indicatorsTurnRight.setSelected(false);
            try {
                dashboard.setRightTurnSignal(false);
            } catch (TurnSignalException e) {
                openDialog(AlertType.ERROR, "Error Dialog", e.getClass().getSimpleName(), e.getMessage());
            }
            indicatorSwitch(IVindicatorsTurnRight, lights.get("indicatorsTurnRight")[0], false);
        }
        lightSwitch(IVparkingLights, lights.get("parkingLights")[isOn], state);
        parkingLights.setSelected(state);
        dashboard.setPositionLights(state);
        lightSwitch(IVheadlightsLowBeam, lights.get("headlightsLowBeam")[isOn], state);
        headlightsLowBeam.setSelected(state);
        dashboard.setLowBeam(state);
        lightSwitch(IVheadlightsHighBeam, lights.get("headlightsHighBeam")[isOn], state);
        headlightsHighBeam.setSelected(state);
        dashboard.setHighBeam(state);
        lightSwitch(IVfogLightsBack, lights.get("fogLightsBack")[isOn], state);
        fogLightsBack.setSelected(state);
        dashboard.setRearFogLights(state);
        lightSwitch(IVfogLightsFront, lights.get("fogLightsFront")[isOn], state);
        fogLightsFront.setSelected(state);
        dashboard.setFrontFogLights(state);
    }

    @FXML
    private void animateEngineStart(boolean forward) {
        final double revs = dashboard.getSettings().getMaxRevs() / ((forward) ? 100.0: -100.0);
        final double speed = dashboard.getSettings().getMaxSpeed() / ((forward) ? 100.0: -100.0);
        Timeline animEngineStartGrow = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            speedGauge.setValue(speedGauge.getValue() + speed);
            revsGauge.setValue(revsGauge.getValue() + revs);
        }), new KeyFrame(Duration.millis(8)));
        animEngineStartGrow.setCycleCount(100);
        animEngineStartGrow.setDelay(Duration.millis((forward) ? 500 : 200));
        animEngineStartGrow.play();
        animEngineStartGrow.setOnFinished(f ->{
            if(forward)
                animateEngineStart(false);
        });
        try {
            Thread.sleep((forward) ? 500 : 200);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        switchAllLights(forward);
        if(dashboard.getSettings().isAutoLowBeam() && !forward) {
            dashboard.setLowBeam(true);
            headlightsLowBeam.setSelected(true);
            lightSwitch(IVheadlightsLowBeam, lights.get("headlightsLowBeam")[1], true);
        }
    }

    @FXML
    public void startStopEngine() {
        if(!MIstartEngine.isDisable()) {
            MIstopEngine.setDisable(false);
            MIstartEngine.setDisable(true);
            dashboard.getOnBoardComputer().startJourneyTime();
            dashboard.playStartEngineSound();
            if(dashboard.getSpeed() == 0)
               animateEngineStart(true);
            speedThread = new SpeedThread(this, 1800);
            speedThread.setEngineRunning(true);
            //speedThread.setDaemon(true); //Wątek uruchamiamy w trybie Deamon by zakończył się razem z aplikacją i jej glownym wątkiem
            speedThread.start();
        }
        else {
            MIstartEngine.setDisable(false);
            MIstopEngine.setDisable(true);
            speedThread.setEngineRunning(false);
            dashboard.getOnBoardComputer().pauseJourneyTime();
            switchAllLights(false);
            try {
                speedThread.join();
                // Tworzymy wątek dla przypadku gdy zgasło auto podczas jazdy, by prędkość nadal spadała
                // aż do zera lub ponownego właczenia silnika
                speedThread = new SpeedThread(this,0);
                //speedThread.setDaemon(true);
                speedThread.start();
                //speedThread.join();

            } catch (InterruptedException e) {
                openDialog(AlertType.ERROR, "Error dialog", e.getClass().getSimpleName(), e.getMessage());
            }
        }
    }

    @FXML
    private void removeFocus(){
        GPmain.requestFocus();
    }

    public synchronized void refresh() {
        speedGauge.setValue(dashboard.getSpeed());
        revsGauge.setValue(dashboard.getRevs());
        TXTgear.setText(String.valueOf(dashboard.getCurrentGear()));
        TXTavgSpeed.setText(String.valueOf(dashboard.getOnBoardComputer().getAvgSpeed()));
        TXTmaxSpeed.setText(String.valueOf(dashboard.getOnBoardComputer().getMaxSpeed()));
        TXTavgFuelUsage.setText(String.valueOf(dashboard.getOnBoardComputer().getAvgCombustion()));
        TXTmaxFuelUsage.setText(String.valueOf(dashboard.getOnBoardComputer().getMaxCombustion()));
        TXTmainCounter.setText(String.valueOf(Math.round(dashboard.getCounter()*10.0)/10.0f));
        TXTdayCounter1.setText(String.valueOf(Math.round(dashboard.getDayCounter1()*10.0)/10.0f));
        TXTdayCounter2.setText(String.valueOf(Math.round(dashboard.getDayCounter2()*10.0)/10.0f));
        TXTjourneyDistance.setText(String.valueOf(Math.round(dashboard.getOnBoardComputer().getJourneyDistance()*10.0)/10.0f));
        TXTjourneyTime.setText(dashboard.getOnBoardComputer().getJourneyStartTime());
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
        if(circle.getId().equals("CresetDailyCounter1")) {
            dashboard.setDayCounter1(0.0f);
            TXTdayCounter1.setText("0.0");
        } else {
            dashboard.setDayCounter2(0.0f);
            TXTdayCounter2.setText("0.0");
        }
    }

    public void onStageDestruction() {
        if(speedThread != null) {
            speedThread.setEngineRunning(false);
            try {
                speedThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
