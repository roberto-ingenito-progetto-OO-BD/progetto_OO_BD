package com.company.Controller;

import com.company.GUI.Login;
import com.company.Model.Employee;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
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

    public EmployeeDashboardController(Employee employee) {
        this.employee = employee;
    }

    @FXML
    public void initialize() {
        userNameLabel.setText(employee.getFirstName() + " " + employee.getLastName());
        typeLabel.setText(employee.getType().toString());
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
        Stage stage = (Stage) userNameLabel.getScene().getWindow();
        Login loginGUI = new Login();
        Scene loginScene = loginGUI.getScene();

        stage.close();
        stage.setHeight(586);
        stage.setWidth(436);
        stage.setTitle("Azienda Dashboard");
        stage.setScene(loginScene);
        stage.setResizable(false);
        stage.show();
    }

}