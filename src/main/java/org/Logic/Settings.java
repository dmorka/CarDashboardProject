package org.Logic;

import java.io.Serializable;

public class Settings implements Serializable {
    public short maxSpeed;
    public short maxRevs;
    public short dashboardLightIntesity;
    public boolean autoLowBeam;
    public char engineType;
    public byte numberOfGears;
    public boolean shuffleMode;

    public Settings() {
        maxSpeed = 220;
        maxRevs = 8000;
        dashboardLightIntesity = 0;
        autoLowBeam = false;
        engineType = 'P';
        numberOfGears = 6;
        shuffleMode = false;
    }

    public short getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(short maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public short getMaxRevs() {
        return maxRevs;
    }

    public void setMaxRevs(short maxRevs) {
        this.maxRevs = maxRevs;
    }

    public short getDashboardLightIntesity() {
        return dashboardLightIntesity;
    }

    public void setDashboardLightIntesity(short dashboardLightIntesity) {
        this.dashboardLightIntesity = dashboardLightIntesity;
    }

    public boolean isAutoLowBeam() {
        return autoLowBeam;
    }

    public void setAutoLowBeam(boolean autoLowBeam) {
        this.autoLowBeam = autoLowBeam;
    }

    public char getEngineType() {
        return engineType;
    }

    public void setEngineType(char engineType) {
        this.engineType = engineType;
    }

    public byte getNumberOfGears() {
        return numberOfGears;
    }

    public void setNumberOfGears(byte numberOfGears) {
        this.numberOfGears = numberOfGears;
    }

    public boolean isShuffleMode() {
        return shuffleMode;
    }

    public void setShuffleMode(boolean shuffleMode) {
        this.shuffleMode = shuffleMode;
    }
}
