package com.company.Controller;

import com.company.GUI.EmployeeDashboard;
import com.company.Model.Employee;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EmployeeDashboardController {
    @FXML
    private Label userNameLabel;
    @FXML
    private Label employeeTypeLabel;

    public Employee currentEmployee;


    public EmployeeDashboardController(Employee currentEmployee) {
        System.out.println("controller constructor");
        this.currentEmployee = currentEmployee;
    }

    @FXML
    public void initialize() {
        userNameLabel.setText(currentEmployee.getFirstName() + " " + currentEmployee.getLastName());
        employeeTypeLabel.setText(currentEmployee.getType().toString());
    }


}