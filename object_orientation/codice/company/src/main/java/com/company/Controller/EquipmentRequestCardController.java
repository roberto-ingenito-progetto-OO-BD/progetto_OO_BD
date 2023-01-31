package com.company.Controller;

import com.company.Model.EquipmentRequest;
import com.company.Model.Laboratory;
import com.company.Model.Project;
import com.company.PostgresDAO.LaboratoryDAOImplementation;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller della finestra che permette di richiedere attrezzatura <br/>
 */
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

    /**
     * @param laboratory Laboratorio che ha effettuato la richiesta di attrezzatura
     * @param project Progetto al quale viene fatta la richiesta di attrezzatura
     */
    public EquipmentRequestCardController(Laboratory laboratory, Project project) {
        this.laboratory = laboratory;
        this.project = project;
    }

    /// FXML METHODS

    /**
     * Funzione che si avvia subito dopo il costruttore <br/>
     * Imposta le label della finestra
     */
    private @FXML void initialize() {
        selectedLaboratoryName.setText(laboratory.getName());
        selectedProjectName.setText(project.getName());
    }

    /**
     * Funzione che viene eseguita quando viene cliccato il pulsante "Richiedi" <br/>
     * Inserisce la richiesta nel database, successivamente aggiorna il model <br/>
     * Tutti i campi: name, type, techSpecs e quantiti devono essere riempiti <br/>
     * Il campo quantity deve essere maggiore di 0
     */
    private @FXML void onRequestClick() {
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

    /**
     * Funzione che viene eseguita quando viene cliccato il pulsante "Annulla" <br/>
     * Non effettua la richiesta e torna alla schermata precedente
     */
    private @FXML void onCancelClick() {
        Stage currentStage = (Stage) nameField.getScene().getWindow();
        currentStage.close();
    }
}
