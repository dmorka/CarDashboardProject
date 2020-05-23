package org.Logic;

import java.io.Serializable;

/**
 * The type Settings.
 */
public class Settings implements Serializable {
    private short maxSpeed;
    private short maxRevs;
    private short dashboardLightIntesity;
    private boolean autoLowBeam;
    private char engineType;
    private byte numberOfGears;
    private boolean shuffleMode;
    private String playlistDirectoryPath;

    /**
     * Instantiates a new Settings.
     */
    public Settings() {
        maxSpeed = 220;
        maxRevs = 8000;
        dashboardLightIntesity = 0;
        autoLowBeam = false;
        engineType = 'P';
        numberOfGears = 6;
        shuffleMode = false;
        playlistDirectoryPath = System.getenv("USERPROFILE") + "\\Music";
    }

    /**
     * Gets playlist directory path.
     *
     * @return the playlist directory path
     */
    public String getPlaylistDirectoryPath() {
        return playlistDirectoryPath;
    }

    /**
     * Sets playlist directory path.
     *
     * @param playlistDirectoryPath the playlist directory path
     */
    public void setPlaylistDirectoryPath(String playlistDirectoryPath) {
        this.playlistDirectoryPath = playlistDirectoryPath;
    }

    /**
     * Gets max speed in km/h.
     *
     * @return the max speed in km/h
     */
    public short getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Sets max speed in km/h.
     *
     * @param maxSpeed the max speed in km/h
     */
    public void setMaxSpeed(short maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Gets max revs.
     *
     * @return the max revs
     */
    public short getMaxRevs() {
        return maxRevs;
    }

    /**
     * Sets max revs.
     *
     * @param maxRevs the max revs
     */
    public void setMaxRevs(short maxRevs) {
        this.maxRevs = maxRevs;
    }

    /**
     * Gets dashboard light intesity.
     *
     * @return the dashboard light intesity
     */
    public short getDashboardLightIntesity() {
        return dashboardLightIntesity;
    }

    /**
     * Sets dashboard light intesity.
     *
     * @param dashboardLightIntesity the dashboard light intesity
     */
    public void setDashboardLightIntesity(short dashboardLightIntesity) {
        this.dashboardLightIntesity = dashboardLightIntesity;
    }

    /**
     * Is auto low beam boolean.
     *
     * @return the boolean
     */
    public boolean isAutoLowBeam() {
        return autoLowBeam;
    }

    /**
     * Sets auto low beam.
     *
     * @param autoLowBeam the auto low beam
     */
    public void setAutoLowBeam(boolean autoLowBeam) {
        this.autoLowBeam = autoLowBeam;
    }

    /**
     * Gets engine type (petrol or diesel).
     *
     * @return the engine type
     */
    public char getEngineType() {
        return engineType;
    }

    /**
     * Sets engine type (petrol or diesel).
     *
     * @param engineType the engine type
     */
    public void setEngineType(char engineType) {
        maxRevs = (short) ((engineType == 'P' || engineType == 'p') ? 8000 : 6000);
        this.engineType = engineType;
    }

    /**
     * Gets number of gears.
     *
     * @return the number of gears
     */
    public byte getNumberOfGears() {
        return numberOfGears;
    }

    /**
     * Sets number of gears.
     *
     * @param numberOfGears the number of gears
     */
    public void setNumberOfGears(byte numberOfGears) {
        this.numberOfGears = numberOfGears;
    }

    /**
     * Is shuffle mode boolean.
     *
     * @return the boolean
     */
    public boolean isShuffleMode() {
        return shuffleMode;
    }

    /**
     * Sets shuffle mode.
     *
     * @param shuffleMode the shuffle mode
     */
    public void setShuffleMode(boolean shuffleMode) {
        this.shuffleMode = shuffleMode;
    }
}
