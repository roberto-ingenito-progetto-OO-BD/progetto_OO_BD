package com.company.Controller;

import com.company.GUI.ProjectCard;
import com.company.Model.*;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

public class EmployeeDashboardController {
    private final Employee employee;

    /// FXML Objects
    private @FXML Tab laboratoryTab;
    private @FXML Tab projectsTab;

    private @FXML ListView<String> equipmentListView;

    private @FXML Button viewAllProjectsButton;

    private @FXML Label projectsLabel;
    private @FXML Label laboratoryLabel;

    private @FXML Label userNameLabel;
    private @FXML Label empTypeLabel;
    private @FXML Label laboratoryTopicLabel;
    private @FXML Label laboratoryNameLabel;
    private @FXML Label laboratoryManagerLabel;

    private @FXML StackPane noWorkingProjectLabel;

    private @FXML TabPane tabPane;

    private @FXML TableView<Project> labWorkingProjectsTableView;
    private @FXML TableColumn<Project, String> cupColumn;
    private @FXML TableColumn<Project, String> nameColumn;

    /// CONSTRUCTOR
    public EmployeeDashboardController(Employee employee) {
        this.employee = employee;
    }

    /// FXML METHODS
    private @FXML void initialize() {
        // imposta il testo che appare quando la lista è vuota
        equipmentListView.setPlaceholder(new Label("Non c'è attrezzatura in questo laboratorio"));

        // imposta il testo che appare quando la tabella è vuota
        labWorkingProjectsTableView.setPlaceholder(new Label("Il laboratorio non lavora a nessun progetto"));

        // imposta nome, cognome e tipo dell'impiegato nella barra in alto
        userNameLabel.setText(employee.getFirstName() + " " + employee.getLastName());
        empTypeLabel.setText(employee.getType().toString());

        // l'impiegato lavora in un laboratorio
        if (employee.getLaboratory() != null) {
            // imposta il nome e il topic del laboratorio
            laboratoryNameLabel.setText(employee.getLaboratory().getName());
            laboratoryTopicLabel.setText(employee.getLaboratory().getTopic());
            laboratoryManagerLabel.setText(employee.getLaboratory().getScientificManager().getFullName());


            // imposta il tipo delle colonne della tabella "labWorkingProjectsTableView"
            cupColumn.setCellValueFactory(new PropertyValueFactory<>("cup"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

            // riempie la tabella "labWorkingProjectsTableView"
            for (Project project : employee.getLaboratory().getProjects()) {
                labWorkingProjectsTableView.getItems().add(project);
            }

            // riempie la ListView "equipmentListView"
            for (Equipment equipment : employee.getLaboratory().getEquipment()) {
                equipmentListView.getItems().add(
                        equipment.getName() + ", " + equipment.getType() + ", " + equipment.getTechSpecs()
                );
            }

        }
        // l'impiegato non lavora in un laboratorio
        else {
            // rendo non visibile l'intera tab
            // il doppio getParent arriva al padre di tutti gli elementi nella tab
            labWorkingProjectsTableView.getParent().getParent().setVisible(false);

            // rendo visibile la label "noWorkingProjectLabel" per fornire il messaggio
            noWorkingProjectLabel.setVisible(true);
        }

        // imposta quali elementi possono essere visualizzati in base al tipo dell'impiegato loggato
        switch (employee.getType()) {
            case junior, middle:
                projectsLabel.setVisible(false); // rendo non visibile il pulsante "Proggetti"
                projectsTab.setDisable(true); // Disabilito la tab dei progetti
                break;
            case senior, manager:
                break;
        }

        // rendo visibile il pulsante per visualizzare tutti i progetti sul database
        // il compito spetta solo al manager di laboratorio (senior)
        if (employee.getType() == EmpType.senior)
            viewAllProjectsButton.setVisible(true);
    }

    private @FXML void goToProjectTab() {
        changeTab(projectsTab);
    }

    private @FXML void goToLaboratoryTab() {
        changeTab(laboratoryTab);
    }

    private @FXML void onLogOutClick() {
        Stage oldStage = (Stage) userNameLabel.getScene().getWindow();
        LoginController.logOut(oldStage);
    }

    private @FXML void onTableRowClick() {
        ProjectCard projectCard;
        @Nullable Project selected = labWorkingProjectsTableView.getSelectionModel().getSelectedItem();
        labWorkingProjectsTableView.getSelectionModel().clearSelection();

        Stage newStage;
        Scene newScene;

        if (selected != null) {
            // crea una nuova schermata con le informazioni del progetto riferito al contratto selezionato
            projectCard = new ProjectCard();
            newScene = projectCard.getScene(selected, null);

            newStage = new Stage();
            newStage.setTitle("Project informations");
            newStage.setScene(newScene);
            newStage.setResizable(false);

            // imposta la nuova finistra come "modal",
            // quindi blocca tutti gli eventi delle altre finestre finché questa non viene chiusa
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.show();
        }
    }


    /// METHODS
    private void changeTab(Tab tab) {
        projectsLabel.setOpacity(0.4);
        laboratoryLabel.setOpacity(0.4);

        if (tab == projectsTab) projectsLabel.setOpacity(1);

        if (tab == laboratoryTab) laboratoryLabel.setOpacity(1);

        tabPane.getSelectionModel().select(tab);
    }
}