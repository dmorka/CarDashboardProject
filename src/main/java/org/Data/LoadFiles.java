package org.Data;

import javafx.scene.media.Media;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LoadFiles {

    public List<String> loadMp3Files(String directoryPath) throws IOException {
        ArrayList<Media> playlist = new ArrayList<>();
        Stream<Path> walk = Files.walk(Paths.get(directoryPath));
        return walk.map(Path::toString).filter(f -> f.endsWith(".mp3")).collect(Collectors.toList());
    }
}
