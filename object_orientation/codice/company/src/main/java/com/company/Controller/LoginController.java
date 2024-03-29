package com.company.Controller;

import com.company.GUI.EmployeeDashboard;
import com.company.GUI.Login;
import com.company.GUI.ProjectSalariedDashboard;
import com.company.Model.*;
import com.company.PostgresDAO.*;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import java.util.ArrayList;


/**
 * Controller della schermata di Login
 */
public class LoginController {
    /// FXML Objects
    private @FXML TextField passwordField;
    private @FXML TextField emailField;

    private @FXML ToggleButton projectButton;
    private @FXML ToggleButton laboratoryButton;

    private @FXML Label incorrectCredentialsLabel;

    /// FXML METHODS

    /**
     * Funzione che viene eseguita quando viene cliccato il pulsante "Login". <br/>
     * In base al bottone selezionato tra "Laboratorio" e "Progetto", chiama la funzione apposita per loggare
     * nell'uno o nell'altro
     */
    private @FXML void signIn() {
        if (projectButton.isSelected()) {
            projectLogin();
        } else if (laboratoryButton.isSelected()) {
            laboratoryLogin();
        }
    }

    /**
     * Funzione che viene attivata quando viene cliccato il pulsante "Progetto" <br/>
     * Cambia il colore del pulsante "Laboratorio" cosi da risultare deselezionato
     */
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

    /**
     * Funzione che viene attivata quando viene cliccato il pulsante "Laboratorio" <br/>
     * Cambia il colore del pulsante "Progetto" cosi da risultare deselezionato
     */
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

    /**
     * Funzione che viene eseguita quando è selezionato il pulsante "Progetti" come metodo di login <br/>
     * Effettua il controllo delle credenziali presenti nel database <br/>
     * Carica le informazioni dell'impiegato loggato e mostra la finestra della dashboard per gli impiegati salariati a progetto
     */
    private void projectLogin() {
        ProjectSalariedDAOImplementation projectSalariedDAO;
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

            fillProjectsData(new ArrayList<>(contracts.stream().map(contract -> contract.getProject()).toList()));

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

    /**
     * Funzione che viene eseguita quando è selezionato il pulsante "Laboratorio" come metodo di login <br/>
     * Effettua il controllo delle credenziali presenti nel database <br/>
     * Carica tutte le informazioni dell'impiegato loggato e in base al tipo, carica ulteriori informazioni <br/>
     * Se è Senior, carica i progetti in cui è referente e carica i laboratori in cui è manager <br/>
     * Se è Manager, carica i progetti in cui è supervisore <br/>
     *
     * Infine mostra la finestra della dashboard per gli impiegati stipendiati
     */
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

        // se l'impiegato loggato è un senior,
        // carica i progetti in cui è referente
        // carica i laboratori in cui è manager
        //
        // altrimenti se l'impiegato loggato è un manager
        // carica i progetti in cui è manager
        if (loggedEmployee instanceof Senior) {
            SeniorDAOImplementation seniorDAO = new SeniorDAOImplementation();

            ((Senior) loggedEmployee).setProjects(
                    seniorDAO.isReferentProjects(loggedEmployee.getCf(), EmpType.senior)
            );

            ((Senior) loggedEmployee).setLaboratories(
                    seniorDAO.isManagerLaboratory(loggedEmployee.getCf(), EmpType.senior)
            );

            ((Senior) loggedEmployee).getLaboratories().forEach(
                    laboratory -> {
                        laboratory.setProjects(
                                laboratoryDAO.getProjects(laboratory, EmpType.senior)
                        );

                        laboratory.setEmployees(
                                laboratoryDAO.getEmployees(laboratory, EmpType.senior)
                        );
                    }
            );

        } else if (loggedEmployee instanceof Manager) {
            ManagerDAOImplements managerDAO = new ManagerDAOImplements();
            ((Manager) loggedEmployee).setProjects(
                    managerDAO.managerWorkingProjects(loggedEmployee.getCf(), EmpType.manager)
            );
        }

        // controllo se non è null dato che l'impiegato potrebbe non lavorare a nessun laboratorio
        if (empWorkingLaboratory != null) {
            // imposta il manager del laboratorio
            empWorkingLaboratory.setScientificManager(laboratoryDAO.getScientificManager(empWorkingLaboratory, loggedEmpType));

            // imposta il laboratorio in cui lavora l'impiegato
            loggedEmployee.setLaboratory(empWorkingLaboratory);

            // imposta l'attrezzatura del laboratorio
            empWorkingLaboratory.setEquipment(laboratoryDAO.getEquipment(empWorkingLaboratory, loggedEmpType));

            // imposta i proggetti a cui lavora quel laboratorio
            laboratoryWorkingProjects = laboratoryDAO.getProjects(empWorkingLaboratory, loggedEmpType);

            fillProjectsData(laboratoryWorkingProjects);

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


    /**
     * @param projects Per ogni progetto, imposta il referente, il manager e i laboratori che vi partecipano. <br/>
     *                 Serviranno nella schermata di visualizzazione progetto
     */
    private void fillProjectsData(ArrayList<Project> projects) {
        ProjectDAOImplementation projectDAO = new ProjectDAOImplementation();
        projects.forEach(project -> {
            project.setLaboratories(
                    projectDAO.getWorkingLaboratories(project.getCup())
            );
            project.setManager(
                    projectDAO.getProjectManager(project.getCup())
            );
            project.setScientificReferent(
                    projectDAO.getProjectReferent(project.getCup())
            );
        });
    }

    /// STATIC METHODS

    /**
     * Al click del pulsante "LogOut" in basso a sinistra della dashboard, viene chiuso lo stage corrente e aperto
     * un nuovo stage nella schermata login
     *
     * @param oldStage Stage che verrà chiuso
     */
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