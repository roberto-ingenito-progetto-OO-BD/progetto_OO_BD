package com.company.Controller;

import com.company.GUI.EmployeeDashboard;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EmployeeDashboardController {
    @FXML
    private Label userNameLabel;

    public String email;

    @FXML
    public void initialize() {
        userNameLabel.setText(EmployeeDashboard.empType.toString() + " " + email);
    }


}