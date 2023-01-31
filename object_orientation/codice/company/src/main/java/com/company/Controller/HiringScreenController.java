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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;

public class HiringScreenController {
    private final Project currentProject;
    private final Employee currentEmployee;
    private ArrayList<ProjectSalaried> allProjectSalaried;

    /// FXML OBJECTS
    private @FXML Label selectedProjectName;
    private @FXML Label remainingFunds;

    private @FXML Button deleteTextFieldButton;
    private @FXML Button hireButton;

    private @FXML DatePicker expirationTextField;
    private @FXML DatePicker birthDateTextField;

    private @FXML TextField payTextField;
    private @FXML TextField roleTextField;
    private @FXML TextField emailTextField;
    private @FXML TextField lastNameTextField;
    private @FXML TextField firstNameTextField;
    private @FXML TextField cfTextField;

    private @FXML TableView<ProjectSalaried> hiringTable;
    private @FXML TableColumn<ProjectSalaried, String> hiringRoleColumn;
    private @FXML TableColumn<ProjectSalaried, String> hiringLastNameColumn;
    private @FXML TableColumn<ProjectSalaried, String> hiringFirstNameColumn;

    /// CONSTRUCTOR
    public HiringScreenController(Project project, Employee employee) {
        this.currentProject = project;
        this.currentEmployee = employee;
    }

    /// FXML METHODS
    private @FXML void initialize() {
        ProjectSalariedDAOImplementation projectSalariedDAO = new ProjectSalariedDAOImplementation();
        ProjectDAOImplementation projectDAO = new ProjectDAOImplementation();
        BigDecimal remainingFundsValue = projectDAO.remainingProjectSalariedFunds(currentProject.getCup());

        selectedProjectName.setText(currentProject.getName());
        remainingFunds.setText("€" + remainingFundsValue.setScale(2, RoundingMode.UNNECESSARY));

        // imposto le colonne di "hiringTable"
        hiringFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        hiringLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        hiringRoleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        allProjectSalaried = projectSalariedDAO.getAllProjectSalaried();

        // filtra i project salaried togliendo dalla tabella quelli che hanno già un contratto in corso nel progetto selezionato
        currentProject.getContracts().forEach(contract -> {
            if (contract.getExpiration() != null && contract.getExpiration().isAfter(LocalDate.now())) {
                allProjectSalaried.remove(contract.getProjectSalaried());
            }
        });

        hiringTable.getItems().addAll(allProjectSalaried);

        // TODO: non funziona bene
        // se l'impiegato è già assunto, colorare la riga di rosso
        hiringTable.setRowFactory(projectSalariedTableView -> new TableRow<>() {
            @Override
            protected void updateItem(ProjectSalaried projectSalaried, boolean isRowEmpty) {
                super.updateItem(projectSalaried, isRowEmpty);

                if (!isRowEmpty) {
                    // Trova tra i contratti del progetto, se c'è quello dell'impiegato all'i-esima riga
                    // se c'è e non è scaduto, colora la riga
                    currentProject
                            .getContracts()
                            .stream()
                            .filter(contract -> contract.getProjectSalaried().getCf().equals(projectSalaried.getCf()))
                            .findFirst()
                            .ifPresent(contract -> {
                                if (contract.getExpiration() != null && contract.getExpiration().isAfter(LocalDate.now())) {
                                    setStyle("-fx-background-color:#a8a8a8");
                                    setDisable(true);
                                }
                            });
                }
            }
        });
        hiringTable.refresh();
    }

    /**
     * Funzione che viene eseguita quando viene cliiccato il pulsante "Assumi"
     */
    private @FXML void hireProjectSalaried() {
        ProjectDAOImplementation projectDAO = new ProjectDAOImplementation();
        ProjectSalaried projectSalaried;
        Contract newContract;

        // se è stato selezionato un project salaried dalla tabella utilizzare quello per l'assunzione
        if (hiringTable.getSelectionModel().getSelectedItem() != null) {
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

            projectDAO.hireProjectSalaried(currentProject.getCup(), projectSalaried, newContract, currentEmployee.getType());

            // se l'assunzione nel db è andato a buon fine aggiungere il contratto al progetto attuale
            System.out.println("impiegato assunto");
        } catch (Exception err) {
            throw new RuntimeException(err);
        }

        // TODO chiudere finestra - controllare che la finestra precedente non si chiuda
        Stage currentStage = (Stage) hireButton.getScene().getWindow();

        currentStage.close();
    }

    /**
     * Funzione che viene eseguita quando viene cliccato il bottone "Cancella"
     * Cancella tutti i valori nei field
     */
    private @FXML void deleteTextFields() {
        hiringTable.getSelectionModel().clearSelection();

        birthDateTextField.setValue(null);
        payTextField.clear();
        expirationTextField.setValue(null);
        roleTextField.clear();
        emailTextField.clear();
        lastNameTextField.clear();
        firstNameTextField.clear();
        cfTextField.clear();
    }
}
