package org.Data;

import javafx.collections.ObservableList;
import org.Logic.Dashboard;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class Serialization implements DataHandling {
    @Override
    public void write(Path path, Dashboard dashboard) throws IOException {
        FileOutputStream fos = new FileOutputStream("startupData");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(dashboard);
        oos.close();
        fos.close();
    }

    @Override
    public ObservableList<RecordModel> read(Path path) throws IOException, ClassNotFoundException {
        return null;
    }

    public Dashboard readDashboard(Path path) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("startupData");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Dashboard dashboard = (Dashboard) ois.readObject();
        ois.close();
        fis.close();

        return dashboard;
    }

    @Override
    public String printAllData(Path path) {
        return null;
    }
}
