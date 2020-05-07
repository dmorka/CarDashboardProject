package org.Presentation;

import org.Logic.Dashboard;

public class UIController {
    protected Dashboard dashboard;

    public UIController() {
        this.dashboard = new Dashboard();
    }

    public Dashboard getDashboard() {
        return dashboard;
    }

    public void refresh() {}
    public void startStopEngine() {}
    public void reloadAfterSettings() {}
}
