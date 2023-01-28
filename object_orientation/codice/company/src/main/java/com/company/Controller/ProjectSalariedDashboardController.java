package com.company.Controller;

import com.company.GUI.ProjectCard;
import com.company.Model.Contract;
import com.company.Model.ProjectSalaried;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public class ProjectSalariedDashboardController {
    private final ProjectSalaried employee;

    /// FXML OBJECTS
    private @FXML TableView<Contract> oldContractsTable;
    private @FXML TableColumn<Contract, LocalDate> currAssunzione;
    private @FXML TableColumn<Contract, LocalDate> currScadenza;
    private @FXML TableColumn<Contract, Float> currCompenso;

    private @FXML TableView<Contract> currentContractsTable;
    private @FXML TableColumn<Contract, LocalDate> oldAssunzione;
    private @FXML TableColumn<Contract, LocalDate> oldScadenza;
    private @FXML TableColumn<Contract, Float> oldCompenso;

    private @FXML Label userNameLabel;
    private @FXML Label roleLabel;

    /// CONSTRUCTOR
    public ProjectSalariedDashboardController(ProjectSalaried employee) {
        this.employee = employee;
    }

    /// FXML METHODS
    @FXML
    public void initialize() {
        userNameLabel.setText(employee.getFullName());
        roleLabel.setText(employee.getRole());

        // creazione tabella progetti in corso
        currAssunzione.setCellValueFactory(new PropertyValueFactory<>("hireDate"));
        currScadenza.setCellValueFactory(new PropertyValueFactory<>("expiration"));
        currCompenso.setCellValueFactory(new PropertyValueFactory<>("pay"));

        oldAssunzione.setCellValueFactory(new PropertyValueFactory<>("hireDate"));
        oldScadenza.setCellValueFactory(new PropertyValueFactory<>("expiration"));
        oldCompenso.setCellValueFactory(new PropertyValueFactory<>("pay"));

        employee.getContracts().forEach(contract -> {
            if (contract.getExpiration() != null) {
                if (contract.getExpiration().isAfter(LocalDate.now())) {
                    // contratti non ancora conclusi
                    currentContractsTable.getItems().add(contract);
                } else {
                    // aggiungi in progetti conclusi
                    oldContractsTable.getItems().add(contract);
                }
            }
        });
    }

    @FXML
    private void onLogOut() {
        Stage oldStage = (Stage) userNameLabel.getScene().getWindow();
        LoginController.logOut(oldStage);
    }

    private @FXML void getCurrentContractSelectedRow() {
        displayRow(currentContractsTable);
    }

    private @FXML void getOldContractSelectedRow() {
        displayRow(oldContractsTable);
    }

    /// METHODS
    public void displayRow(@NotNull TableView<Contract> table) {
        ProjectCard projectCard;

        Stage newStage;
        Scene newScene;

        Contract currentContract = table.getSelectionModel().getSelectedItem();

        if (currentContract != null) {
            table.getSelectionModel().clearSelection(); // deseleziona l'elemento cliccato

            // crea una nuova schermata con le informazioni del progetto riferito al contratto selezionato
            projectCard = new ProjectCard();
            newScene = projectCard.getScene(currentContract.getProject(), null, table);

            newStage = new Stage();
            newStage.setTitle("Project informations");
            newStage.setScene(newScene);
            newStage.setResizable(false);

            // imposta la nuova finistra come "modal",
            // quindi blocca tutti gli eventi delle altre finestre finché questa non viene chiusa
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.show();
        }
    }

}