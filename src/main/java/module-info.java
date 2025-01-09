module com.k2pbo.tubespbo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.k2pbo.tubespbo to javafx.fxml;
    exports com.k2pbo.tubespbo;
    requires transitive java.sql;
}