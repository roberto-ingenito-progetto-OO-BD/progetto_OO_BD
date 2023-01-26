package com.company.Controller;

import com.company.Model.Employee;
import com.company.Model.Project;
import com.company.Model.ScientificReferent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.jetbrains.annotations.Nullable;

public class ProjectCardController {
    private Project project;
    private Employee employee;

    /// FXML OBJECTS
    private @FXML Button takePartButton;
    private @FXML Label lab3Label;
    private @FXML Label lab2Label;
    private @FXML Label lab1Label;
    private @FXML Label referentLabel;
    private @FXML Label managerLabel;
    private @FXML Label endDateLabel;
    private @FXML Label deadlineLabel;
    private @FXML Label startDateLabel;
    private @FXML Label descriptionLabel;
    private @FXML Label cupLabel;
    private @FXML Label projectName;

    /// CONSTRUCTOR
    public ProjectCardController(Project project, @Nullable Employee employee) {
        this.project = project;
        this.employee = employee;
    }

    /// FXML METHODS
    private @FXML void initialize() {
        // imposta tutte le label
        cupLabel.setText(project.getCup());
        projectName.setText(project.getName());
        descriptionLabel.setText(project.getDescription());
        startDateLabel.setText(project.getStartDate().toString());

        deadlineLabel.setText(project.getDeadline() == null ? "Non prevista" : project.getEndDate().toString());
        endDateLabel.setText(project.getEndDate() == null ? "Non prevista" : project.getEndDate().toString());
        managerLabel.setText(project.getManager() == null ? "" : project.getManager().getFirstAndLastName());
        referentLabel.setText(project.getScientificReferent() == null ? "" : project.getScientificReferent().getFirstAndLastName());

        // label dei 3 laboratori
        lab1Label.setText(project.getLaboratories()[0] != null ? project.getLaboratories()[0].getName() : "");
        lab2Label.setText(project.getLaboratories()[1] != null ? project.getLaboratories()[1].getName() : "");
        lab3Label.setText(project.getLaboratories()[2] != null ? project.getLaboratories()[2].getName() : "");

        // TODO: fare attenzione a questo if, probabilmente bisogna verificare che sia o instanceof Manager o instanceof Senior
        // soltanto chi è manager o scientific Referent può vedere il button
        if (!(employee instanceof ScientificReferent)) {
            takePartButton.setVisible(false);
        }
    }

    // TODO: implementare funzione
    private @FXML void projectTakePart(ActionEvent actionEvent) {
    }
}
