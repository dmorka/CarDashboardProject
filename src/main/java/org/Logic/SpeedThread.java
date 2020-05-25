package org.Logic;

import org.Presentation.UIController;

import java.util.Random;

/**
 * The type Speed thread.
 */
public class SpeedThread extends Thread {
    private final UIController uiController;
    private final Dashboard dashboard;
    private boolean engineRunning;
    private boolean animationToZero;
    private final OnBoardComputer onBoardComputer;
    private final int startAfter;
    private float distance;


    /**
     * Instantiates a new Speed thread.
     *
     * @param uiController the ui controller
     * @param startAfter   the start after
     */
//Konstruktor klasy
    public SpeedThread(UIController uiController, int startAfter) {
        this.uiController = uiController;
        this.startAfter = startAfter;
        this.dashboard = uiController.getDashboard();
        this.onBoardComputer = this.dashboard.getOnBoardComputer();
    }

    /**
     * Is dashboard engine off animation activated boolean.
     *
     * @return the boolean
     */
    public boolean isAnimationToZero() {
        return animationToZero;
    }

    /**
     * Sets animation to zero.
     *
     * @param animationToZero the animation to zero
     */
    public void setAnimationToZero(boolean animationToZero) {
        this.animationToZero = animationToZero;
    }

    /**
     * Sets engine running.
     *
     * @param running the running
     */
    public void setEngineRunning(boolean running) {
        this.engineRunning = running;
    }

    /**
     * Count distances.
     */
    public void countDistances() {
        distance = dashboard.getSpeed() * (1f / 3600f);
        dashboard.setCounter(dashboard.getCounter() + distance);
        dashboard.setDayCounter1(dashboard.getDayCounter1() + distance);
        dashboard.setDayCounter2(dashboard.getDayCounter2() + distance);
    }

    public void run() {
        long startTime = System.currentTimeMillis();
        float revs = dashboard.getRevs();
        short maxRevs = dashboard.getSettings().getMaxRevs();
        float gearMaxSpeed;
        float maxSpeed = 0;
        long sumSpeed = 0;
        long sumAvgFuel = 0;
        float iter = 1;
        float fuelCoeff = (dashboard.getSettings().getEngineType() == 'P') ? 3.33f : 4.2f;
        float currFuelComb;
        distance = 0.0f;
        try {
            Thread.sleep(startAfter);
        } catch (InterruptedException ignore) { }
        if (!engineRunning) {
            revs = dashboard.getRevs();
            while ((dashboard.getSpeed() > 0 || dashboard.getRevs() > 0) && animationToZero) {
                if (System.currentTimeMillis() - startTime >= 1000) {
                    startTime = System.currentTimeMillis();
                    countDistances();
                }
                if (dashboard.getSpeed() > 0) {
                    dashboard.subSpeed(1);
                    if (dashboard.getCurrentGear() != 0)
                        revs = (dashboard.getSettings().getMaxRevs() - 1000) * (dashboard.getSpeed() / (float) dashboard.getCurrentGearMaxSpeed());
                    else
                        if(revs != 0)
                            revs *= 0.99;
                } else {
                    revs -= 100;
                }
                try {
                    dashboard.setRevs((int) ((revs >= 0) ? revs : 0));
                } catch (NegativeValueException ignore) { }
                synchronized (uiController) {
                    uiController.refresh();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignore) {}
            }
        }
        Random random = new Random();
        float lastGearMaxSpeed = 1;
        while (engineRunning) {
            synchronized (uiController) {
                if ((dashboard.isKeyUp() && dashboard.getSpeed() < dashboard.getCurrentGearMaxSpeed()) ||
                        (dashboard.getCruiseSpeed() > dashboard.getSpeed() && dashboard.isCruiseControl())) {
                    dashboard.addSpeed(1);
                    if (dashboard.getSpeed() > maxSpeed)
                        onBoardComputer.setMaxSpeed(dashboard.getSpeed());
                } else if (dashboard.isKeyDown() && dashboard.getSpeed() >= 3) {
                    if(dashboard.isCruiseControl())
                        uiController.switchOffCruiseControl();
                    dashboard.subSpeed(3);
                } else {
                    if (!uiController.getDashboard().isCruiseControl())
                        dashboard.subSpeed(1);
                    else if (dashboard.getSpeed() > dashboard.getCruiseSpeed())
                        dashboard.subSpeed(1);
                    else dashboard.addSpeed(1);

                }

                try {
                    gearMaxSpeed = dashboard.getCurrentGearMaxSpeed();
                    lastGearMaxSpeed = (dashboard.getCurrentGear() != 0) ? gearMaxSpeed : dashboard.getSettings().getMaxSpeed();
                    if (dashboard.getCurrentGear() == 0) {
                        if (dashboard.getRevs() < 1000)
                            if(dashboard.getRevs() < 900)
                                revs += 100;
                            else
                                revs += random.nextInt(20);
                        else
                            if(dashboard.getRevs() < 1050)
                                revs -= random.nextInt(20);
                            else
                                revs = (dashboard.getSettings().getMaxRevs()) * (dashboard.getSpeed() / lastGearMaxSpeed);

                    } else {

                        if (dashboard.getCurrentGear() == 1) {
                            if (dashboard.isKeyUp() && revs < maxRevs)
                                revs += (dashboard.getSettings().getMaxRevs() - 1000) * (1 / gearMaxSpeed);
                            else if (revs > 1000)
                                revs -= (dashboard.getSettings().getMaxRevs() - 1000) * (1 / gearMaxSpeed);
                        } else
                            revs = (dashboard.getSettings().getMaxRevs()) * (dashboard.getSpeed() / gearMaxSpeed);
                        if (revs < 800) {
                            uiController.switchEngine(false, true);
                        }

                    }


                    if (revs > 0)
                        dashboard.setRevs((int) revs);
                } catch (NegativeValueException ignore) {}

                //Czas próbkowania co 1s
                if (System.currentTimeMillis() - startTime >= 1000) {
                    startTime = System.currentTimeMillis();
                    sumSpeed += dashboard.getSpeed();
                    onBoardComputer.setAvgSpeed(sumSpeed / iter);
                    currFuelComb = (dashboard.getRevs() * fuelCoeff) / 1000;
                    sumAvgFuel += currFuelComb;
                    onBoardComputer.setAvgCombustion(sumAvgFuel / iter);
                    if (onBoardComputer.getMaxCombustion() < currFuelComb)
                        onBoardComputer.setMaxCombustion(currFuelComb);
                    iter += 1;
                    countDistances();
                    onBoardComputer.setJourneyDistance(onBoardComputer.getJourneyDistance() + distance);
                }
                uiController.refresh();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignore) { }
        }
    }

}
