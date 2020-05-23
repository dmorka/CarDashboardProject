package org.Data;

import javafx.scene.image.Image;
import org.Presentation.GUI;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Load files from disk.
 */
public class LoadFilesFromDisk {

    /**
     * Load files list.
     *
     * @param directoryPath the directory path
     * @param extension     the extension
     * @return the list
     * @throws IOException the io exception
     */
    public List<String> loadFiles(String directoryPath, String extension) throws IOException {
        Stream<Path> walk = Files.walk(Paths.get(directoryPath));
        return walk.map(Path::toString).filter(f -> f.endsWith("." + extension)).collect(Collectors.toList());
    }

    /**
     * Load mp 3 files list.
     *
     * @param directoryPath the directory path
     * @return the list
     * @throws IOException the io exception
     */
    public List<String> loadMp3Files(String directoryPath) throws IOException {
        return loadFiles(directoryPath, "mp3");
    }

    /**
     * Load lights hash map.
     *
     * @return the hash map
     */
    public HashMap<String, Image[]> loadLights() {
        String[] lightList = {"parkingLights", "indicatorsTurnLeft", "indicatorsTurnRight", "headlightsLowBeam",
                "headlightsHighBeam", "fogLightsFront", "fogLightsBack", "cruiseControl"};
        HashMap<String, Image[]> result = new HashMap<>();

        for (String name : lightList) {
            Image[] lights = {
                    new Image(GUI.class.getResourceAsStream("images/lights/"+ name + "Off.png")),
                    new Image(GUI.class.getResourceAsStream("images/lights/"+ name + "On.png"))
            };
            result.put(name, lights);
        }

        return result;
    }
}
