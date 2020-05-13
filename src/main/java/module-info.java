module org.cardashboardproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires eu.hansolo.medusa;
    requires org.controlsfx.controls;
    requires javafx.media;
    requires java.sql;

    opens org.Presentation to javafx.fxml;
    exports org.Presentation;
}