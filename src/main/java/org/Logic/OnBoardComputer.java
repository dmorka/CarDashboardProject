package org.Logic;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The type On board computer.
 */
public class OnBoardComputer implements Serializable, Comparable<OnBoardComputer> {
    private float avgSpeed;
    private float maxSpeed;

    /**
     * Sets journey start time.
     *
     * @param journeyStartTime the journey start time
     */
    public void setJourneyStartTime(LocalDateTime journeyStartTime) {
        this.journeyStartTime = journeyStartTime;
    }

    private LocalDateTime journeyStartTime = null;
    private LocalDateTime journeyPauseTime = null;
    private float journeyDistance;
    private float avgCombustion;
    private float maxCombustion;

    /**
     * Gets max combustion.
     *
     * @return the max combustion
     */
    public float getMaxCombustion() {
        return Math.round(maxCombustion * 100f) / 100f;
    }

    /**
     * Sets max combustion.
     *
     * @param maxCombustion the max combustion
     */
    public void setMaxCombustion(float maxCombustion) {
        this.maxCombustion = maxCombustion;
    }

    /**
     * Gets avg speed.
     *
     * @return the avg speed
     */
    public int getAvgSpeed() {
        return Math.round(avgSpeed);
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
                        return Float.compare(maxSpeed, o.maxSpeed);

                    return avgCombustionCmp;
                }

                return avgSpeedCmp;
            }
            return journeyTimeCmp;
        }
        return journeyDistanceCmp;
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
     * Gets max speed.
     *
     * @return the max speed
     */
    public int getMaxSpeed() {
        return Math.round(maxSpeed);
    }

    /**
     * Get journey time int.
     *
     * @return the int
     */
    public int getJourneyTime(){
        if(journeyStartTime == null)
            return 0;
        return (int)(Duration.between(journeyStartTime, LocalDateTime.now()).getSeconds() / 60);
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
     * Gets journey start time.
     *
     * @return the journey start time
     */
    public String getJourneyStartTime() {
        if(journeyStartTime != null)
        {
            int min = (int)(Duration.between(journeyStartTime, LocalDateTime.now()).getSeconds() / 60);
            int h = min / 60;
            return h + "h " + min % 60 + " min";
        }

        return "0h 0min";
    }

    /**
     * Start journey time.
     */
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

    /**
     * Pause journey time.
     */
    public void pauseJourneyTime() {
        this.journeyPauseTime = LocalDateTime.now();
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
     * Sets journey distance.
     *
     * @param journeyDistance the journey distance
     */
    public void setJourneyDistance(float journeyDistance) {
        this.journeyDistance = journeyDistance;
    }

    /**
     * Gets avg combustion.
     *
     * @return the avg combustion
     */
    public float getAvgCombustion() {
        return Math.round(avgCombustion * 100f) / 100f;
    }

    /**
     * Sets avg combustion.
     *
     * @param avgCombustion the avg combustion
     */
    public void setAvgCombustion(float avgCombustion) {
        this.avgCombustion = avgCombustion;
    }

    /**
     * Sets journey time.
     *
     * @param minutes the minutes
     */
    public void setJourneyTime(int minutes) {
        LocalDateTime localDateTime = LocalDateTime.now();
        setJourneyStartTime(localDateTime.minusMinutes(minutes));
    }

}