package com.company.Controller;

import com.company.DAO.ProjectDAO;
import com.company.Model.EmpType;
import com.company.Model.Employee;
import com.company.Model.Project;
import com.company.Model.ScientificReferent;
import com.company.PostgresDAO.ProjectDAOImplementation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.jetbrains.annotations.Nullable;

public class ProjectCardController {

    private Project project;
    private Employee employee;
    @FXML public Button takePartButton;
    @FXML public Label lab3Label;
    @FXML public Label lab2Label;
    @FXML public Label lab1Label;
    @FXML public Label referentLabel;
    @FXML public Label managerLabel;
    @FXML public Label endDateLabel;
    @FXML public Label deadlineLabel;
    @FXML public Label startDateLabel;
    @FXML public Label descrLabel;
    @FXML public Label cupLabel;
    @FXML public Label projectName;

    public ProjectCardController(Project project, @Nullable Employee employee) {
        this.project = project;
        this.employee = employee;
    }

    public void initialize() {
        this.cupLabel.setText(project.getCup());
        this.projectName.setText(project.getName());
        this.descrLabel.setText(project.getDescription());
        this.startDateLabel.setText(project.getStartDate().toString());
        this.deadlineLabel.setText(project.getDeadline().toString());

        if(project.getEndDate() == null){
            endDateLabel.setText("Non prevista");
        } else {
            endDateLabel.setText(project.getEndDate().toString());
        }

        if(project.getManager() == null){
            managerLabel.setText("");
        } else {
            managerLabel.setText(project.getManager().getFirstName() + " " + project.getManager().getLastName());
        }

        if(project.getScientificReferent() == null){
            referentLabel.setText("");
        } else {
            referentLabel.setText(project.getScientificReferent().getFirstName() + " " + project.getScientificReferent().getLastName());
        }

        // caricare i laboratori
        lab1Label.setText(
                project.getLaboratories()[0] != null ? project.getLaboratories()[0].getName() : ""
        );
        lab2Label.setText(
                project.getLaboratories()[1] != null ? project.getLaboratories()[1].getName() : ""
        );
        lab3Label.setText(
                project.getLaboratories()[2] != null ? project.getLaboratories()[2].getName() : ""
        );

        // soltanto chi è manager o scientific Referent può vedere il button
        if(!(employee instanceof ScientificReferent)){
            takePartButton.setVisible(false);
        }
    }
    @FXML public void projectTakePart(ActionEvent actionEvent) {

    }
}
