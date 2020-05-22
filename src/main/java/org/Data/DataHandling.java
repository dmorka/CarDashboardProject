package org.Data;

import javafx.collections.ObservableList;
import java.nio.file.Path;

public interface DataHandling {
    public void write(Path path, RecordModel record) throws Exception;
    public ObservableList<RecordModel> read(Path path) throws Exception;
    public String printAllData(Path path);
}
