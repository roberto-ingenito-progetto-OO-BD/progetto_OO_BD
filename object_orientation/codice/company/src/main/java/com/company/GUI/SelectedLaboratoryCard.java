package com.company.GUI;

import com.company.Controller.SelectedLaboratoryController;
import com.company.Main;
import com.company.Model.Laboratory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SelectedLaboratoryCard {
    private final FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("SelectedLaboratoryCard.fxml"));

    public Scene getScene(@NotNull Laboratory selectedLaboratory) {
        try {
            fxmlLoader.setControllerFactory(c -> new SelectedLaboratoryController(selectedLaboratory));
            return new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
