package com.company.Controller;

import com.company.GUI.Login;
import com.company.Model.Employee;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class EmployeeDashboardController {
    private final Employee employee;

    @FXML
    private Label userNameLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private TabPane tabPane;

    @FXML
    private Label lablabel;

    public EmployeeDashboardController(Employee employee) {
        this.employee = employee;
    }

    @FXML
    public void initialize() {
        userNameLabel.setText(employee.getFirstName() + " " + employee.getLastName());
        typeLabel.setText(employee.getType().toString());
        lablabel.setText(employee.getLaboratory().getName());
    }

    @FXML
    private void goToProjectTab() {
        tabPane.getSelectionModel().clearAndSelect(0);
    }

    @FXML
    private void goToLaboratoryTab() {
        tabPane.getSelectionModel().clearAndSelect(1);
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

}