package com.company.Controller;

import com.company.GUI.Login;
import com.company.GUI.ProjectCard;
import com.company.Model.Contract;
import com.company.Model.ProjectSalaried;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public class ProjectSalariedDashboardController {
    private final ProjectSalaried employee;

    @FXML
    public TableColumn<Contract, LocalDate> currAssunzione;
    @FXML
    public TableColumn<Contract, LocalDate> currScadenza;
    @FXML
    public TableColumn<Contract, Float> currCompenso;

    @FXML
    public TableColumn<Contract, LocalDate> oldAssunzione;
    @FXML
    public TableColumn<Contract, LocalDate> oldScadenza;
    @FXML
    public TableColumn<Contract, Float> oldCompenso;
    @FXML
    private TableView<Contract> oldContractsTable;
    @FXML
    private TableView<Contract> currentContractsTable;

    @FXML
    private Label userNameLabel;
    @FXML
    private Label roleLabel;

    public ProjectSalariedDashboardController(ProjectSalaried employee) {
        this.employee = employee;
    }

    @FXML
    public void initialize() {
        userNameLabel.setText(employee.getFirstName() + " " + employee.getLastName());
        roleLabel.setText(employee.getRole());

        // creazione tabella progetti in corso
        currAssunzione.setCellValueFactory(new PropertyValueFactory<>("hireDate"));
        currScadenza.setCellValueFactory(new PropertyValueFactory<>("expiration"));
        currCompenso.setCellValueFactory(new PropertyValueFactory<>("pay"));
        ObservableList<Contract> currentContracts = currentContractsTable.getItems();

        oldAssunzione.setCellValueFactory(new PropertyValueFactory<>("hireDate"));
        oldScadenza.setCellValueFactory(new PropertyValueFactory<>("expiration"));
        oldCompenso.setCellValueFactory(new PropertyValueFactory<>("pay"));
        ObservableList<Contract> oldContracts = oldContractsTable.getItems();

        employee.getContracts().forEach(contract -> {
            if (contract.getExpiration().isAfter(LocalDate.now())) {
                // contratti non ancora conclusi
                currentContracts.add(contract);
            } else {
                // aggiungi in progetti conclusi
                oldContracts.add(contract);
            }
        });
        currentContractsTable.setItems(currentContracts);
        oldContractsTable.setItems(oldContracts);
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

    public void getCurrentContractSelectedRow(MouseEvent mouseEvent) {
        displayRow(currentContractsTable);
    }

    public void getOldContractSelectedRow(MouseEvent mouseEvent) {
        displayRow(oldContractsTable);
    }

    public void displayRow(@NotNull TableView table) {
        ProjectCard projectCard;
        Stage newStage;
        Scene newScene;
        Contract currentContract = (Contract) table.getSelectionModel().getSelectedItem();
        if (currentContract == null) {
            System.out.println("no row selected");
        } else {
            System.out.println(currentContract.getProject().getCup());
            table.getSelectionModel().clearSelection();

            // creare nuova schermata con informazioni di project ed aprirla
            projectCard = new ProjectCard();
            newScene = projectCard.getScene(currentContract.getProject(), null);
            newStage = new Stage();
            newStage.setTitle("Project informations");
            newStage.setScene(newScene);
            newStage.setResizable(false);
            newStage.show();
        }
    }

}