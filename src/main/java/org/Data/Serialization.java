package org.Data;

import org.Logic.Dashboard;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class  Serialization {

    public static void write(Dashboard dashboard) throws IOException {
        Path path = Files.createDirectories(Paths.get(System.getenv("LOCALAPPDATA") + "\\Dashboard"));
        Files.createDirectories(path);
        FileOutputStream fos = new FileOutputStream(path.toString()+"\\startupData");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(dashboard);
        oos.close();
        fos.close();
    }

    public static Dashboard readDashboard() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(Paths.get(System.getenv("LOCALAPPDATA") + "\\Dashboard\\startupData").toString());
        ObjectInputStream ois = new ObjectInputStream(fis);
        Dashboard dashboard = (Dashboard) ois.readObject();
        ois.close();
        fis.close();

        return dashboard;
    }

}
