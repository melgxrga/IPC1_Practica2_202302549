module com.melgar {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.opencsv;

    opens com.melgar to javafx.fxml;
    opens com.melgar.models to javafx.base; // Añade esta línea
    exports com.melgar;
}