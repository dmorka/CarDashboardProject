module org.cardashboardproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires eu.hansolo.medusa;
    requires org.controlsfx.controls;
    requires javafx.media;
    requires java.sql;
    requires com.jfoenix;
    requires CustomStage;
    requires FX.BorderlessScene;
    requires lanterna;

    opens org.Presentation to javafx.fxml;
    opens org.Data to javafx.base;
    exports org.Presentation;
}