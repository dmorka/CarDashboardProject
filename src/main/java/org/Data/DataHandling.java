package org.Data;

import javafx.collections.ObservableList;
import org.Logic.Dashboard;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public interface DataHandling {
    public void write(Path path, RecordModel record) throws Exception;
    public ObservableList<RecordModel> read(Path path) throws Exception;
    public String printAllData(Path path);
}
