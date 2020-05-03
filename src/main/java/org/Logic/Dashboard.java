package org.Logic;

import java.io.Serializable;

public class Dashboard implements Serializable {

    OnBoardComputer onBoardComputer;
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
    int DayCounter2;
    int revs;
    byte actualGear;

    public Dashboard() {}

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
        return DayCounter2;
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
        DayCounter2 = dayCounter2;
    }

    public void setRevs(int revs) throws NegativeValueException {
        if(revs < 0)
            throw new NegativeValueException("The revs can't be negative!");

        this.revs = revs;
    }

    public void setActualGear(byte actualGear) {
        this.actualGear = actualGear;
    }
}



