/***
 * Data layer including serialization and read/write to xml file or database.
 */
package org.Data;

import javafx.collections.ObservableList;

import java.nio.file.Path;

/**
 * The interface Data handling. Created to write and read fom databases or files, for example XML.
 */
public interface DataHandling {
    /**
     * Write to file or database.
     *
     * @param path   the path
     * @param record the record
     * @throws Exception the exception
     */
    void write(Path path, RecordModel record) throws Exception;

    /**
     * Read observable list from file or database.
     *
     * @param path the path
     * @return the observable list
     * @throws Exception the exception
     */
    ObservableList<RecordModel> read(Path path) throws Exception;

    /**
     * Print all data string.
     *
     * @param path the path
     * @return the string
     */
    String printAllData(Path path);
}
