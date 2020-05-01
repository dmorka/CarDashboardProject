module org.cardashboardproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires medusa;

    opens org.cardashboardproject to javafx.fxml;
    exports org.cardashboardproject;
}