package com.company.Controller;

import com.company.Model.Employee;
import com.company.Model.Project;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class EmployeeDashboardController {
    private final Employee employee;

    /// FXML Objects
    private @FXML Tab laboratoryTab;
    private @FXML Tab projectsTab;

    private @FXML Button viewAllProjects;

    private @FXML Label projectsLabel;
    private @FXML Label userNameLabel;
    private @FXML Label empTypeLabel;
    private @FXML Label laboratoryTopicLabel;
    private @FXML Label laboratoryNameLabel;

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
        // imposta nome, cognome e tipo dell'impiegato nella barra in alto
        userNameLabel.setText(employee.getFirstName() + " " + employee.getLastName());
        empTypeLabel.setText(employee.getType().toString());

        // l'impiegato lavora in un laboratorio
        if (employee.getLaboratory() != null) {
            // imposta il nome e il topic del laboratorio
            laboratoryNameLabel.setText(employee.getLaboratory().getName());
            laboratoryTopicLabel.setText(employee.getLaboratory().getTopic());

            // imposta il tipo delle colonne della tabella "labWorkingProjectsTableView"
            cupColumn.setCellValueFactory(new PropertyValueFactory<>("cup"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

            // riempie la tabella "labWorkingProjectsTableView"
            for (Project project : employee.getLaboratory().getProjects()) {
                labWorkingProjectsTableView.getItems().add(project);
            }
        } else // l'impiegato non lavora in un laboratorio
        {
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

                // rendo non visibile il pulsante per visualizzare tutti i progetti sul database
                // il compito spetta solo al manager di laboratorio
                viewAllProjects.setVisible(false);

                break;
            case senior:
                // break;
            case manager:
                break;
        }
    }

    private @FXML void goToProjectTab() {
        tabPane.getSelectionModel().select(projectsTab);
    }

    private @FXML void goToLaboratoryTab() {
        tabPane.getSelectionModel().select(laboratoryTab);
    }

    private @FXML void onLogOutClick() {
        Stage oldStage = (Stage) userNameLabel.getScene().getWindow();
        LoginController.logOut(oldStage);
    }

    private @FXML void onTableRowClick() {
        Project selected = labWorkingProjectsTableView.getSelectionModel().getSelectedItem();
        labWorkingProjectsTableView.getSelectionModel().clearSelection();

        if (selected != null) {
            System.out.println(selected);
        }
    }
}