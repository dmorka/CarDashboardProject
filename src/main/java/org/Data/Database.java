package org.Data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.Logic.Dashboard;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.*;

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
            RecordModel record = new RecordModel(result.getInt("id"),
                                                result.getFloat("avg_speed"),
                                                result.getFloat("max_speed"),
                                                result.getFloat("avg_fuel"),
                                                result.getFloat("max_fuel"),
                                                result.getFloat("journey_distance"),
                                                result.getInt("journey_time"),
                                                result.getInt("total_counter"),
                                                result.getFloat("day_counter1"),
                                                result.getFloat("day_counter2"),
                                                result.getDate("create_data"));
            set.add(record);
        }
        return set;
    }

    @Override
    public String printAllData(Path path) {
        return null;
    }
}
