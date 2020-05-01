/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cardashboardproject;

import javafx.application.Application;
import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Y879
 */
public class MultiGaugeJavaFX extends Application {
    Pane    pane;
    @Override
    public void start(Stage primaryStage) {
       
        
        StackPane root = new StackPane();      
        
        MultiGauge gauge = new MultiGauge();        
        pane  = gauge.initGraphics();       
        root.getChildren().addAll(pane);        
        
        Scene scene = new Scene(root, 402, 470);        
        primaryStage.setTitle("Hello World!");
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}




