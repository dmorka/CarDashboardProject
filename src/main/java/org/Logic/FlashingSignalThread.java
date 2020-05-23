package org.Logic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * The type Flashing signal thread.
 */
public class FlashingSignalThread extends Thread {

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
            this.light.setOpacity(0.88);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
            this.light.setOpacity(0.2);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }
        }
    }

}