package org.Data;

import javafx.scene.image.Image;
import javafx.scene.media.Media;

import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LoadFilesFromDisk {

    public List<String> loadFiles(String directoryPath, String extension) throws IOException {
        Stream<Path> walk = Files.walk(Paths.get(directoryPath));
        return walk.map(Path::toString).filter(f -> f.endsWith("."+extension)).collect(Collectors.toList());
    }

    public List<String> loadMp3Files(String directoryPath) throws IOException {
        return loadFiles(directoryPath, "mp3");
    }

    public HashMap<String, Image[]> loadLights() throws IOException {
        String[] lightList = {"parkingLights", "indicatorsTurnLeft", "indicatorsTurnRight", "headlightsLowBeam",
                    "headlightsHighBeam", "fogLightsFront", "fogLightsBack", "cruiseControl"};
        HashMap<String, Image[]> result = new HashMap<>();
        String absolutePath =  (new File("src\\main\\resources\\org\\Presentation\\images\\lights").getAbsolutePath())+"\\";
        for(String name : lightList) {
            Image[] lights = {
                    new Image(new FileInputStream(absolutePath + name + "Off.png")),
                    new Image(new FileInputStream(absolutePath + name + "On.png"))
            };
            result.put(name, lights);
        }

        return result;
    }
}
