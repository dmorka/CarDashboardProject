package org.Logic;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class MusicPlayer {
    private ArrayList<AudioClip> playlist;
    MediaPlayer mediaPlayer;

    public MusicPlayer(String path) {
        String path2 = new File(path).getAbsolutePath();
        Media media = new Media(new File(path2).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

    }

    public void playSong() {
        mediaPlayer.play();
    }

//    @FXML
//    public void loadSongs(String directoryPath)  {
//        File folder = new File("C:/Users/Dawid/Music/Music");
//
//        //Implementing FilenameFilter to retrieve only txt files
//
//        FilenameFilter txtFileFilter = new FilenameFilter()
//        {
//            @Override
//            public boolean accept(File dir, String name)
//            {
//                if(name.endsWith(".mp3"))
//                {
//                    return true;
//                }
//                else
//                {
//                    return false;
//                }
//            }
//        };
//
//        //Passing txtFileFilter to listFiles() method to retrieve only txt files
//
//        File[] files = folder.listFiles(txtFileFilter);
//
//        for (File file : files)
//        {
//                playlist.add(new AudioClip("C:\\Users\\Dawid\\Music\\music\\happy_adveture.mp3") );
//        }
//
//    }
}
