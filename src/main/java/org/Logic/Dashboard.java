package org.Logic;

import java.io.Serializable;
import java.util.ArrayList;

public class Dashboard implements Serializable {

    OnBoardComputer onBoardComputer;
    Settings settings;
    short speed;
    boolean leftTurnSignal;
    boolean rightTurnSignal;
    boolean positionLights;
    boolean lowBeam;
    boolean trafficLights;
    boolean frontFogLights;
    boolean rearFogLights;
    int counter;
    int dayCounter1;
    int dayCounter2;
    int revs;
    byte actualGear;
    ArrayList<Short> gears;

    public Dashboard() {
        this.onBoardComputer = new OnBoardComputer();
        this.settings = new Settings();
        this.speed = 0;
        this.leftTurnSignal = false;
        this.rightTurnSignal = false;
        this.positionLights = false;
        this.lowBeam = false;
        this.trafficLights = false;
        this.frontFogLights = false;
        this.rearFogLights = false;
        this.counter = 0;
        this.dayCounter1 = 0;
        this.dayCounter2 = 0;
        this.revs = 0;
        this.actualGear = 0;
        this.setGears();
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

    public boolean isTrafficLights() {
        return trafficLights;
    }

    public boolean isFrontFogLights() {
        return frontFogLights;
    }

    public boolean isRearFogLights() {
        return rearFogLights;
    }

    public int getCounter() {
        return counter;
    }

    public int getDayCounter1() {
        return dayCounter1;
    }

    public int getDayCounter2() {
        return dayCounter2;
    }

    public int getRevs() {
        return revs;
    }

    public byte getActualGear() {
        return actualGear;
    }

    public void setSpeed(short speed) throws NegativeValueException {
        if(speed < 0)
            throw new NegativeValueException("The speed  can't be negative!");

        this.speed = speed;
    }

    public void setLeftTurnSignal(boolean leftTurnSignal) throws TurnSignalException {
        if(isRightTurnSignal())
            throw new TurnSignalException("The right turn signal is on, you can't turn on both at the same time!");

        this.leftTurnSignal = leftTurnSignal;
    }

    public void setRightTurnSignal(boolean rightTurnSignal) throws TurnSignalException {
        if(isLeftTurnSignal())
            throw new TurnSignalException("The left turn signal is on, you can't turn on both at the same time!");

        this.rightTurnSignal = rightTurnSignal;
    }

    public void setPositionLights(boolean positionLights) {
        this.positionLights = positionLights;
    }

    public void setLowBeam(boolean lowBeam) {
        this.lowBeam = lowBeam;
    }

    public void setTrafficLights(boolean trafficLights) {
        this.trafficLights = trafficLights;
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

    public void setActualGear(byte actualGear) {
        this.actualGear = actualGear;
    }

    public void setGears(){
        this.gears.clear();
        this.gears.add((short)0);
        this.gears.add((short) (this.settings.maxSpeed*0.1));
        this.gears.add((short)(this.settings.maxSpeed*0.15));
        this.gears.add((short)(this.settings.maxSpeed*0.31));
        if (this.settings.numberOfGears == 5) {
            this.gears.add((short)(this.settings.maxSpeed*0.5));
            this.gears.add(this.settings.maxSpeed);
        }
        else {
            this.gears.add((short)(this.settings.maxSpeed*0.4));
            this.gears.add((short)(this.settings.maxSpeed*0.6));
            this.gears.add(this.settings.maxSpeed);
        }

    }
}


