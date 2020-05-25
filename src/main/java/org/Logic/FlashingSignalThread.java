package org.Logic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;

import java.io.File;

/**
 * The type Flashing signal thread.
 */
public class FlashingSignalThread extends Thread {
    /**
     * The B 1.
     */
    AudioClip b1 = new AudioClip(new File("sounds/blinkerSound1.mp3").toURI().toString());
    /**
     * The B 2.
     */
    AudioClip b2 = new AudioClip(new File("sounds/blinkerSound2.mp3").toURI().toString());
    /**
     * The Light.
     */
    ImageView light;
    /**
     * The Light enable.
     */
    Image lightEnable;
    private boolean running;

    /**
     * Instantiates a new Flashing signal thread.
     *
     * @param light       the light
     * @param lightEnable the light enable
     */
    public FlashingSignalThread(ImageView light, Image lightEnable) {
        this.lightEnable = lightEnable;
        this.light = light;
    }

    /**
     * Sets lights running.
     *
     * @param running the running lights
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    public void run() {
        this.light.setImage(lightEnable);
        while (running) {
            b1.play();
            this.light.setOpacity(0.88);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
            b2.play();
            this.light.setOpacity(0.2);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }
        }
    }

}