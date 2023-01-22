package com.company.Controller;

import com.company.Model.Employee;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EmployeeDashboardController {
    private final Employee employee;

    @FXML
    private Label userNameLabel;

    @FXML
    private Label typeLabel;

    public EmployeeDashboardController(Employee employee) {
        this.employee = employee;
    }

    @FXML
    public void initialize() {
        userNameLabel.setText(employee.getFirstName() + " " + employee.getLastName());
        typeLabel.setText(employee.getType().toString());
    }
}