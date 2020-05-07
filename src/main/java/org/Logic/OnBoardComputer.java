package org.Logic;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class OnBoardComputer implements Serializable, Comparable<OnBoardComputer> {
    public float avgSpeed;
    public int maxSpeed;
    public LocalDateTime journeyTime = null;
    public float journeyDistance;
    public float avgCombustion;
    public float maxCombustion;

    public float avgSpeed() {
        return 0;
    }

    public int maxSpeed() {
        return 0;
    }

    public long journeyTime() {
        return 0;
    }

    public float journeyDistance() {
        return 0;
    }

    public float avgCombustion() {
        return 0;
    }

    public float getMaxCombustion() {
        return maxCombustion;
    }

    public void setMaxCombustion(float maxCombustion) {
        this.maxCombustion = maxCombustion;
    }

    public float getAvgSpeed() {
        return avgSpeed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OnBoardComputer person = (OnBoardComputer) o;
        return Objects.equals(avgSpeed, person.avgSpeed) &&
                Objects.equals(avgCombustion, person.avgCombustion) &&
                Objects.equals(maxSpeed, person.maxSpeed) &&
                Objects.equals(journeyTime, person.journeyTime) &&
                Objects.equals(journeyDistance, person.journeyDistance);
    }

    @Override
    public int compareTo(OnBoardComputer o) {
        if(equals(o)) {
            return 0;
        }
        int journeyDistanceCmp = Float.compare(journeyDistance, o.journeyDistance);
        if(journeyDistanceCmp == 0) {
            int journeyTimeCmp = journeyTime.compareTo(o.journeyTime);
            if(journeyTimeCmp == 0) {
                int avgSpeedCmp = Float.compare(avgSpeed, o.avgSpeed);

                if(avgSpeedCmp==0) {
                    int avgCombustionCmp = Float.compare(avgCombustion, o.avgCombustion);

                    if(avgCombustionCmp == 0)
                        return Integer.compare(maxSpeed, o.maxSpeed);

                    return avgCombustionCmp;
                }

                return avgSpeedCmp;
            }
            return journeyTimeCmp;
        }
        return journeyDistanceCmp;
    }

    public void setAvgSpeed(float avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getJourneyTime() {
        if(journeyTime != null)
        {
            int min = (int)(Duration.between(journeyTime, LocalDateTime.now()).getSeconds() / 60);
            int h = (int)(min / 60);
            return h + "h " + min % 60 + " min";
        }

        return "0h 0min";
    }

    public void startJourneyTime() {
        this.journeyTime = LocalDateTime.now();
    }

    public float getJourneyDistance() {
        return Math.round(journeyDistance * 10.0) / 10.0f;
    }

    public void setJourneyDistance(float journeyDistance) {
        this.journeyDistance = journeyDistance;
    }

    public float getAvgCombustion() {
        return avgCombustion;
    }

    public void setAvgCombustion(float avgCombustion) {
        this.avgCombustion = avgCombustion;
    }



}