package com.company.GUI;

import com.company.Controller.ProjectSalariedDashboardController;
import com.company.Main;
import com.company.Model.ProjectSalaried;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class ProjectSalariedDashboard {
    private final FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ProjectSalariedDashboard.fxml"));

    public Scene getScene(ProjectSalaried employee) {
        try {
            fxmlLoader.setControllerFactory(c -> new ProjectSalariedDashboardController(employee));
            return new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}