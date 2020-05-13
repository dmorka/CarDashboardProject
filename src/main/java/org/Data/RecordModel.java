package org.Data;

import java.time.LocalDateTime;

public class RecordModel {
    private int id, counter, journeyTime;
    private float avgSpeed, maxSpeed, avgFuel, maxFuel, journeyDistance, dayCounter1, dayCounter2;
    private LocalDateTime createDate;

    public RecordModel(int id, int counter, int journeyTime, float avgSpeed, float maxSpeed, float avgFuel, float maxFuel, float journeyDistance, float dayCounter1, float dayCounter2, LocalDateTime createDate) {
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

    public void setId(int id) {
        this.id = id;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void setJourneyTime(int journeyTime) {
        this.journeyTime = journeyTime;
    }

    public void setAvgSpeed(float avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setAvgFuel(float avgFuel) {
        this.avgFuel = avgFuel;
    }

    public void setMaxFuel(float maxFuel) {
        this.maxFuel = maxFuel;
    }

    public void setJourneyDistance(float journeyDistance) {
        this.journeyDistance = journeyDistance;
    }

    public void setDayCounter1(float dayCounter1) {
        this.dayCounter1 = dayCounter1;
    }

    public void setDayCounter2(float dayCounter2) {
        this.dayCounter2 = dayCounter2;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public int getId() {
        return id;
    }

    public int getCounter() {
        return counter;
    }

    public int getJourneyTime() {
        return journeyTime;
    }

    public float getAvgSpeed() {
        return avgSpeed;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public float getAvgFuel() {
        return avgFuel;
    }

    public float getMaxFuel() {
        return maxFuel;
    }

    public float getJourneyDistance() {
        return journeyDistance;
    }

    public float getDayCounter1() {
        return dayCounter1;
    }

    public float getDayCounter2() {
        return dayCounter2;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }
}
