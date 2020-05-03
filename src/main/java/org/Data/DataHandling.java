package org.Data;

import org.Logic.Dashboard;

import java.io.IOException;
import java.nio.file.Path;

public interface DataHandling {
    public void write(Path path, Dashboard dashboard) throws IOException;
    public Dashboard read(Path path) throws IOException, ClassNotFoundException;
    public String printAllData(Path path);
}
