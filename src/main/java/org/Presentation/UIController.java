package org.Presentation;

import org.Data.Serialization;
import org.Logic.Dashboard;

import java.io.IOException;

public class UIController {
    protected Dashboard dashboard;

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

    public Dashboard getDashboard() {
        return dashboard;
    }

    public void refresh() {}
    public void startStopEngine() {}
    public void reloadAfterSettings() {}
}
