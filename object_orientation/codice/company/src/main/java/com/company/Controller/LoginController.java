package com.company.Controller;

import com.company.GUI.EmployeeDashboard;
import com.company.GUI.Login;
import com.company.GUI.ProjectSalariedDashboard;
import com.company.Model.*;
import com.company.PostgresDAO.EmployeeDAOImplementation;
import com.company.PostgresDAO.LaboratoryDAOImplementation;
import com.company.PostgresDAO.ProjectDAOImplementation;
import com.company.PostgresDAO.ProjectSalariedDAOImplementation;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import java.util.ArrayList;


public class LoginController {
    /// FXML Objects
    private @FXML TextField passwordField;
    private @FXML TextField emailField;

    private @FXML ToggleButton projectButton;
    private @FXML ToggleButton laboratoryButton;

    private @FXML Label incorrectCredentialsLabel;

    /// FXML METHODS
    private @FXML void signIn() {
        if (projectButton.isSelected()) {
            projectLogin();
        } else if (laboratoryButton.isSelected()) {
            laboratoryLogin();
        }
    }

    private @FXML void toggleProjectButton() {
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

    private @FXML void toggleLaboratoryButton() {
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


    /// METHODS
    private void projectLogin() {
        ProjectSalariedDAOImplementation projectSalariedDAO;
        ProjectDAOImplementation projectDAO;
        ProjectSalaried loggedProjectSalaried;
        ArrayList<Contract> contracts;
        ProjectSalariedDashboard dashboard;

        Stage oldStage;
        Stage newStage;
        Scene dashboardScene;

        // se la email o la password sono vuote, termina il login
        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            System.out.println("testo vuoto");
            return;
        }

        // interazione col db - login
        projectSalariedDAO = new ProjectSalariedDAOImplementation();

        // login errato
        if (!projectSalariedDAO.projectSalariedLogin(emailField.getText(), passwordField.getText())) {
            incorrectCredentialsLabel.setVisible(true);
        } else {
            // interazione col db - ricerca del project salaried / inizializzazione del model
            loggedProjectSalaried = projectSalariedDAO.getProjectSalariedData(emailField.getText());
            contracts = projectSalariedDAO.getContracts(loggedProjectSalaried);
            // per ogni contratto - settare il referente e il manager / serviranno nella schermata di visualizzazione progetto
            projectDAO = new ProjectDAOImplementation();
            contracts.forEach(contract -> {
                contract.getProject().setLaboratories(
                        projectDAO.getWorkingLaboratories(contract.getProject().getCup())
                );
                contract.getProject().setManager(
                        projectDAO.getProjectManager(contract.getProject().getCup())
                );
                contract.getProject().setScientificReferent(
                        projectDAO.getProjectReferent(contract.getProject().getCup())
                );
            });
            loggedProjectSalaried.setContracts(contracts);

            // istanziare dashboard
            dashboard = new ProjectSalariedDashboard();
            dashboardScene = dashboard.getScene(loggedProjectSalaried);

            // prendo lo stage corrente
            oldStage = (Stage) emailField.getScene().getWindow();

            // creo un nuovo stage
            newStage = new Stage();
            newStage.setMinHeight(700);
            newStage.setMinWidth(1000);
            newStage.setTitle("Project Salaried Dashboard");
            newStage.setScene(dashboardScene);

            // chiudo il vecchio e apro il nuovo
            oldStage.close();
            newStage.show();
        }
    }

    private void laboratoryLogin() {
        EmployeeDAOImplementation employeeDAO;
        LaboratoryDAOImplementation laboratoryDAO;

        Employee loggedEmployee;
        EmpType loggedEmpType; // emp type dell'impiegato loggato
        Laboratory empWorkingLaboratory; // laboratorio in cui lavora l'impiegato
        ArrayList<Project> laboratoryWorkingProjects; // progetti a cui lavora il laboratorio

        EmployeeDashboard dashboard;

        Stage oldStage;
        Stage newStage;
        Scene dashboardScene;

        // se la email o la password sono vuote, termina il login
        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            System.out.println("testo vuoto");
            return;
        }

        // interazione db - login
        employeeDAO = new EmployeeDAOImplementation();
        laboratoryDAO = new LaboratoryDAOImplementation();

        loggedEmpType = employeeDAO.baseEmpLogin(emailField.getText(), passwordField.getText());

        if (loggedEmpType == null) {
            incorrectCredentialsLabel.setVisible(true);
            return;
        }

        // prende le informazioni dell'impiegato loggato
        loggedEmployee = employeeDAO.getEmployeeData(loggedEmpType, emailField.getText());

        // prende il laboratorio in cui lavora l'impiegato
        empWorkingLaboratory = employeeDAO.getWorkingLaboratory(loggedEmpType, loggedEmployee.getCf());

        // controllo se non è null dato che l'impiegato potrebbe non lavorare a nessun laboratorio
        if (empWorkingLaboratory != null) {
            // imposta il laboratorio in cui lavora l'impiegato
            loggedEmployee.setLaboratory(empWorkingLaboratory);

            // imposta i proggetti a cui lavora quel laboratorio
            laboratoryWorkingProjects = laboratoryDAO.getProjects(empWorkingLaboratory, loggedEmpType);
            empWorkingLaboratory.setProjects(laboratoryWorkingProjects);
        }

        // caricamento della nuova scena
        //
        // passo il controller al costruttore della dashboard
        // il quale lo imposterà come il controller della scena
        dashboard = new EmployeeDashboard();
        dashboardScene = dashboard.getScene(loggedEmployee);

        // prendo lo stage corrente
        oldStage = (Stage) emailField.getScene().getWindow();

        // creo un nuovo stage
        newStage = new Stage();
        newStage.setMinHeight(700);
        newStage.setMinWidth(1000);
        newStage.setTitle("Employee Dashboard");
        newStage.setScene(dashboardScene);

        // chiudo il vecchio e apro il nuovo
        oldStage.close();
        newStage.show();
    }

    public static void logOut(Stage oldStage) {
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