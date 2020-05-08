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
            while(dashboard.speed > 0) {
                dashboard.speed -= 1;
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
        while(engineRunning) {
            synchronized (uiController) {
                if (dashboard.isKeyUp() && dashboard.speed < dashboard.getCurrentGearMaxSpeed()) {
                    dashboard.speed += 2;
                    if(dashboard.speed > onBoardComputer.getMaxSpeed())
                        onBoardComputer.setMaxSpeed(dashboard.speed);
                } else if (dashboard.isKeyDown() && dashboard.getSpeed() >= 8) {
                    dashboard.speed -= 3;
                } else if (dashboard.speed > dashboard.getLowerGearMaxSpeed()*(3/5f)) {
                    dashboard.speed -= 1;
                }

                if(dashboard.currentGear > 0 )
                    dashboard.revs = (int)(dashboard.getSettings().maxRevs * ((float)dashboard.speed/(float)dashboard.getCurrentGearMaxSpeed()));

                //Czas próbkowania co 1s
                if(System.currentTimeMillis() - startTime >= 1000) {
                    startTime = System.currentTimeMillis();
                    distance = dashboard.speed * (1f/3600f);
                    dashboard.counter += distance;
                    dashboard.dayCounter1 += distance;
                    dashboard.dayCounter2 += distance;
                    onBoardComputer.journeyDistance += distance;

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
