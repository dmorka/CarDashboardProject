package org.Presentation;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.Data.Database;

import java.util.ArrayList;
import java.util.HashMap;


public class DatabaseImportController {
    @FXML
    private TableColumn col_id;
    @FXML
    private TableColumn col_avg_speed;
    @FXML
    private TableColumn col_max_speed;
    @FXML
    private TableColumn col_avg_fuel;
    @FXML
    private TableColumn col_max_fuel;
    @FXML
    private TableColumn col_journey_dist;
    @FXML
    private TableColumn col_journey_time;
    @FXML
    private TableColumn col_counter;
    @FXML
    private TableColumn col_day_counter_1;
    @FXML
    private TableColumn col_day_counter_2;
    @FXML
    private TableColumn col_create_date;

    @FXML
    private void initialize(){
        Database loadFromDatabase = new Database();
        ArrayList<HashMap<String, Object>> set = null;
        try {
            set = loadFromDatabase.read(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        col_avg_speed.setCellValueFactory(new PropertyValueFactory<>());
    }
}
