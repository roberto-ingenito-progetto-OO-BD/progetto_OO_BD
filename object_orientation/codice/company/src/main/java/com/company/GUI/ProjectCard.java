package com.company.GUI;

import com.company.Controller.ProjectCardController;
import com.company.Main;
import com.company.Model.Employee;
import com.company.Model.Project;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class ProjectCard {
    private final FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ProjectCard.fxml"));

    public Scene getScene(Project project, @Nullable Employee employee, TableView tableView) {
        try {
            fxmlLoader.setControllerFactory(c -> new ProjectCardController(project, employee, tableView));
            return new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
