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

    public void connect() {
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
    public void write(Path path, RecordModel record) throws Exception {
        connect();
        String query = "INSERT INTO DashboardData(avg_speed, max_speed, avg_fuel, max_fuel, " +
                       "journey_distance, journey_time, total_counter, day_counter1, day_counter2)" +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setFloat(1, record.getAvgSpeed());
        preparedStatement.setFloat(2, record.getMaxSpeed());
        preparedStatement.setFloat(3, record.getAvgFuel());
        preparedStatement.setFloat(4, record.getMaxFuel());
        preparedStatement.setFloat(5, Math.round(record.getJourneyDistance()*100.0)/100.0f);
        preparedStatement.setInt(6, record.getJourneyTime());
        preparedStatement.setInt(7, record.getCounter());
        preparedStatement.setFloat(8, Math.round(record.getDayCounter1()*100.0)/100.0f);
        preparedStatement.setFloat(9, Math.round(record.getDayCounter2()*100.0)/100.0f);
        preparedStatement.executeUpdate();
        disconnect();
    }

    @Override
    public ObservableList<RecordModel> read(Path path) throws Exception {
        connect();
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
        disconnect();
        return set;
    }

    @Override
    public String printAllData(Path path) {
        return null;
    }
}
