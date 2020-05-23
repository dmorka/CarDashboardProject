package org.Data;

import org.Logic.OnBoardComputer;

import java.sql.Date;


/**
 * The type Record model including parameters wrote to file/database.
 */
public class RecordModel {
    private int id;
    private int counter;
    private int journeyTime;
    private float avgSpeed;
    private float maxSpeed;
    private float avgFuel;
    private float maxFuel;
    private float journeyDistance;
    private float dayCounter1;
    private float dayCounter2;
    private Date createDate;

    /**
     * Instantiates a new, empty Record model.
     */
    public RecordModel() {
    }

    /**
     * Instantiates a new Record model.
     *
     * @param id              the id
     * @param avgSpeed        the avg speed
     * @param maxSpeed        the max speed
     * @param avgFuel         the avg fuel
     * @param maxFuel         the max fuel
     * @param journeyDistance the journey distance
     * @param journeyTime     the journey time
     * @param counter         the counter
     * @param dayCounter1     the day counter 1
     * @param dayCounter2     the day counter 2
     * @param createDate      the create date
     */
    public RecordModel(int id, float avgSpeed, float maxSpeed, float avgFuel, float maxFuel,
                       float journeyDistance, int journeyTime, int counter,
                       float dayCounter1, float dayCounter2, Date createDate) {
        this.id = id;
        this.counter = counter;
        this.journeyTime = journeyTime;
        this.avgSpeed = avgSpeed;
        this.maxSpeed = maxSpeed;
        this.avgFuel = avgFuel;
        this.maxFuel = maxFuel;
        this.journeyDistance = journeyDistance;
        this.dayCounter1 = dayCounter1;
        this.dayCounter2 = dayCounter2;
        this.createDate = createDate;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets counter in km.
     *
     * @return the counter in km
     */
    public int getCounter() {
        return counter;
    }

    /**
     * Sets counter in km.
     *
     * @param counter the counter in km
     */
    public void setCounter(int counter) {
        this.counter = counter;
    }

    /**
     * Gets journey time in minutes.
     *
     * @return the journey time in minutes
     */
    public int getJourneyTime() {
        return journeyTime;
    }

    /**
     * Sets journey time in minutes.
     *
     * @param journeyTime the journey time in minutes
     */
    public void setJourneyTime(int journeyTime) {
        this.journeyTime = journeyTime;
    }

    /**
     * Gets avg speed in km/h.
     *
     * @return the avg speed in km/h
     */
    public float getAvgSpeed() {
        return avgSpeed;
    }

    /**
     * Sets avg speed in km/h.
     *
     * @param avgSpeed the avg speed in km/h
     */
    public void setAvgSpeed(float avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    /**
     * Gets max speed in km/h.
     *
     * @return the max speed in km/h
     */
    public float getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Sets max speed in km/h.
     *
     * @param maxSpeed the max speed in km/h
     */
    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Gets avg fuel consumption.
     *
     * @return the avg fuel consumption
     */
    public float getAvgFuel() {
        return avgFuel;
    }

    /**
     * Sets avg fuel consumption.
     *
     * @param avgFuel the avg fuel consumption
     */
    public void setAvgFuel(float avgFuel) {
        this.avgFuel = avgFuel;
    }

    /**
     * Gets max fuel consumption.
     *
     * @return the max fuel consumption
     */
    public float getMaxFuel() {
        return maxFuel;
    }

    /**
     * Sets max fuel consumption.
     *
     * @param maxFuel the max fuel consumption
     */
    public void setMaxFuel(float maxFuel) {
        this.maxFuel = maxFuel;
    }

    /**
     * Gets journey distance in km.
     *
     * @return the journey distance in km
     */
    public float getJourneyDistance() {
        return journeyDistance;
    }

    /**
     * Sets journey distance in km.
     *
     * @param journeyDistance the journey distance in km
     */
    public void setJourneyDistance(float journeyDistance) {
        this.journeyDistance = journeyDistance;
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
     * Gets create date.
     *
     * @return the create date
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * Sets create date.
     *
     * @param createDate the create date
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * Sets create date from string.
     *
     * @param createDate the create date
     */
    public void setCreateDate(String createDate) {
        this.createDate = Date.valueOf(createDate);
    }

    /**
     * Sets on board computer type.
     *
     * @param onBoardComputer the on board computer type
     */
    public void setOnBoardComputer(OnBoardComputer onBoardComputer) {
        avgSpeed = onBoardComputer.getAvgSpeed();
        maxSpeed = onBoardComputer.getMaxSpeed();
        avgFuel = onBoardComputer.getAvgCombustion();
        maxFuel = onBoardComputer.getMaxCombustion();
        journeyDistance = onBoardComputer.getJourneyDistance();
        journeyTime = onBoardComputer.getJourneyTime();
    }
}
