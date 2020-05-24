/***
 * Logic layer.
 */
package org.Logic;

import javafx.collections.ObservableList;
import javafx.scene.media.AudioClip;
import org.Data.RecordModel;
import org.Data.SQL;
import org.Data.XML;
import org.Presentation.GUI;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * The type Dashboard.
 */
public class Dashboard implements Serializable {
    private transient OnBoardComputer onBoardComputer;
    private Settings settings;
    private transient MusicPlayer musicPlayer;
    private transient short speed;
    private transient boolean leftTurnSignal;
    private transient boolean rightTurnSignal;
    private boolean positionLights;
    private boolean highBeam;
    private boolean lowBeam;
    private boolean frontFogLights;
    private boolean rearFogLights;
    private transient short cruiseSpeed;
    private transient boolean cruiseControl;
    private transient boolean keyUp;
    private transient boolean keyDown;
    private float counter;
    private float dayCounter1;
    private float dayCounter2;
    private transient int revs;
    private short currentGear;
    private ArrayList<Short> gears;

    /**
     * Instantiates a new Dashboard.
     */
    public Dashboard() {
        this.onBoardComputer = new OnBoardComputer();
        this.settings = new Settings();
        this.musicPlayer = new MusicPlayer();
        this.speed = 0;
        this.leftTurnSignal = false;
        this.rightTurnSignal = false;
        this.positionLights = false;
        this.lowBeam = false;
        this.highBeam = false;
        this.frontFogLights = false;
        this.rearFogLights = false;
        this.cruiseControl = false;
        this.counter = 0;
        this.dayCounter1 = 0;
        this.dayCounter2 = 0;
        this.revs = 0;
        this.currentGear = 0;
        gears = new ArrayList<>();
        this.setGears();
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
    }

    /**
     * Is cruise control activated boolean.
     *
     * @return the boolean
     */
    public boolean isCruiseControl() {
        return cruiseControl;
    }

    /**
     * Sets cruise control.
     *
     * @param cruiseControl the cruise control
     */
    public void setCruiseControl(boolean cruiseControl) {
        this.cruiseControl = cruiseControl;
    }

    /**
     * Gets cruise speed in km/h.
     *
     * @return the cruise speed in km/h
     */
    public short getCruiseSpeed() {
        return cruiseSpeed;
    }

    /**
     * Sets cruise speed in km/h.
     *
     * @param cruiseSpeed the cruise speed
     * @throws CruiseControlException the cruise control exception
     */
    public void setCruiseSpeed(short cruiseSpeed) throws CruiseControlException {
        if (cruiseSpeed >= 80)
            this.cruiseSpeed = cruiseSpeed;
        else
            throw new CruiseControlException("Too low speed to enable Cruise Control!");
    }

    /**
     * Initialize dashboard.
     */
    public void init() {
        this.onBoardComputer = new OnBoardComputer();
        this.musicPlayer = new MusicPlayer();
        this.speed = 0;
        this.cruiseControl = false;
        this.revs = 0;
        this.cruiseSpeed = 0;
        this.keyUp = false;
        this.keyDown = false;
        this.leftTurnSignal = false;
        this.rightTurnSignal = false;
    }

    /**
     * Is keyboard key up boolean.
     *
     * @return the boolean
     */
    public boolean isKeyUp() {
        return keyUp;
    }

    /**
     * Sets keyboard key up.
     *
     * @param keyUp the key up
     */
    public void setKeyUp(boolean keyUp) {
        this.keyUp = keyUp;
    }

