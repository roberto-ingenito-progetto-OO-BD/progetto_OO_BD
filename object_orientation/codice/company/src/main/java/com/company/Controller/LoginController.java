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

    /**
     * aa
     */
    private void laboratoryLogin() {
        EmployeeDAOImplementation employeeDAO;

        EmpType loggedEmpType;
        Employee loggedEmployee;

        EmployeeDashboardController employeeDashboardController;
        EmployeeDashboard dashboard;

        Stage stage;
        Scene dashboardScene;

        // se la email o la password sono vuote, termina il login
        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            System.out.println("testo vuoto");
            return;
        }

        // interazione db - login
        employeeDAO = new EmployeeDAOImplementation();
        loggedEmpType = employeeDAO.baseEmpLogin(emailField.getText(), passwordField.getText());

        loggedEmployee = employeeDAO.getEmployeeData(loggedEmpType, emailField.getText());

        // Istanzio il controller passando l'employee loggato
        employeeDashboardController = new EmployeeDashboardController(loggedEmployee);

        // caricamento della nuova scena
        //
        // passo il controller al costruttore della dashboard
        // il quale lo imposterà come il controller della scena
        dashboard = new EmployeeDashboard(employeeDashboardController);
        dashboardScene = dashboard.getScene();

        // prendo lo stage corrente
        stage = (Stage) emailField.getScene().getWindow();

        // chiusura della pagina login
        stage.close();

        stage.setMinHeight(700);
        stage.setMinWidth(1000);
        stage.setTitle("Employee Dashboard");
        stage.setScene(dashboardScene);
        stage.setResizable(true);

        stage.show();
    }

}