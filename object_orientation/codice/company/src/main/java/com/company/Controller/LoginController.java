package com.company.Controller;

import com.company.GUI.EmployeeDashboard;
import com.company.Model.EmpType;
import com.company.Model.Employee;
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
        } else if (laboratoryButton.isSelected()) {
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
        EmpType loggedEmpType;
        Employee loggedEmployee;

        // controllo dei TextField
        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            System.out.println("testo vuoto");
            return;
        }

        // interazione db - login
        employeeLogin = new EmployeeDAOImplementation();
        loggedEmpType = employeeLogin.baseEmpLogin(emailField.getText(), passwordField.getText());

        // interazione db - nuova connessione attraverso il ruolo corretto

        // chiusura del login
        stage = (Stage) emailField.getScene().getWindow();


        loggedEmployee = employeeLogin.getEmployeeData(loggedEmpType, emailField.getText());


        // Istanzio il controller e imposto il campo email
        EmployeeDashboardController controller = new EmployeeDashboardController(loggedEmployee);

        // caricamento della nuova scena
        //
        // passo il controller al costruttore della dashboard
        // il quale impostera il controller in input, come il controller dello scene
        EmployeeDashboard dashboard = new EmployeeDashboard(controller);
        Scene dashboardScene = dashboard.getScene();

        stage.close();
        stage.setTitle("Employee Dashboard");
        stage.setScene(dashboardScene);
        stage.setResizable(false);
        stage.show();
    }

}