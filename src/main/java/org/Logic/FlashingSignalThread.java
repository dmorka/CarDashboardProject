package org.Logic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FlashingSignalThread extends Thread {

    ImageView light;
    Image lightEnable;
    private boolean running;

    public FlashingSignalThread(ImageView light, Image lightEnable) {
        this.lightEnable = lightEnable;
        this.light = light;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void run() {
        this.light.setImage(lightEnable);
        while(running) {
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