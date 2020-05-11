package org.Logic;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Objects;

public class OnBoardComputer implements Serializable, Comparable<OnBoardComputer> {
    private float avgSpeed;
    private int maxSpeed;
    private LocalDateTime journeyStartTime = null;
    private LocalDateTime journeyPauseTime = null;
    private float journeyDistance;
    private float avgCombustion;
    private float maxCombustion;

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
                Objects.equals(journeyStartTime, person.journeyStartTime) &&
                Objects.equals(journeyDistance, person.journeyDistance);
    }

    @Override
    public int compareTo(OnBoardComputer o) {
        if(equals(o)) {
            return 0;
        }
        int journeyDistanceCmp = Float.compare(journeyDistance, o.journeyDistance);
        if(journeyDistanceCmp == 0) {
            int journeyTimeCmp = journeyStartTime.compareTo(o.journeyStartTime);
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

    public String getJourneyStartTime() {
        if(journeyStartTime != null)
        {
            int min = (int)(Duration.between(journeyStartTime, LocalDateTime.now()).getSeconds() / 60);
            int h = min / 60;
            return h + "h " + min % 60 + " min ";
        }

        return "0h 0min";
    }

    public void startJourneyTime() {
        //Pierwsze uruchomienie silnika
        if(journeyStartTime == null)
            this.journeyStartTime = LocalDateTime.now();

        //Gdy wyłączymy silnika i po pewnym czasie ponownie uruchamiamy silnik to czas podróży jest aktualizowany by
        // tej przerwy nie wliczać w czas podróży
        if(journeyPauseTime != null) {
            journeyStartTime = journeyStartTime.plusSeconds(Duration.between(journeyPauseTime, LocalDateTime.now()).toSeconds());
        }
    }

    public void pauseJourneyTime() {
        this.journeyPauseTime = LocalDateTime.now();
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