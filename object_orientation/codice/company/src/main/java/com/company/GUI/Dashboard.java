package com.company.GUI;

import com.company.Main;
import com.company.Model.EmpType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class Dashboard {
    public static EmpType empType;

    private final FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("EmployeeDashboard.fxml"));

    public Scene getScene() {
        try {
            return new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
