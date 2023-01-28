package com.company.Controller;

import com.company.Connection.DatabaseConnection;
import com.company.Model.EmpType;
import com.company.Model.Employee;
import com.company.Model.Laboratory;
import com.company.Model.Project;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SelectedLaboratoryController {
    final Laboratory laboratory;

    /// FXML OBJECTS
    private @FXML Label laboratoryNameLabel;

    private @FXML TableView<Employee> employeesTable;
    private @FXML TableView<Project> workingProjectsTable;
    private @FXML TableView<Project> projectsTable;

    /// CONSTRUCTOR
    public SelectedLaboratoryController(Laboratory selectedLaboratory) {
        this.laboratory = selectedLaboratory;
    }

    /// FXML METHODS
    private @FXML void initialize() {
        laboratoryNameLabel.setText(laboratory.getName());

        employeesTable.setPlaceholder(new Label("Nessun impiegato lavora al laboratorio"));
        workingProjectsTable.setPlaceholder(new Label("Il laboratorio non lavora a nessun progetto"));
        projectsTable.setPlaceholder(new Label("Non ci sono progetti a cui puoi lavorare"));

        // imposta le colonne della tabella "employeesTable"
        employeesTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("cf"));
        employeesTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("fullName"));

        // imposta le colonne della tabella "workingProjectsTable"
        workingProjectsTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("cup"));
        workingProjectsTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));

        // imposta le colonne della tabella "projectsTable"
        projectsTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("cup"));
        projectsTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        projectsTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("startDate"));
        projectsTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("deadline"));

        employeesTable.getItems().addAll(laboratory.getEmployees()); // riempie la tabella "employeesTable"
        workingProjectsTable.getItems().addAll(laboratory.getProjects()); // riempie la tabella "workingProjectsTable"
        projectsTable.getItems().addAll(getProjects()); // riempie la tabella "projectsTable"
    }


    /// FXML METHODS

    /**
     * Funzione che viene eseguita quando viene cliccata una riga
     * della tabella "workingProjectsTable"
     */
    private @FXML void onWorkingProjectClick() {
        ButtonType yesButton = new ButtonType("Si", ButtonBar.ButtonData.YES);
        Alert alert = new Alert(
                Alert.AlertType.NONE,
                "",
                ButtonType.NO,
                yesButton
        );
        alert.setTitle("Attenzione");

        Project selectedProject = workingProjectsTable.getSelectionModel().getSelectedItem();
        workingProjectsTable.getSelectionModel().clearSelection();

        // se non è stato selezionato nulla, termina la funzione
        if (selectedProject == null) return;

        alert.setContentText("Vuoi lasciare il progetto \""
                + selectedProject.getName()
                + "\"?"
        );

        // avvia il dialog e aspetta
        alert.showAndWait();

        if (alert.getResult() == yesButton) {
            // TODO: implementare leave project

            // rimuove il progetto dalla tabella dei progetti a cui partecipa
            workingProjectsTable.getItems().removeIf(project -> project.getCup().equals(selectedProject.getCup()));

            // aggiunge il progetto alla tabella dei progetti partecipabili
            projectsTable.getItems().add(selectedProject);

            projectsTable.refresh();
            workingProjectsTable.refresh();
        }
    }

    /**
     * Funzione che viene eseguita quando viene cliccata una riga
     * della tabella "projectsTable"
     */
    private @FXML void onProjectClick() {
        ButtonType yesButton = new ButtonType("Si", ButtonBar.ButtonData.YES);
        Alert alert = new Alert(
                Alert.AlertType.NONE,
                "",
                ButtonType.NO,
                yesButton
        );
        alert.setTitle("Attenzione");

        Project selectedProject = projectsTable.getSelectionModel().getSelectedItem();
        projectsTable.getSelectionModel().clearSelection();

        // se non è stato selezionato nulla, termina la funzione
        if (selectedProject == null) return;

        alert.setContentText("Vuoi partecipare al progetto \""
                + selectedProject.getName()
                + "\"?"
        );

        // avvia il dialog e aspetta
        alert.showAndWait();
        if (alert.getResult() == yesButton) {
            // TODO: implementare funzione che permette al laboratorio di partecipare al progetto
            //  sostanzialmente, la funzione take_part

            // rimuove il progetto dalla tabella dei progetti partecipabili
            projectsTable.getItems().removeIf(project -> project.getCup().equals(selectedProject.getCup()));

            // aggiunge il progetto alla tabella dei progetti a cui partecipa il laboratorio
            workingProjectsTable.getItems().add(selectedProject);

            workingProjectsTable.refresh();
            projectsTable.refresh();
        }


    } /// METHODS

    /**
     * @return Restituisce tutti i progetti NON TERMINATI presenti nel database, escludendo i progetti ai quali già lavora
     */
    private ArrayList<Project> getProjects() {
        DatabaseConnection db;
        ArrayList<Project> projects = new ArrayList<>();
        ResultSet resultSet;

        String query = "SELECT cup, name, start_date, deadline " +
                "FROM project " +
                "WHERE end_date IS NULL";

        try {
            db = DatabaseConnection.baseEmpInstance(EmpType.senior);
            resultSet = db.connection.createStatement().executeQuery(query);

            while (resultSet.next()) {
                Project project = new Project(
                        resultSet.getString("cup"),
                        resultSet.getString("name"),
                        null,
                        resultSet.getDate("start_date").toLocalDate(),
                        null,
                        resultSet.getDate("deadline") == null ? null : resultSet.getDate("start_date").toLocalDate()
                );

                projects.add(project);
            }

            // rimuove dall'array "projects", i progetti a cui il laboratorio già partecipa
            for (Project labProject : laboratory.getProjects()) {
                projects.removeIf(
                        dbProject -> dbProject.getCup().equals(labProject.getCup())
                );
            }

            return projects;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
