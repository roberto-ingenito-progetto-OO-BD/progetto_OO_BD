module com.company {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.company to javafx.fxml;
    exports com.company;
    exports com.company.Controller;
}