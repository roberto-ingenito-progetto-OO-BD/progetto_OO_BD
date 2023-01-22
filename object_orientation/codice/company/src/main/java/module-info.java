module com.company {
    requires javafx.controls;
    requires javafx.fxml;

    //    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires javafx.graphics;

    // permette a javafx.fxml di accedere al package Controller
    opens com.company.Controller to javafx.fxml;
    exports com.company;
    exports com.company.Controller;

}