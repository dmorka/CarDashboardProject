
package org.cardashboardproject;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Gauge.NeedleShape;
import eu.hansolo.medusa.Gauge.NeedleSize;
import eu.hansolo.medusa.Gauge.SkinType;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.TickMarkType;
import java.awt.Paint;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

class MultiGauge extends Region {
    private static final double PREFERRED_WIDTH  = 320;
    private static final double PREFERRED_HEIGHT = 320;
    private static final double MINIMUM_WIDTH    = 5;
    private static final double MINIMUM_HEIGHT   = 5;
    private static final double MAXIMUM_WIDTH    = 1024;
    private static final double MAXIMUM_HEIGHT   = 1024;
    private        double  size;
    private        Gauge   rpmGauge;
    private        Gauge   tempGauge;
    private        Gauge   oilGauge;
    private        Pane    pane;


    //constructor
    public MultiGauge() {
        
        //getStylesheets().add("CSS/styles.css");
        
        init();
        
        
    }


    // ******************** Initialization ************************************
    private void init() {
        if (Double.compare(getPrefWidth(), 0.0) <= 0 || Double.compare(getPrefHeight(), 0.0) <= 0 ||
            Double.compare(getWidth(), 0.0) <= 0 || Double.compare(getHeight(), 0.0) <= 0) {
            if (getPrefWidth() > 0 && getPrefHeight() > 0) {
                setPrefSize(getPrefWidth(), getPrefHeight());
            } else {
                setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
            }
        }

        if (Double.compare(getMinWidth(), 0.0) <= 0 || Double.compare(getMinHeight(), 0.0) <= 0) {
            setMinSize(MINIMUM_WIDTH, MINIMUM_HEIGHT);
        }

        if (Double.compare(getMaxWidth(), 0.0) <= 0 || Double.compare(getMaxHeight(), 0.0) <= 0) {
            setMaxSize(MAXIMUM_WIDTH, MAXIMUM_HEIGHT);
        }
    }

    Pane initGraphics() {
        
        
        Button btn = new Button();
        btn.setText("Start");
        
        RadialGradient gradient1 = new RadialGradient(0,
            .1,
            200,
            200,
            170,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0, Color.BLUE),
            new Stop(1, Color.BLACK));
        
        rpmGauge = GaugeBuilder.create()
                               .borderPaint(Color.HONEYDEW)    //FlORALWHITE , HONEYDEW, AQUA
                               .foregroundBaseColor(Color.WHITE)
                              
                               .borderWidth(3.0)
                               .prefSize(400, 400)
                               .startAngle(290)
                               .angleRange(220)
                               .minValue(0)
                               .maxValue(4000)
                               .valueVisible(false)
                               .minorTickMarksVisible(false)
                               .majorTickMarkType(TickMarkType.BOX)
                               .mediumTickMarkType(TickMarkType.BOX)
                               .title("RPM\nx100")
                               .needleShape(NeedleShape.ROUND)
                               .needleSize(NeedleSize.THICK)
                               .needleColor(Color.rgb(234, 67, 38))
                               .knobColor(Gauge.DARK_COLOR)
                               .customTickLabelsEnabled(true)
                               .customTickLabelFontSize(40)
                               .customTickLabels("0", "", "10", "", "20", "", "30", "", "40")
                               .animated(true)
                               .build();
        rpmGauge.setBackgroundPaint(gradient1);
               

        tempGauge = GaugeBuilder.create()
                                .skinType(SkinType.HORIZONTAL)
                                .prefSize(170, 170)
                                .autoScale(false)
                                .foregroundBaseColor(Color.RED)
                                .title("TEMP")
                                .valueVisible(false)
                                .angleRange(90)
                                .minValue(100)
                                .maxValue(250)
                                .needleShape(NeedleShape.ROUND)
                                .needleSize(NeedleSize.THICK)
                                .needleColor(Color.WHITE)
                                //.needleColor(Color.rgb(234, 67, 38))
                                .minorTickMarksVisible(false)
                                .mediumTickMarksVisible(false)
                                .majorTickMarkType(TickMarkType.BOX)
                                .knobColor(Gauge.DARK_COLOR)
                                .customTickLabelsEnabled(true)
                                .customTickLabelFontSize(36)
                                .customTickLabels("100", "", "", "", "", "", "", "175", "", "", "", "", "", "", "", "250")
                                .animated(true)
                                .build();

        oilGauge = GaugeBuilder.create()
                               .skinType(SkinType.HORIZONTAL)
                               .prefSize(170, 170)
                               .foregroundBaseColor(Color.RED)
                               .title("OIL")
                               .valueVisible(false)
                               .angleRange(90)
                               .needleShape(NeedleShape.ROUND)
                               .needleSize(NeedleSize.THICK)
                               .needleColor(Color.WHITE)
                               //.needleColor(Color.rgb(234, 67, 38))
                               .minorTickMarksVisible(false)
                               .mediumTickMarksVisible(false)
                               .majorTickMarkType(TickMarkType.BOX)
                               .knobColor(Gauge.DARK_COLOR)
                               .customTickLabelsEnabled(true)
                               .customTickLabelFontSize(36)
                               .customTickLabels("0", "", "", "", "", "50", "", "", "", "", "100")
                               .animated(true)
                               .build();
        
        
        tempGauge.setLayoutY(220);
        tempGauge.setLayoutX(203);
        
        oilGauge.setLayoutY(220);
        oilGauge.setLayoutX(40);
        
        btn.setLayoutX(190);
        btn.setLayoutY(420);
        
        pane = new Pane(rpmGauge, tempGauge, oilGauge, btn);

        //getChildren().setAll(pane);
        
          btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Starting.....");
                rpmGauge.setValue(3000.0);
                
                tempGauge.setValue(70.0);
                oilGauge.setValue(80.0);
            }
        });
        
        return pane;
    }

        
    public Gauge getRpmGauge()  { return rpmGauge; }
    public Gauge getTempGauge() { return tempGauge; }
    public Gauge getOilGauge()  { return oilGauge; }


    
   
}
