package com.company.Controller;

import com.company.Model.Contract;
import com.company.Model.Employee;
import com.company.Model.Project;
import com.company.Model.ProjectSalaried;
import com.company.PostgresDAO.ProjectDAOImplementation;
import com.company.PostgresDAO.ProjectSalariedDAOImplementation;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;

public class HiringScreenController {
    private @FXML Label selectedProjectName;
    private @FXML DatePicker birthDateTextField;
    private @FXML Button deleteTextFieldButton;
    private @FXML Button hireButton;
    private @FXML TextField payTextField;
    private @FXML DatePicker expirationTextField;
    private @FXML TextField roleTextField;
    private @FXML TextField emailTextField;
    private @FXML TextField lastNameTextField;
    private @FXML TextField firstNameTextField;
    private @FXML TableColumn hiringRoleColumn;
    private @FXML TextField cfTextField;

    private @FXML TableColumn<ProjectSalaried, String> hiringLastNameColumn;
    private @FXML TableColumn<ProjectSalaried, String> hiringFirstNameColumn;
    private @FXML TableView<ProjectSalaried> hiringTable;

    private Project currentProject;
    private Employee currentEmployee;
    private ArrayList<ProjectSalaried> allProjectSalarieds;

    public HiringScreenController(Project project, Employee employee) {
        this.currentProject = project;
        this.currentEmployee = employee;
    }

    private @FXML void initialize() {
        ProjectSalariedDAOImplementation projectSalariedDAO = new ProjectSalariedDAOImplementation();

        selectedProjectName.setText(selectedProjectName.getText() + " " + currentProject.getName());
        hiringFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        hiringLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        allProjectSalarieds = projectSalariedDAO.getAllProjectSalaried();
        // filtrare i project salaried togliendo dalla tabella quelli che hanno già un contratto in corso nel progetto selezionato
        currentProject.getContracts().forEach(contract -> {
            if(contract.getExpiration().isAfter(LocalDate.now())) {
                allProjectSalarieds.remove(contract.getProjectSalaried());
            }
        });

        hiringTable.getItems().addAll(allProjectSalarieds);

    }

    private @FXML void hireProjectSalaried() {
        ProjectDAOImplementation projectDAO = new ProjectDAOImplementation();
        ProjectSalaried projectSalaried = null;
        Contract newContract = null;

        // se è stato selezionato un project salaried dalla tabella utilizzare quello per l'assunzione
        if(hiringTable.getSelectionModel().getSelectedItem() != null){
            projectSalaried = hiringTable.getSelectionModel().getSelectedItem();
        } else {
            projectSalaried = new ProjectSalaried(
                    cfTextField.getText(),
                    firstNameTextField.getText(),
                    lastNameTextField.getText(),
                    emailTextField.getText(),
                    roleTextField.getText(),
                    birthDateTextField.getValue()
            );
        }

        newContract = new Contract(LocalDate.now(), expirationTextField.getValue(), Float.parseFloat(payTextField.getText()));
        newContract.setProjectSalaried(projectSalaried);
        newContract.setProject(currentProject);
        currentProject.addContract(newContract);
        projectSalaried.addContract(newContract);

        projectDAO.hireProjectSalaried(currentProject.getCup(),projectSalaried, newContract, currentEmployee.getType());
        // se l'assunzione nel db è andato a buon fine aggiungere il contratto al progetto attuale
        System.out.println("impiegato assunto");
        // chiudere finestra
        Stage currentStage = (Stage) hireButton.getScene().getWindow();
        currentStage.close();
    }
    private @FXML void deleteTextFields() {
        birthDateTextField.cancelEdit();
        payTextField.clear();
        expirationTextField.cancelEdit();
        roleTextField.clear();
        emailTextField.clear();
        lastNameTextField.clear();
        firstNameTextField.clear();
        cfTextField.clear();
    }

}
