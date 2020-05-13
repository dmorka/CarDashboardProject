package org.Data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.Logic.Dashboard;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Database implements DataHandling{
    static final String connectionString = "jdbc:sqlserver://localhost:1433;databaseName=dashboard";
    static final String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    Connection con = null;
    Statement stsm = null;

    public void connect(){
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(connectionString, "car", "12345678");
            stsm = con.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() throws SQLException {
        if(con != null){
            con.close();
        }
        if(stsm != null){
            stsm.close();
        }
    }

    @Override
    public void write(Path path, Dashboard dashboard) throws IOException {

    }

    @Override
    public ObservableList<RecordModel> read(Path path) throws Exception {
        String query = "SELECT * FROM DashboardData";
        ResultSet result = stsm.executeQuery(query);

        ObservableList<RecordModel> set = FXCollections.observableArrayList();
        while(result.next()){
            RecordModel record = new RecordModel(result.getInt(1), result.getFloat(2), );
            record.setId(result.getInt(1));
            record.put("avgSpeed", result.getFloat(2));
            record.put("maxSpeed", result.getFloat(3));
            record.put("avgFuel", result.getFloat(4));
            record.put("maxFuel", result.getFloat(5));
            record.put("journeyDistance", result.getFloat(6));
            record.put("journeyTime", result.getInt(7));
            record.put("counter", result.getInt(8));
            record.put("dayCounter1", result.getFloat(9));
            record.put("dayCounter2", result.getFloat(10));
            record.put("createTime", result.getDate(11));
            set.add(record);
        }
        return set;
    }

    @Override
    public String printAllData(Path path) {
        return null;
    }
}
