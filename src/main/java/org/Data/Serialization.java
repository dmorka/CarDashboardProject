package org.Data;

import org.Logic.Dashboard;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The type Serialization.
 */
public class  Serialization {

    /**
     * Write.
     *
     * @param dashboard the dashboard
     * @throws IOException the io exception
     */
    public static void write(Dashboard dashboard) throws IOException {
        Path path = Files.createDirectories(Paths.get(System.getenv("LOCALAPPDATA") + "\\Dashboard"));
        Files.createDirectories(path);
        FileOutputStream fos = new FileOutputStream(path.toString()+"\\startupData");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(dashboard);
        oos.close();
        fos.close();
    }

    /**
     * Read dashboard dashboard.
     *
     * @return the dashboard
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    public static Dashboard readDashboard() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(Paths.get(System.getenv("LOCALAPPDATA") + "\\Dashboard\\startupData").toString());
        ObjectInputStream ois = new ObjectInputStream(fis);
        Dashboard dashboard = (Dashboard) ois.readObject();
        ois.close();
        fis.close();

        return dashboard;
    }

}
