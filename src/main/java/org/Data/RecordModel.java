package org.Data;

import org.Logic.OnBoardComputer;

import java.sql.Date;


/**
 * The type Record model.
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
     * Instantiates a new Record model.
     */
    public RecordModel() {}

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
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets counter.
     *
     * @param counter the counter
     */
    public void setCounter(int counter) {
        this.counter = counter;
    }

    /**
     * Sets journey time.
     *
     * @param journeyTime the journey time
     */
    public void setJourneyTime(int journeyTime) {
        this.journeyTime = journeyTime;
    }

    /**
     * Sets avg speed.
     *
     * @param avgSpeed the avg speed
     */
    public void setAvgSpeed(float avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    /**
     * Sets max speed.
     *
     * @param maxSpeed the max speed
     */
    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Sets avg fuel.
     *
     * @param avgFuel the avg fuel
     */
    public void setAvgFuel(float avgFuel) {
        this.avgFuel = avgFuel;
    }

    /**
     * Sets max fuel.
     *
     * @param maxFuel the max fuel
     */
    public void setMaxFuel(float maxFuel) {
        this.maxFuel = maxFuel;
    }

    /**
     * Sets journey distance.
     *
     * @param journeyDistance the journey distance
     */
    public void setJourneyDistance(float journeyDistance) {
        this.journeyDistance = journeyDistance;
    }

    /**
     * Sets day counter 1.
     *
     * @param dayCounter1 the day counter 1
     */
    public void setDayCounter1(float dayCounter1) {
        this.dayCounter1 = dayCounter1;
    }

    /**
     * Sets day counter 2.
     *
     * @param dayCounter2 the day counter 2
     */
    public void setDayCounter2(float dayCounter2) {
        this.dayCounter2 = dayCounter2;
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
     * Sets create date.
     *
     * @param createDate the create date
     */
    public void setCreateDate(String createDate) {
        this.createDate = Date.valueOf(createDate);
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
     * Gets counter.
     *
     * @return the counter
     */
    public int getCounter() {
        return counter;
    }

    /**
     * Gets journey time.
     *
     * @return the journey time
     */
    public int getJourneyTime() {
        return journeyTime;
    }

    /**
     * Gets avg speed.
     *
     * @return the avg speed
     */
    public float getAvgSpeed() {
        return avgSpeed;
    }

    /**
     * Gets max speed.
     *
     * @return the max speed
     */
    public float getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Gets avg fuel.
     *
     * @return the avg fuel
     */
    public float getAvgFuel() {
        return avgFuel;
    }

    /**
     * Gets max fuel.
     *
     * @return the max fuel
     */
    public float getMaxFuel() {
        return maxFuel;
    }

    /**
     * Gets journey distance.
     *
     * @return the journey distance
     */
    public float getJourneyDistance() {
        return journeyDistance;
    }

    /**
     * Gets day counter 1.
     *
     * @return the day counter 1
     */
    public float getDayCounter1() {
        return dayCounter1;
    }

    /**
     * Gets day counter 2.
     *
     * @return the day counter 2
     */
    public float getDayCounter2() {
        return dayCounter2;
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
     * Sets on board computer.
     *
     * @param onBoardComputer the on board computer
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
