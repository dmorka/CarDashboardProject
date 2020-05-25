package org.Presentation;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.Data.RecordModel;

import java.sql.Date;


/**
 * The type Database import controller.
 */
public class DatabaseImportController {
    private RecordModel selectedRecord = null;
    @FXML
    private TableView<RecordModel> table;
    @FXML
    private TableColumn<RecordModel, Integer> col_id;
    @FXML
    private TableColumn<RecordModel, Float> col_avg_speed;
    @FXML
    private TableColumn<RecordModel, Float> col_max_speed;
    @FXML
    private TableColumn<RecordModel, Float> col_avg_fuel;
    @FXML
    private TableColumn<RecordModel, Float> col_max_fuel;
    @FXML
    private TableColumn<RecordModel, Float> col_journey_dist;
    @FXML
    private TableColumn<RecordModel, Integer> col_journey_time;
    @FXML
    private TableColumn<RecordModel, Integer> col_counter;
    @FXML
    private TableColumn<RecordModel, Float> col_day_counter_1;
    @FXML
    private TableColumn<RecordModel, Float> col_day_counter_2;
    @FXML
    private TableColumn<RecordModel, Date> col_create_date;

    /**
     * Instantiates a new Database import controller.
     */
    public DatabaseImportController() {
    }

    /**
     * Load db.
     *
     * @param set the set
     */
    public void loadDB(ObservableList<RecordModel> set) {
        col_id.setCellValueFactory(new PropertyValueFactory<RecordModel, Integer>("id"));
        col_avg_speed.setCellValueFactory(new PropertyValueFactory<RecordModel, Float>("avgSpeed"));
        col_max_speed.setCellValueFactory(new PropertyValueFactory<RecordModel, Float>("maxSpeed"));
        col_avg_fuel.setCellValueFactory(new PropertyValueFactory<RecordModel, Float>("avgFuel"));
        col_max_fuel.setCellValueFactory(new PropertyValueFactory<RecordModel, Float>("maxFuel"));
        col_journey_dist.setCellValueFactory(new PropertyValueFactory<RecordModel, Float>("journeyDistance"));
        col_journey_time.setCellValueFactory(new PropertyValueFactory<RecordModel, Integer>("journeyTime"));
        col_day_counter_1.setCellValueFactory(new PropertyValueFactory<RecordModel, Float>("dayCounter1"));
        col_day_counter_2.setCellValueFactory(new PropertyValueFactory<RecordModel, Float>("dayCounter2"));
        col_counter.setCellValueFactory(new PropertyValueFactory<RecordModel, Integer>("counter"));
        col_create_date.setCellValueFactory(new PropertyValueFactory<RecordModel, Date>("createDate"));
        table.setItems(set);
    }

    @FXML
    private void onMousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            selectedRecord = table.getSelectionModel().getSelectedItem();
            Stage stage = (Stage) table.getScene().getWindow();
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
    }

    /**
     * Gets selected record.
     *
     * @return the selected record
     */
    public RecordModel getSelectedRecord() {
        return selectedRecord;
    }
}
