package com.company.GUI;

import com.company.Controller.EmployeeDashboardController;
import com.company.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class EmployeeDashboard {
    private final FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("EmployeeDashboard.fxml"));

    public EmployeeDashboard(EmployeeDashboardController controller) {
        fxmlLoader.setController(controller);
    }


    public Scene getScene() {
        try {
            return new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
