package com.company.GUI;

import com.company.Controller.EquipmentBuyingController;
import com.company.Main;
import com.company.Model.EquipmentRequest;
import com.company.Model.Project;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class EquipmentBuyingScreen {
    private final FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ValidateRequest.fxml"));

    public Scene getScene(Project project, EquipmentRequest equipmentRequest) {
        try {
            fxmlLoader.setControllerFactory(c -> new EquipmentBuyingController(project, equipmentRequest));
            return new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
