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
    private @FXML TableColumn<ProjectSalaried, String> hiringRoleColumn;
    private @FXML TextField cfTextField;
    private @FXML Label remainingFunds;

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
        ProjectDAOImplementation projectDAO = new ProjectDAOImplementation();
        selectedProjectName.setText(selectedProjectName.getText() + " " + currentProject.getName());
        float remainingFundsValue = projectDAO.remainingEquipmentFunds(currentProject.getCup());
        System.out.println(remainingFundsValue);
        remainingFunds.setText(String.valueOf(remainingFundsValue));
        hiringFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        hiringLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        hiringRoleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        allProjectSalarieds = projectSalariedDAO.getAllProjectSalaried();
        // filtrare i project salaried togliendo dalla tabella quelli che hanno già un contratto in corso nel progetto selezionato
        currentProject.getContracts().forEach(contract -> {
            if(contract.getExpiration().isAfter(LocalDate.now())) {
                allProjectSalarieds.remove(contract.getProjectSalaried());
            }
        });

        hiringTable.getItems().addAll(allProjectSalarieds);

        // se l'impiegato è già assunto, colorare la riga di rosso
        hiringTable.setRowFactory(projectSalariedTableView -> new TableRow<ProjectSalaried>() {
            @Override
            protected void updateItem(ProjectSalaried projectSalaried, boolean isHired) {
                super.updateItem(projectSalaried, isHired);
                currentProject.getContracts().forEach(contract -> {
                    if(contract.getProjectSalaried() == projectSalaried){
                        if(contract.getExpiration().isAfter(LocalDate.now())){
                            setStyle("-fx-background-color:#A63C06");
                        }
                    }
                });}
        });

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

        try {
            newContract = new Contract(LocalDate.now(), expirationTextField.getValue(), Float.parseFloat(payTextField.getText()));
            newContract.setProjectSalaried(projectSalaried);
            newContract.setProject(currentProject);
            currentProject.addContract(newContract);
            projectSalaried.addContract(newContract);

            projectDAO.hireProjectSalaried(currentProject.getCup(),projectSalaried, newContract, currentEmployee.getType());
            // se l'assunzione nel db è andato a buon fine aggiungere il contratto al progetto attuale
            System.out.println("impiegato assunto");
        } catch (Exception err) {
            throw err;
        }
        // TODO chiudere finestra - controllare che la finestra precedente non si chiuda
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
