package com.company.GUI;

import com.company.Controller.EquipmentRequestCardController;
import com.company.Main;
import com.company.Model.Laboratory;
import com.company.Model.Project;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.IOException;

public class EquipmentRequestCard {
    final Laboratory laboratory;
    final Project project;

    private final FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("EquipmentRequestCard.fxml"));

    public EquipmentRequestCard(Laboratory laboratory, Project project) {
        this.laboratory = laboratory;
        this.project = project;
    }

    public Scene getScene() {
        try {
            fxmlLoader.setControllerFactory(c -> new EquipmentRequestCardController(laboratory, project));
            return new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
