module com.company {
    requires javafx.controls;
    requires javafx.fxml;

    //    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires javafx.graphics;
    requires org.jetbrains.annotations;
    requires org.postgresql.jdbc;

    // permette a javafx.fxml di accedere al package Controller
    opens com.company.Controller to javafx.fxml;
    exports com.company;
    exports com.company.Model;
    exports com.company.Controller;

}