package org.Logic;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Dashboard implements Serializable {
    OnBoardComputer onBoardComputer;
    Settings settings;
    short speed;
    boolean leftTurnSignal;
    boolean rightTurnSignal;
    boolean positionLights;
    boolean lowBeam;
    boolean highBeam;
    boolean frontFogLights;
    boolean rearFogLights;
    boolean KeyUp;
    boolean KeyDown;
    float counter;
    float dayCounter1;
    float dayCounter2;
    int revs;
    short currentGear;
    ArrayList<Short> gears;
    DecimalFormat decimalFormat;


    public Dashboard() {
        this.onBoardComputer = new OnBoardComputer();
        this.settings = new Settings();
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
        this.decimalFormat  = new DecimalFormat("#.#");
        this.decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
    }

    public boolean isKeyUp() {
        return KeyUp;
    }

    public OnBoardComputer getOnBoardComputer() {
        return onBoardComputer;
    }

    public void setCounter(float counter) {
        this.counter = counter;
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
        return Math.round(counter * 10.0) / 10.0f;
    }

    public float getDayCounter1() {
        return Math.round(dayCounter1 * 10.0) / 10.0f;
    }

    public float getDayCounter2() {
        return Math.round(dayCounter2 * 10.0) / 10.0f;
    }

    public int getRevs() {
        return revs;
    }

    public short getCurrentGear() {
        return currentGear;
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

    public void setCounter(int counter) throws NegativeValueException {
        if(counter < 0)
            throw new NegativeValueException("The counter  can't be negative!");

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
            if(currentGear == 0)
                this.currentGear = currentGear;
            else if(speed >= (float)gears.get(currentGear-1)*(3f/5f))
                this.currentGear = currentGear;
            else
                throw new GearException("You cannot change the gear to "+currentGear+" at this speed!");
        }
    }

    public void setGears(){
        this.gears.clear();
        this.gears.add((short)0);
        this.gears.add((short) (this.settings.maxSpeed*0.1));
        this.gears.add((short)(this.settings.maxSpeed*0.15));
        this.gears.add((short)(this.settings.maxSpeed*0.31));
        if (this.settings.numberOfGears == 5) {
            this.gears.add((short)(this.settings.maxSpeed*0.5));
        }
        else {
            this.gears.add((short)(this.settings.maxSpeed*0.4));
            this.gears.add((short)(this.settings.maxSpeed*0.6));
        }
        this.gears.add(this.settings.maxSpeed);
    }

    public short getCurrentGearMaxSpeed() {
        return gears.get(currentGear);
    }

    public short getLowerGearMaxSpeed() {
        if(currentGear != 0)
            return gears.get(currentGear-1);
        return 0;
    }
}


