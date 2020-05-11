package org.Logic;

import org.Presentation.UIController;

public class SpeedThread extends Thread {
    private final UIController uiController;
    private boolean engineRunning;
    private final Dashboard dashboard;
    private OnBoardComputer onBoardComputer;
    private int startAfter;

    //Konstruktor klasy
    public SpeedThread(UIController uiController, int startAfter) {
        this.uiController = uiController;
        this.startAfter = startAfter;
        this.dashboard = uiController.getDashboard();
        this.onBoardComputer = this.dashboard.getOnBoardComputer();
    }

    public void setEngineRunning(boolean running) {
        this.engineRunning = running;
    }

    public void run() {
        try {
            Thread.sleep(startAfter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(!engineRunning) {
            while(dashboard.getSpeed() > 0) {
                dashboard.subSpeed(1);
                synchronized (uiController) {
                    uiController.refresh();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }
        }
        float distance = 0.0f;
        long startTime = System.currentTimeMillis();
        float revs = 0;
        float gearMaxSpeed = 0;
        while(engineRunning) {
            synchronized (uiController) {
                if (dashboard.isKeyUp() && dashboard.getSpeed() < dashboard.getCurrentGearMaxSpeed()) {
                    dashboard.addSpeed(1);
                } else if (dashboard.isKeyDown() && dashboard.getSpeed() >= 3) {
                    dashboard.subSpeed(3);
                } else {
                    dashboard.subSpeed(1);
                }

                //if(dashboard.getCurrentGear() > 0 ) {
                    try {

                        if(dashboard.getCurrentGear() != 0 )
                            gearMaxSpeed = dashboard.getCurrentGearMaxSpeed();

                        revs = (dashboard.getSettings().getMaxRevs()-1000) * (dashboard.getSpeed() /  gearMaxSpeed);

                        dashboard.setRevs((int)revs);
                    } catch (NegativeValueException e) {
                        e.printStackTrace();
                    }
                //}

                //Czas próbkowania co 1s
                if(System.currentTimeMillis() - startTime >= 1000) {
                    startTime = System.currentTimeMillis();
                    distance = dashboard.getSpeed() * (1f/3600f);
                    dashboard.setCounter(dashboard.getCounter() + distance);
                    dashboard.setDayCounter1(dashboard.getDayCounter1() + distance);
                    dashboard.setDayCounter2(dashboard.getDayCounter2() + distance);
                    onBoardComputer.setJourneyDistance(onBoardComputer.getJourneyDistance() + distance);

                }

                uiController.refresh();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        }
    }

}
