module org.cardashboardproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires eu.hansolo.medusa;

    opens org.Presentation to javafx.fxml;
    exports org.Presentation;
}