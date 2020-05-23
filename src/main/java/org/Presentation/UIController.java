package org.Presentation;

import org.Data.Serialization;
import org.Logic.Dashboard;

import java.io.IOException;

/**
 * The type Ui controller.
 */
public class UIController {
    /**
     * The Dashboard.
     */
    protected Dashboard dashboard;

    /**
     * Instantiates a new Ui controller.
     */
    public UIController() {
//        this.dashboard = new Dashboard();
        try {
            this.dashboard = Serialization.readDashboard();
            dashboard.init();
        } catch (IOException | ClassNotFoundException e) {
            this.dashboard = new Dashboard();
            System.out.println(e.getMessage());

        }
    }

    /**
     * Gets dashboard.
     *
     * @return the dashboard
     */
    public Dashboard getDashboard() {
        return dashboard;
    }

    /**
     * Refresh.
     */
    public void refresh() {}

    /**
     * Start stop engine.
     */
    public void startStopEngine() {}

    /**
     * Reload after settings.
     */
    public void reloadAfterSettings() {}

    /**
     * Switch engine.
     *
     * @param enable      the enable
     * @param interrupted the interrupted
     */
    public void switchEngine(boolean enable, boolean interrupted) {}

    /**
     * Switch off cruise control.
     */
    public void switchOffCruiseControl() {}
}
