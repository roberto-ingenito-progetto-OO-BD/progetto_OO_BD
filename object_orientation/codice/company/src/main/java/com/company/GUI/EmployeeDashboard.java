package com.company.GUI;

import com.company.Controller.EmployeeDashboardController;
import com.company.Main;
import com.company.Model.Employee;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class EmployeeDashboard {
    private final FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("EmployeeDashboard.fxml"));

    public Scene getScene(Employee employee) {
        try {
            fxmlLoader.setControllerFactory(c -> new EmployeeDashboardController(employee));
            return new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
