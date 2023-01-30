package com.company.GUI;

import com.company.Controller.EmployeeDashboardController;
import com.company.Controller.HiringScreenController;
import com.company.Main;
import com.company.Model.Employee;
import com.company.Model.Project;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class HiringScreen {
    private final FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("HiringScreen.fxml"));

    public Scene getScene(Project project, Employee employee) {
        try {
            fxmlLoader.setControllerFactory(c -> new HiringScreenController(project, employee));
            return new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
