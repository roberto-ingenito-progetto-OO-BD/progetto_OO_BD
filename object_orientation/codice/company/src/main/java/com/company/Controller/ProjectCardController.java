package com.company.Controller;

import com.company.Model.*;
import com.company.PostgresDAO.ProjectDAOImplementation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.time.LocalDate;

public class ProjectCardController {
    private final Project project;
    private final Employee employee;
    private final TableView sourceTable;
    public Button endProjectButton;

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
    public ProjectCardController(Project project, @Nullable Employee employee, @Nullable TableView tableView) {
        this.project = project;
        this.employee = employee;
        this.sourceTable = tableView;
    }

    /// FXML METHODS
    private @FXML void initialize() {
        // imposta tutte le label
        cupLabel.setText(project.getCup());
        projectName.setText(project.getName());
        descriptionLabel.setText(project.getDescription());
        startDateLabel.setText(project.getStartDate().toString());

        deadlineLabel.setText(project.getDeadline() == null ? "Non prevista" : project.getDeadline().toString());
        endDateLabel.setText(project.getEndDate() == null ? "Non prevista" : project.getEndDate().toString());
        managerLabel.setText(project.getManager() == null ? "" : project.getManager().getFullName());
        referentLabel.setText(project.getScientificReferent() == null ? "" : project.getScientificReferent().getFullName());

        // label dei 3 laboratori ?? switch
        switch(project.getLaboratories().size()-1){
            case 2: lab1Label.setText(project.getLaboratories().get(2).getName());
            case 1: lab2Label.setText(project.getLaboratories().get(1).getName());
            case 0: lab3Label.setText(project.getLaboratories().get(0).getName());
        }

        // soltanto chi è scientific Manager (Senior) può vedere il button
        if (!(employee.getType() == EmpType.senior)) {
            takePartButton.setVisible(false);
        }
        // soltanto chi è Manager può vedere il button
        if(!(employee.getType() == EmpType.manager)) {
            endProjectButton.setVisible(false);
        }
        if(project.getEndDate() != null){
            endProjectButton.setDisable(true);
            endProjectButton.setText("Già concluso");
        }
    }

    // TODO: implementare funzione
    //  serve il laboratorio
    private @FXML void projectTakePart() {

    }


    @FXML
    public void endProject(ActionEvent actionEvent) {
        ProjectDAOImplementation projectDAO = new ProjectDAOImplementation();
        System.out.println(employee.getType().toString());
        projectDAO.endProject(project.getCup(), employee.getType());
        // aggiornare il model
        project.setEndDate(LocalDate.now());
        // TODO droppare i laboratori che hanno lavorato al progetto ? ma poi non possiamo più vedere chi ci ha lavorato
        //if(!project.getLaboratories().isEmpty()){
        //   project.getLaboratories().forEach(laboratory -> {
        //       laboratory.getProjects().remove(project);
        //   });
        //   project.getLaboratories().clear();
       //}
        // chiudere la schermata
        Stage currentStage = new Stage();
        currentStage = (Stage) endProjectButton.getScene().getWindow();
        currentStage.close();
        sourceTable.refresh();
    }
}
