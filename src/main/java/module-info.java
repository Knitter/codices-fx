module eu.sergiolopes.codices.desktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;

    opens eu.sergiolopes.codices to javafx.fxml;
    exports eu.sergiolopes.codices;
    exports eu.sergiolopes.codices.controllers;
    opens eu.sergiolopes.codices.controllers to javafx.fxml;
}