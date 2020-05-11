package org.Presentation;

import java.io.IOException;
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
import org.Data.LoadFilesFromDisk;
import org.Logic.*;

public class DashboardController extends UIController {
    private FlashingSignalThread flashingSignalThread;
    private SpeedThread speedThread;
    private Timeline progressBar;
    private HashMap<String, Image[]> lights;
    public ImageView IVindicatorsTurnRight;
    public ImageView IVindicatorsTurnLeft;
    public ImageView IVparkingLights;
    public ImageView IVheadlightsLowBeam;
    public ImageView IVheadlightsHighBeam;
    public ImageView IVfogLightsBack;
    public ImageView IVfogLightsFront;
    public Gauge speedGauge;
    public Gauge revsGauge;
    public GridPane GPmain;
    public MenuItem MIexit;
    public MenuItem MIstartEngine;
    public MenuItem MIstopEngine;
    public MenuItem MIsettings;
    public CheckMenuItem indicatorsTurnLeft;
    public CheckMenuItem indicatorsTurnRight;
    public CheckMenuItem headlightsLowBeam;
    public Text TXTclock;
    public Text TXTgear;
    public Text TXTavgSpeed;
    public Text TXTmaxSpeed;
    public Text TXTavgFuelUsage;
    public Text TXTmaxFuelUsage;
    public Text TXTmainCounter;
    public Text TXTdayCounter1;
    public Text TXTdayCounter2;
    public Text TXTjourneyDistance;
    public Text TXTjourneyTime;
    public Label LtitleMP;
    public Label LartistMP;
    public Slider SLvolume;
    public Polygon PolyPlay;
    public Rectangle RecPause1;
    public Rectangle RecPause2;
    public ProgressBar PBsongDuration;

    @FXML
    public void initialize() {
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
        //dashboard.getMusicPlayer().autoPlayNext(this::nextSongMP);
        progressBarMP(true, PolyPlay.isVisible());
        setTitleArtist();
    }

    @FXML
    public void previousSong() {
        dashboard.getMusicPlayer().previousSong();
        //dashboard.getMusicPlayer().autoPlayNext(this::nextSongMP);
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
            default:
                filename ="";
                title ="";
        }

        FXMLLoader root = new FXMLLoader(GUI.class.getResource(filename));
        Scene scene = new Scene(root.load());
        if(filename.equals("settings.fxml")) {
            SettingsController settingsController = root.getController();
            settingsController.lockSettings(MIstartEngine.isDisable());
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

    private void switchAllLights(boolean state) {
        int isOn = (state) ? 1 : 0;
        lightSwitch(IVindicatorsTurnLeft, lights.get("indicatorsTurnLeft")[isOn], state);
        lightSwitch(IVindicatorsTurnRight, lights.get("indicatorsTurnRight")[isOn], state);
        lightSwitch(IVparkingLights, lights.get("parkingLights")[isOn], state);
        lightSwitch(IVheadlightsLowBeam, lights.get("headlightsLowBeam")[isOn], state);
        lightSwitch(IVheadlightsHighBeam, lights.get("headlightsHighBeam")[isOn], state);
        lightSwitch(IVfogLightsBack, lights.get("fogLightsBack")[isOn], state);
        lightSwitch(IVfogLightsFront, lights.get("fogLightsFront")[isOn], state);
    }

    private void animateEngineStart(boolean forward) {
        final double revs = dashboard.getSettings().getMaxRevs() / ((forward) ? 100.0: -100.0);
        final double speed = dashboard.getSettings().getMaxSpeed() / ((forward) ? 100.0: -100.0);
        final double maxRevs = dashboard.getSettings().getMaxRevs();
        final double maxSpeed = dashboard.getSettings().getMaxSpeed();
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

    public void startStopEngine() {
        if(!MIstartEngine.isDisable()) {
            MIstopEngine.setDisable(false);
            MIstartEngine.setDisable(true);
            dashboard.getOnBoardComputer().startJourneyTime();
            dashboard.playStartEngineSound();
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
            if(dashboard.getSettings().isAutoLowBeam()) {
                dashboard.setLowBeam(false);
                headlightsLowBeam.setSelected(false);
                lightSwitch(IVheadlightsLowBeam, lights.get("headlightsLowBeam")[0], false);
            }
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
    public void removeFocus(){
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
        TXTmainCounter.setText(String.valueOf(dashboard.getCounter()));
        TXTdayCounter1.setText(String.valueOf(dashboard.getDayCounter1()));
        TXTdayCounter2.setText(String.valueOf(dashboard.getDayCounter2()));
        TXTjourneyDistance.setText(String.valueOf(dashboard.getOnBoardComputer().getJourneyDistance()));
        TXTjourneyTime.setText(dashboard.getOnBoardComputer().getJourneyTime());
    }

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
