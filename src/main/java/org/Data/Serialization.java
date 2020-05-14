package org.Data;

import org.Logic.Dashboard;

import java.io.*;

public class  Serialization {

    public static void write(Dashboard dashboard) throws IOException {
        FileOutputStream fos = new FileOutputStream("startupData");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(dashboard);
        oos.close();
        fos.close();
    }

    public static Dashboard readDashboard() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("startupData");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Dashboard dashboard = (Dashboard) ois.readObject();
        ois.close();
        fis.close();

        return dashboard;
    }

}
