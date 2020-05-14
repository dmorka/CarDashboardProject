package org.Logic;

import javafx.collections.ObservableList;
import javafx.scene.media.AudioClip;
import org.Data.Database;
import org.Data.RecordModel;
import org.Data.XML;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.Serializable;
import java.math.RoundingMode;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;

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
    private transient boolean KeyUp;
    private transient boolean KeyDown;
    private float counter;
    private float dayCounter1;
    private float dayCounter2;
    private transient int revs;
    private short currentGear;
    private ArrayList<Short> gears;


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

    public void init() {
        this.onBoardComputer = new OnBoardComputer();
        this.musicPlayer = new MusicPlayer();
        this.speed = 0;
        this.revs = 0;
        this.KeyUp = false;
        this.KeyDown = false;
        this.leftTurnSignal = false;
        this.rightTurnSignal = false;
    }

    public boolean isKeyUp() {
        return KeyUp;
    }

    public ObservableList<RecordModel> readFromDB(){
        Database loadFromDatabase = new Database();
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
                onBoardComputer.getJourneyTime(), (int)counter, dayCounter1, dayCounter2, new Date(System.currentTimeMillis()));
    }

    public void writeToDB() throws Exception {
        Database db = new Database();
        db.write(null, getRecordModel());
    }

    public void writeToXml(String path) throws IOException, XMLStreamException {
        XML xml =  new XML();
        xml.write(path, getRecordModel());
    }

    public void readFromXml(String path) throws IOException, XMLStreamException {
        XML xml =  new XML();
        updateDashboard(xml.read(path));
    }

    public OnBoardComputer getOnBoardComputer() {
        return onBoardComputer;
    }

    public void setDayCounter1(float dayCounter1) {
        this.dayCounter1 = dayCounter1;
    }

    public void setDayCounter2(float dayCounter2) {
        this.dayCounter2 = dayCounter2;
    }

    public void setKeyUp(boolean keyUp) {
        KeyUp = keyUp;
    }

    public boolean isKeyDown() {
        return KeyDown;
    }

    public void setKeyDown(boolean keyDown) {
        KeyDown = keyDown;
    }

    public Settings getSettings() {
        return settings;
    }

    public short getSpeed() {
        return speed;
    }

    public boolean isLeftTurnSignal() {
        return leftTurnSignal;
    }

    public boolean isRightTurnSignal() {
        return rightTurnSignal;
    }

    public boolean isPositionLights() {
        return positionLights;
    }

    public boolean isLowBeam() {
        return lowBeam;
    }

    public boolean isHighBeam() {
        return highBeam;
    }

    public boolean isFrontFogLights() {
        return frontFogLights;
    }

    public boolean isRearFogLights() {
        return rearFogLights;
    }

    public float getCounter() {
        return counter;
    }

    public float getDayCounter1() {
        return dayCounter1;
    }

    public float getDayCounter2() {
        return dayCounter2;
    }

    public int getRevs() {
        return revs;
    }

    public short getCurrentGear() {
        return currentGear;
    }

    public void updateDashboard(RecordModel selectedRecord){
        this.onBoardComputer.setAvgSpeed(selectedRecord.getAvgSpeed());
        this.onBoardComputer.setMaxSpeed(selectedRecord.getMaxSpeed());
        this.onBoardComputer.setJourneyDistance(selectedRecord.getJourneyDistance());
        this.onBoardComputer.setMaxCombustion(selectedRecord.getMaxFuel());
        this.onBoardComputer.setAvgCombustion(selectedRecord.getAvgFuel());
        this.dayCounter1 = selectedRecord.getDayCounter1();
        this.dayCounter2 = selectedRecord.getDayCounter2();
        this.onBoardComputer.setJourneyTime(selectedRecord.getJourneyTime());
    }

    public void setSpeed(short speed) throws NegativeValueException {
        if(speed < 0)
            throw new NegativeValueException("The speed  can't be negative!");

        this.speed = speed;
    }

    public void setLeftTurnSignal(boolean leftTurnSignal) throws TurnSignalException {
        if(isRightTurnSignal())
            throw new TurnSignalException("The right turn signal is already on, you can't turn on both at the same time!");

        this.leftTurnSignal = leftTurnSignal;
    }

    public void setRightTurnSignal(boolean rightTurnSignal) throws TurnSignalException {
        if(isLeftTurnSignal())
            throw new TurnSignalException("The left turn signal is already on, you can't turn on both at the same time!");

        this.rightTurnSignal = rightTurnSignal;
    }

    public void setPositionLights(boolean positionLights) {
        this.positionLights = positionLights;
    }

    public void setLowBeam(boolean lowBeam) {
        this.lowBeam = lowBeam;
    }

    public void setHighBeam(boolean highBeam) {
        this.highBeam = highBeam;
    }

    public void setFrontFogLights(boolean frontFogLights) {
        this.frontFogLights = frontFogLights;
    }

    public void setRearFogLights(boolean rearFogLights) {
        this.rearFogLights = rearFogLights;
    }

    public void setCounter(float counter) {
        this.counter = counter;
    }

    public void setDayCounter1(int dayCounter1) throws NegativeValueException{
        if(dayCounter1 < 0)
            throw new NegativeValueException("The day counter 1 can't be negative!");

        this.dayCounter1 = dayCounter1;
    }

    public void setDayCounter2(int dayCounter2) throws NegativeValueException {
        if(dayCounter2 < 0)
            throw new NegativeValueException("The day counter 2 can't be negative!");
        this.dayCounter2 = dayCounter2;
    }

    public void setRevs(int revs) throws NegativeValueException {
        if(revs < 0)
            throw new NegativeValueException("The revs can't be negative!");

        this.revs = revs;
    }

    public void setCurrentGear(short currentGear) throws GearException {
        if(currentGear < gears.size()){
            if(currentGear <= 1)
                this.currentGear = currentGear;
            else if(revs >= 1999)
                this.currentGear = currentGear;
            else
                throw new GearException("You cannot change the gear to "+currentGear+" at this speed!");
        }
    }

    public void addSpeed(int value) {
        speed += value;
        if(speed > settings.getMaxSpeed())
            speed = settings.getMaxSpeed();
    }

    public void subSpeed(int value) {
        speed -= value;
        if(speed < 0)
            speed = 0;
    }

    public void setGears(){
        this.gears.clear();
        this.gears.add((short)0);
        this.gears.add((short) (this.settings.getMaxSpeed()*0.2));
        this.gears.add((short)(this.settings.getMaxSpeed()*0.3));
        this.gears.add((short)(this.settings.getMaxSpeed()*0.4));
        if (this.settings.getNumberOfGears() == 5) {
            this.gears.add((short)(this.settings.getMaxSpeed()*0.6));
        }
        else {
            this.gears.add((short)(this.settings.getMaxSpeed()*0.5));
            this.gears.add((short)(this.settings.getMaxSpeed()*0.7));
        }
        this.gears.add(this.settings.getMaxSpeed());
    }

    public short getCurrentGearMaxSpeed() {
        return gears.get(currentGear);
    }

    public short getLowerGearMaxSpeed() {
        if(currentGear != 0)
            return gears.get(currentGear-1);
        return 0;
    }

    public void playStartEngineSound() {
        AudioClip audioClip = new AudioClip(Paths.get("src/main/resources/org/Presentation/sounds/engineStartSound.mp3").toUri().toString());
        audioClip.play();
    }

    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }
}
