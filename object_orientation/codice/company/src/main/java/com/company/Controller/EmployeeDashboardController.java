package com.company.Controller;

import com.company.GUI.Login;
import com.company.Model.Employee;
import com.company.Model.Project;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class EmployeeDashboardController {
    private final Employee employee;

    /// FXML Objects
    @FXML
    private Tab laboratoryTab;
    @FXML
    private Tab projectsTab;

    @FXML
    private Button viewAllProjects;

    @FXML
    private Label projectsLabel;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label empTypeLabel;
    @FXML
    private Label laboratoryTopicLabel;
    @FXML
    private Label laboratoryNameLabel;
    @FXML
    private StackPane noWorkingProjectLabel;

    @FXML
    private TabPane tabPane;

    @FXML
    private TableView<Project> labWorkingProjectsTableView;
    @FXML
    private TableColumn<Project, String> cupColumn;
    @FXML
    private TableColumn<Project, String> nameColumn;

    /// CONSTRUCTOR
    public EmployeeDashboardController(Employee employee) {
        this.employee = employee;
    }

    /// FXML METHODS
    @FXML
    public void initialize() {
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

    /**
     * seleziona la tab del progetto
     */
    @FXML
    private void goToProjectTab() {
        tabPane.getSelectionModel().select(projectsTab);
    }

    /**
     * seleziona la tab del laboratorio
     */
    @FXML
    private void goToLaboratoryTab() {
        tabPane.getSelectionModel().select(laboratoryTab);
    }

    @FXML
    private void onLogOut() {
        Stage oldStage = (Stage) userNameLabel.getScene().getWindow();
        Stage newStage;

        Login loginGUI = new Login();
        Scene loginScene = loginGUI.getScene();

        // creo un nuovo stage
        newStage = new Stage();
        newStage.setTitle("Azienda Dashboard");
        newStage.setScene(loginScene);
        newStage.setResizable(false);

        // chiudo il vecchio e apro il nuovo
        oldStage.close();
        newStage.show();
    }

    public void onTableRowClick() {
        Project selected = labWorkingProjectsTableView.getSelectionModel().getSelectedItem();
        labWorkingProjectsTableView.getSelectionModel().clearSelection();

        if (selected != null) {
            System.out.println(selected);
        }
    }
}