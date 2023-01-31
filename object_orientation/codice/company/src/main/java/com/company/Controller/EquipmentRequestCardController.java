package com.company.Controller;

import com.company.Model.EquipmentRequest;
import com.company.Model.Laboratory;
import com.company.Model.Project;
import com.company.PostgresDAO.LaboratoryDAOImplementation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EquipmentRequestCardController {
    final Laboratory laboratory;
    final Project project;
    EquipmentRequest equipmentRequest;

    /// FXML OBJECTS
    private @FXML Label selectedLaboratoryName;
    private @FXML Label selectedProjectName;
    private @FXML Label errorLabel;

    private @FXML TextField nameField;
    private @FXML TextField quantityField;
    private @FXML TextField typeField;
    private @FXML TextArea techSpecsField;


    /// CONSTRUCTOR
    public EquipmentRequestCardController(Laboratory laboratory, Project project) {
        this.laboratory = laboratory;
        this.project = project;
    }

    /// FXML METHODS
    private @FXML void initialize() {
        selectedLaboratoryName.setText(laboratory.getName());
        selectedProjectName.setText(project.getName());
    }

    private @FXML void onRequestClick(ActionEvent actionEvent) {
        LaboratoryDAOImplementation laboratoryDAO = new LaboratoryDAOImplementation();

        String name = nameField.getText();
        String type = typeField.getText();
        String techSpecs = techSpecsField.getText();
        int quantity;

        if (name.isEmpty() || type.isEmpty() || techSpecs.isEmpty()) {
            errorLabel.setVisible(true);
            errorLabel.setText("Compila tutti i campi");
            return;
        }

        try {
            quantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException e) {
            errorLabel.setVisible(true);
            errorLabel.setText("Inserisci un numero intero nel campo \"Quantità\"");
            return;
        }

        if (quantity <= 0) {
            errorLabel.setVisible(true);
            errorLabel.setText("Inserisci un numero maggiore di 0 nel campo \"Quantità\"");
            return;
        }

        equipmentRequest = new EquipmentRequest(name, techSpecs, type, quantity);
        equipmentRequest.setLaboratory(laboratory);
        equipmentRequest.setProject(project);

        laboratoryDAO.equipmentRequest(equipmentRequest);

        Stage currentStage = (Stage) nameField.getScene().getWindow();
        currentStage.setUserData(equipmentRequest);

        currentStage.close();
    }

    private @FXML void onCancelClick(ActionEvent actionEvent) {
        Stage currentStage = (Stage) nameField.getScene().getWindow();
        currentStage.close();
    }
}
