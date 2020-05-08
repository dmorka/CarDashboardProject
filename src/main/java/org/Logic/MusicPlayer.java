package org.Logic;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.fxml.FXML;
import javafx.scene.media.*;
import javafx.util.Duration;


public class MusicPlayer  {
    private ArrayList<Media> playlist;
    private MediaPlayer mediaPlayer = null;
    private double volume = 75;
    private int currentSong = 0;

    public String getTitle() {
        if(playlist.isEmpty())
            return "Playlist is empty";
        String title;
        try{
            title = mediaPlayer.getMedia().getMetadata().get("title").toString();
        } catch (NullPointerException e) {
            return new File(playlist.get(currentSong).getSource()).getName().replaceFirst(".mp3", "");
        }
        return title;

    }

    public String getArtist() {
        if(playlist.isEmpty())
            return "Unknown";
        String artistName;
        try{
            artistName = mediaPlayer.getMedia().getMetadata().get("artist").toString();
        } catch (NullPointerException e) {
            return "Unknown";
        }
        return artistName;
    }

    public void playSong() {
        if(playlist.isEmpty())
            return;
        else if(mediaPlayer == null)
            mediaPlayer = new MediaPlayer(playlist.get(currentSong));

        mediaPlayer.setVolume(volume);
        mediaPlayer.play();
    }

    public void pauseSong() {
        if(mediaPlayer != null)
            mediaPlayer.pause();
    }

    public void stopSong() {
        if(mediaPlayer != null)
            mediaPlayer.stop();
    }

    public void dispose() {
        if(mediaPlayer != null) {
            mediaPlayer.dispose();
            mediaPlayer = null;
            currentSong = 0;
        }
    }

    public void shufflePlaylist() {
        if(!playlist.isEmpty())
            Collections.shuffle(playlist);
    }

    public void nextSong() {
        if(playlist.isEmpty())
            return;
        boolean wasPlaying = isPlaying();
        if(mediaPlayer != null)
            mediaPlayer.stop();
        currentSong += 1;
        if(currentSong >= playlist.size()) currentSong = 0;
        mediaPlayer = new MediaPlayer(playlist.get(currentSong));
        if(wasPlaying)
            playSong();
    }

    public void previousSong() {
        if(playlist.isEmpty())
            return;
        boolean wasPlaying = isPlaying();
        if(mediaPlayer != null)
            mediaPlayer.stop();
        currentSong -= 1;
        if(currentSong < 0) currentSong = playlist.size()-1;
        mediaPlayer = new MediaPlayer(playlist.get(currentSong));
        if(wasPlaying)
            playSong();
    }

    public boolean isPlaying() {
        if(mediaPlayer == null)
            return false;

        return mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    @FXML
    public void loadSongs(String directoryPath)  {
        playlist = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(directoryPath))) {

            List<String> result = walk.map(x -> x.toString())
                    .filter(f -> f.endsWith(".mp3")).collect(Collectors.toList());

            MediaPlayer initMedia = null;
            int i = 0;
            // Wpisujemy obiekty Media (pliki mp3) do playlisty a nastepnie je inicjalizujemy
            // by przy pierwszym odtworzeniu utworu mieć wczytane wszytskie jego parametry (szczególnie czas trwania utworu)
            for(String path : result) {
                playlist.add(new Media(new File(path).toURI().toString()));
                initMedia = new MediaPlayer(playlist.get(i));
                i+=1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeVolume(double volume) {
        if(mediaPlayer != null)
            mediaPlayer.setVolume(volume);

        this.volume = volume;
    }

    public Duration getTotalDuration() {
        if(mediaPlayer == null)
            return Duration.UNKNOWN;

        return mediaPlayer.getStopTime();
    }

    public double getCurrentTime() {
        if(mediaPlayer == null)
            return 0;

        return mediaPlayer.getCurrentTime().toSeconds();
    }

    public boolean isEmpty() {
        return playlist.isEmpty();
    }
}