    /**
     * Read from database to observable list.
     *
     * @return the observable list
     */
    public ObservableList<RecordModel> readFromDB() {
        SQL loadFromDatabase = new SQL();
        ObservableList<RecordModel> set = null;
        try {
            set = loadFromDatabase.read(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return set;
    }

    private RecordModel getRecordModel() {
        return new RecordModel(0, onBoardComputer.getAvgSpeed(),
                onBoardComputer.getMaxSpeed(), onBoardComputer.getAvgCombustion(),
                onBoardComputer.getMaxCombustion(), onBoardComputer.getJourneyDistance(),
                onBoardComputer.getJourneyTime(), (int) counter, dayCounter1, dayCounter2, new Date(System.currentTimeMillis()));
    }

    /**
     * Write record to database.
     *
     * @throws Exception the exception
     */
    public void writeToDB() throws Exception {
        SQL db = new SQL();
        db.write(null, getRecordModel());
    }

    /**
     * Write record to xml file.
     *
     * @param path the path
     * @throws IOException        the io exception
     * @throws XMLStreamException the xml stream exception
     */
    public void writeToXml(String path) throws IOException, XMLStreamException {
        XML xml = new XML();
        xml.write(path, getRecordModel());
    }

    /**
     * Read record from xml file.
     *
     * @param path the path
     * @throws IOException        the io exception
     * @throws XMLStreamException the xml stream exception
     */
    public void readFromXml(String path) throws IOException, XMLStreamException {
        XML xml = new XML();
        updateDashboard(xml.read(path));
    }

    /**
     * Gets on board computer.
     *
     * @return the on board computer
     */
    public OnBoardComputer getOnBoardComputer() {
        return onBoardComputer;
    }

    /**
     * Is keyboard key down boolean.
     *
     * @return the boolean
     */
    public boolean isKeyDown() {
        return keyDown;
    }

    /**
     * Sets keyboard key down.
     *
     * @param keyDown the key down
     */
    public void setKeyDown(boolean keyDown) {
        this.keyDown = keyDown;
    }

    /**
     * Gets settings.
     *
     * @return the settings
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * Gets speed in km/h.
     *
     * @return the speed in km/h
     */
    public short getSpeed() {
        return speed;
    }

    /**
     * Sets speed in km/h.
     *
     * @param speed the speed in km
     * @throws NegativeValueException the negative value of speed exception
     */
    public void setSpeed(short speed) throws NegativeValueException {
        if (speed < 0)
            throw new NegativeValueException("The speed  can't be negative!");
        this.speed = speed;
    }

    /**
     * Is left turn signal boolean.
     *
     * @return the boolean
     */
    public boolean isLeftTurnSignal() {
        return leftTurnSignal;
    }

    /**
     * Sets left turn signal.
     *
     * @param leftTurnSignal the left turn signal
     * @throws TurnSignalException the turn signal exception
     */
    public void setLeftTurnSignal(boolean leftTurnSignal) throws TurnSignalException {
        if (isRightTurnSignal())
            throw new TurnSignalException("The right turn signal is already on, you can't turn on both at the same time!");

        this.leftTurnSignal = leftTurnSignal;
    }

    /**
     * Is right turn signal boolean.
     *
     * @return the boolean
     */
    public boolean isRightTurnSignal() {
        return rightTurnSignal;
    }

    /**
     * Sets right turn signal.
     *
     * @param rightTurnSignal the right turn signal
     * @throws TurnSignalException the turn signal exception
     */
    public void setRightTurnSignal(boolean rightTurnSignal) throws TurnSignalException {
        if (isLeftTurnSignal())
            throw new TurnSignalException("The left turn signal is already on, you can't turn on both at the same time!");

        this.rightTurnSignal = rightTurnSignal;
    }

    /**
     * Is position lights boolean.
     *
     * @return the boolean
     */
    public boolean isPositionLights() {
        return positionLights;
    }

    /**
     * Sets position lights.
     *
     * @param positionLights the position lights
     */
    public void setPositionLights(boolean positionLights) {
        this.positionLights = positionLights;
    }

    /**
     * Is low beam boolean.
     *
     * @return the boolean
     */
    public boolean isLowBeam() {
        return lowBeam;
    }

    /**
     * Sets low beam.
     *
     * @param lowBeam the low beam
     */
    public void setLowBeam(boolean lowBeam) {
        this.lowBeam = lowBeam;
    }

    /**
     * Is high beam boolean.
     *
     * @return the boolean
     */
    public boolean isHighBeam() {
        return highBeam;
    }

    /**
     * Sets high beam.
     *
     * @param highBeam the high beam
     */
    public void setHighBeam(boolean highBeam) {
        this.highBeam = highBeam;
    }

    /**
     * Is front fog lights boolean.
     *
     * @return the boolean
     */
    public boolean isFrontFogLights() {
        return frontFogLights;
    }

    /**
     * Sets front fog lights.
     *
     * @param frontFogLights the front fog lights
     */
    public void setFrontFogLights(boolean frontFogLights) {
        this.frontFogLights = frontFogLights;
    }

    /**
     * Is back fog lights boolean.
     *
     * @return the boolean
     */
    public boolean isBackFogLights() {
        return rearFogLights;
    }

    /**
     * Gets counter in km.
     *
     * @return the counter in km
     */
    public float getCounter() {
        return counter;
    }

    /**
     * Sets counter in km.
     *
     * @param counter the counter in km
     */
    public void setCounter(float counter) {
        this.counter = counter;
    }

    /**
     * Gets day counter 1 in km.
     *
     * @return the day counter 1 in km
     */
    public float getDayCounter1() {
        return dayCounter1;
    }

    /**
     * Sets day counter 1 in km.
     *
     * @param dayCounter1 the day counter 1 in km
     */
    public void setDayCounter1(float dayCounter1) {
        this.dayCounter1 = dayCounter1;
    }

    /**
     * Sets day counter 1 in km.
     *
     * @param dayCounter1 the day counter 1 in km
     * @throws NegativeValueException the negative value of counter exception
     */
    public void setDayCounter1(int dayCounter1) throws NegativeValueException {
        if (dayCounter1 < 0)
            throw new NegativeValueException("The day counter 1 can't be negative!");

        this.dayCounter1 = dayCounter1;
    }

    /**
     * Gets day counter 2 in km.
     *
     * @return the day counter 2 in km
     */
    public float getDayCounter2() {
        return dayCounter2;
    }

    /**
     * Sets day counter 2 in km.
     *
     * @param dayCounter2 the day counter 2 in km
     */
    public void setDayCounter2(float dayCounter2) {
        this.dayCounter2 = dayCounter2;
    }

    /**
     * Sets day counter 2 in km.
     *
     * @param dayCounter2 the day counter 2 in km
     * @throws NegativeValueException the negative value of counter exception
     */
    public void setDayCounter2(int dayCounter2) throws NegativeValueException {
        if (dayCounter2 < 0)
            throw new NegativeValueException("The day counter 2 can't be negative!");
        this.dayCounter2 = dayCounter2;
    }

    /**
     * Gets revs.
     *
     * @return the revs
     */
    public int getRevs() {
        return revs;
    }

    /**
     * Sets revs.
     *
     * @param revs the revs
     * @throws NegativeValueException the negative value exception
     */
    public void setRevs(int revs) throws NegativeValueException {
        if (revs < 0)
            throw new NegativeValueException("The revs can't be negative!");

        this.revs = revs;
    }

    /**
     * Gets current gear.
     *
     * @return the current gear
     */
    public short getCurrentGear() {
        return currentGear;
    }

    /**
     * Update dashboard.
     *
     * @param selectedRecord the selected record
     */
    public void updateDashboard(RecordModel selectedRecord) {
        this.onBoardComputer.setAvgSpeed(selectedRecord.getAvgSpeed());
        this.onBoardComputer.setMaxSpeed(selectedRecord.getMaxSpeed());
        this.onBoardComputer.setJourneyDistance(selectedRecord.getJourneyDistance());
        this.onBoardComputer.setMaxCombustion(selectedRecord.getMaxFuel());
        this.onBoardComputer.setAvgCombustion(selectedRecord.getAvgFuel());
        this.dayCounter1 = selectedRecord.getDayCounter1();
        this.dayCounter2 = selectedRecord.getDayCounter2();
        this.onBoardComputer.setJourneyTime(selectedRecord.getJourneyTime());
    }

    /**
     * Sets rear fog lights.
     *
     * @param rearFogLights the rear fog lights
     */
    public void setRearFogLights(boolean rearFogLights) {
        this.rearFogLights = rearFogLights;
    }

    /**
     * Sets current gear.
     *
     * @param currentGear   the current gear
     * @param engineRunning the engine running
     * @throws GearException the gear exception
     */
    public void setCurrentGear(short currentGear, boolean engineRunning) throws GearException {
        if (currentGear < gears.size()) {
            if (currentGear == 0)
                this.currentGear = currentGear;
            else if (this.speed > gears.get(currentGear)) {
                this.keyUp = false;
                this.keyDown = false;
                throw new GearException("You cannot change the gear to " + currentGear + " at this speed!");
            } else if (currentGear == 1)
                this.currentGear = currentGear;
            else if (revs >= 1999 || engineRunning)
                this.currentGear = currentGear;
            else {
                this.keyUp = false;
                this.keyDown = false;
                throw new GearException("You cannot change the gear to " + currentGear + " at this speed!");
            }
        }
    }

    /**
     * Cruise control speed change in km/h.
     *
     * @param speedChange the speed change in km/h
     */
    public void cruiseControlSpeedChange(boolean speedChange) {
        if (speedChange) {
            this.cruiseSpeed += 5;
            if (this.cruiseSpeed > this.getCurrentGearMaxSpeed())
                this.cruiseSpeed = this.getCurrentGearMaxSpeed();
        } else {
            this.cruiseSpeed -= 5;
            if (this.cruiseSpeed < 80)
                this.cruiseSpeed = 80;

        }
    }

    /**
     * Add speed.
     *
     * @param value the value in km/h
     */
    public void addSpeed(int value) {
        speed += value;
        if (speed > settings.getMaxSpeed())
            speed = settings.getMaxSpeed();
    }

    /**
     * Sub speed.
     *
     * @param value the value in km/h
     */
    public void subSpeed(int value) {
        speed -= value;
        if (speed < 0)
            speed = 0;
    }

    /**
     * Set gears.
     */
    public void setGears() {
        this.gears.clear();
        this.gears.add((short) 0);
        this.gears.add((short) (this.settings.getMaxSpeed() * 0.2));
        this.gears.add((short) (this.settings.getMaxSpeed() * 0.3));
        this.gears.add((short) (this.settings.getMaxSpeed() * 0.4));
        if (this.settings.getNumberOfGears() == 5) {
            this.gears.add((short) (this.settings.getMaxSpeed() * 0.6));
        } else {
            this.gears.add((short) (this.settings.getMaxSpeed() * 0.5));
            this.gears.add((short) (this.settings.getMaxSpeed() * 0.7));
        }
        this.gears.add(this.settings.getMaxSpeed());
    }

    /**
     * Gets current gear max speed in km/h.
     *
     * @return the current gear max speed in km/h
     */
    public short getCurrentGearMaxSpeed() {
        return gears.get(currentGear);
    }

    /**
     * Gets lower gear max speed in km/h.
     *
     * @return the lower gear max speed in km/h
     */
    public short getLowerGearMaxSpeed() {
        if (currentGear != 0)
            return gears.get(currentGear - 1);
        return 0;
    }

    /**
     * Play start engine sound.
     */
    public void playStartEngineSound() {
        AudioClip audioClip = new AudioClip(new File("sounds/engineStartSound.mp3").toURI().toString());

        audioClip.play();
    }

    /**
     * Gets music player.
     *
     * @return the music player
     */
    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }
}
