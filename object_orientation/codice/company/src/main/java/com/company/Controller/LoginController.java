package com.company.Controller;

import com.company.Model.EmpType;
import com.company.PostgresDAO.LoginDAOImplementation;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;


public class LoginController {
    @FXML
    public Button loginButton;
    @FXML
    public TextField passwordTextLabel;
    @FXML
    public TextField emailTextLabel;
    @FXML
    public ToggleButton projButton;
    @FXML
    public ToggleButton labButton;

    @FXML
    public void signIn() {
        if (projButton.isSelected()) {
            projectLogin();
        }

        if (labButton.isSelected()) {
            laboratoryLogin();
        }
    }

    @FXML
    public void toggleProjectButton() {
        // se sto portando il bottone a false, allora lo riporto a true
        if (!projButton.isSelected()) {
            projButton.setSelected(true);
        }
        // altrimenti sto portando il bottone a true, quindi disattivo l'altro
        else {
            labButton.setSelected(false);
        }
    }

    @FXML
    public void toggleLaboratoryButton() {
        // se sto portando il bottone a false, allora lo riporto a true
        if (!labButton.isSelected()) {
            labButton.setSelected(true);
        }
        // altrimenti sto portando il bottone a true, quindi disattivo l'altro
        else {
            projButton.setSelected(false);
        }
    }


    private void projectLogin() {
        LoginDAOImplementation login = new LoginDAOImplementation();
        boolean logged = login.projectSalariedLogin(emailTextLabel.getText(), passwordTextLabel.getText());
        System.out.println(logged);
    }

    private void laboratoryLogin() {
        LoginDAOImplementation login = new LoginDAOImplementation();
        EmpType empType = login.baseEmpLogin(emailTextLabel.getText(), passwordTextLabel.getText());
        System.out.println(empType);
    }

}