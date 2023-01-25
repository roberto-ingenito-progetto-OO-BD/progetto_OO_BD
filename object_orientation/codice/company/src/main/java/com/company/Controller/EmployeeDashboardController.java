package com.company.Controller;

import com.company.GUI.Login;
import com.company.Model.Employee;
import com.company.Model.Project;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EmployeeDashboardController {
    private final Employee employee;

    /// FXML Objects
    @FXML
    private Tab laboratoryTab;
    @FXML
    private Tab projectsTab;

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

    /**
     * vbox dove andranno i progetti al quale lavora il laboratorio
     */
    @FXML
    private VBox labProjectsVBox;

    @FXML
    private TabPane tabPane;

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

        // imposta il nome e il topic del laboratorio
        laboratoryNameLabel.setText("Laboratorio: " + employee.getLaboratory().getName());
        laboratoryTopicLabel.setText("Topic: " + employee.getLaboratory().getTopic());

        // TODO: attualmente riempie la lista di progetti solo con i nomi dei progetti
        for (Project project : employee.getLaboratory().getProjects()) {
            Label label = new Label();
            label.setText(project.getName());
            labProjectsVBox.getChildren().add(label);
        }

        // imposta quali elementi possono essere visualizzati in base al tipo dell'impiegato loggato
        switch (employee.getType()) {
            case junior, middle:
                projectsLabel.setVisible(false);
                projectsTab.setDisable(true);
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

}