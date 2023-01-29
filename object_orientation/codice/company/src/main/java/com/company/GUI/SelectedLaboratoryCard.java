package com.company.GUI;

import com.company.Controller.SelectedLaboratoryCardController;
import com.company.Main;
import com.company.Model.Laboratory;
import com.company.Model.Project;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

public class SelectedLaboratoryCard {
    private final FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("SelectedLaboratoryCard.fxml"));

    public Scene getScene(
            @NotNull Laboratory selectedLaboratory,
            @NotNull ArrayList<Project> referentProjects
    ) {
        try {
            fxmlLoader.setControllerFactory(c -> new SelectedLaboratoryCardController(selectedLaboratory, referentProjects));
            return new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
