package org.Logic;

import java.io.Serializable;
import java.util.Objects;

public class OnBoardComputer implements Serializable, Comparable<OnBoardComputer> {
    public float avgSpeed;
    public int maxSpeed;
    public long journeyTime;
    public float journeyDistance;
    public float avgCombustion;

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
            int journeyTimeCmp = Long.compare(journeyTime, o.journeyTime);
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

    public long getJourneyTime() {
        return journeyTime;
    }

    public void setJourneyTime(long journeyTime) {
        this.journeyTime = journeyTime;
    }

    public float getJourneyDistance() {
        return journeyDistance;
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