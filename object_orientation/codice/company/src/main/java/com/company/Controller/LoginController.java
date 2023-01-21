package com.company.Controller;

import com.company.GUI.Dashboard;
import com.company.Model.EmpType;
import com.company.PostgresDAO.EmployeeDAOImplementation;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;


public class LoginController {
    @FXML
    private TextField passwordField;
    @FXML
    private TextField emailField;
    @FXML
    private ToggleButton projectButton;
    @FXML
    private ToggleButton laboratoryButton;

    @FXML
    private void signIn() {
        if (projectButton.isSelected()) {
            projectLogin();
        }

        if (laboratoryButton.isSelected()) {
            laboratoryLogin();
        }
    }

    @FXML
    private void toggleProjectButton() {
        // se sto portando il bottone a false, allora lo riporto a true
        if (!projectButton.isSelected()) {
            projectButton.setSelected(true);
        }
        // altrimenti sto portando il bottone a true, quindi disattivo l'altro
        else {
            laboratoryButton.setSelected(false);
        }
        projectButton.setStyle("-fx-background-color: #3c6e71");
        laboratoryButton.setStyle("-fx-background-color: #C2C2C2");
    }

    @FXML
    private void toggleLaboratoryButton() {
        // se sto portando il bottone a false, allora lo riporto a true
        if (!laboratoryButton.isSelected()) {
            laboratoryButton.setSelected(true);
        }
        // altrimenti sto portando il bottone a true, quindi disattivo l'altro
        else {
            projectButton.setSelected(false);
        }
        laboratoryButton.setStyle("-fx-background-color: #3c6e71");
        projectButton.setStyle("-fx-background-color: #C2C2C2");
    }


    private void projectLogin() {
        EmployeeDAOImplementation login = new EmployeeDAOImplementation();
        boolean logged = login.projectSalariedLogin(emailField.getText(), passwordField.getText());
        System.out.println(logged);
    }

    private void laboratoryLogin() {
        EmployeeDAOImplementation employeeLogin;
        Stage stage;
        // controllo dei TextField
        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            System.out.println("testo vuoto");
            return;
        }

        // interazione db - login
        employeeLogin = new EmployeeDAOImplementation();
        Dashboard.empType = employeeLogin.baseEmpLogin(emailField.getText(), passwordField.getText());

        // interazione db - nuova connessione attraverso il ruolo corretto

        // chiusura del login
        stage = (Stage) emailField.getScene().getWindow();

        // caricamento della nuova scena
        Dashboard dashboard = new Dashboard();
        Scene dashboardScene = dashboard.getScene();

        stage.close();

        stage.setTitle("Employee Dashboard");
        stage.setScene(dashboardScene);
        stage.setResizable(false);
        stage.show();
    }

}