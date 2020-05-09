package org.Logic;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javafx.scene.media.*;
import javafx.util.Duration;
import org.Data.LoadFiles;


public class MusicPlayer  {
    private LoadFiles loadFiles = new LoadFiles();
    private ArrayList<Media> playlist = new ArrayList<>();
    private MediaPlayer mediaPlayer = null;
    private double volume = 75;
    private int currentSong = 0;
    private String playlistDirectoryPath;
    private Runnable autoPlayNext = null;

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

    public String getArtist() throws InterruptedException {
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
        if(autoPlayNext != null)
            mediaPlayer.setOnEndOfMedia(autoPlayNext);
    }

    public void autoPlayNext(Runnable runnable) {
        if(mediaPlayer != null)
            mediaPlayer.setOnEndOfMedia(runnable);
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

    public void setAutoPlayNext(Runnable autoPlayNext) {
        this.autoPlayNext = autoPlayNext;
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

    public void loadSongs(String directoryPath) throws IOException {
        playlistDirectoryPath = directoryPath;
        List<String> result = loadFiles.loadMp3Files(directoryPath);
        playlist = new ArrayList<>();
        // Wpisujemy obiekty Media (pliki mp3) do playlisty a nastepnie je inicjalizujemy
        // by przy pierwszym odtworzeniu utworu mieć wczytane wszytskie jego parametry (szczególnie czas trwania utworu)
        MediaPlayer initMedia = null;
        for(int i = 0; i < result.size(); i++) {
            playlist.add(new Media(new File(result.get(i)).toURI().toString()));
            initMedia = new MediaPlayer(playlist.get(i));
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

    public String getPlaylistDirectoryPath() {
        return playlistDirectoryPath;
    }

    public void onReady(Runnable runnable) {
        if(mediaPlayer != null)
            mediaPlayer.setOnReady(runnable);
    }

    public void setPlaylistDirectoryPath(String playlistDirectoryPath) {
        this.playlistDirectoryPath = playlistDirectoryPath;
    }
}
