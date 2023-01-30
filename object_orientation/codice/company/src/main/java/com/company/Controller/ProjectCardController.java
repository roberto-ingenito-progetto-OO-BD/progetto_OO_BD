package com.company.Controller;

import com.company.Model.*;
import com.company.PostgresDAO.ProjectDAOImplementation;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;

@SuppressWarnings("rawtypes")
public class ProjectCardController {
    private final Project project;
    private final @Nullable Employee employee;
    private final @NotNull TableView sourceTable;

    /// FXML OBJECTS
    private @FXML Button takePartButton;
    private @FXML Button endProjectButton;

    private @FXML Label lab1Label;
    private @FXML Label lab2Label;
    private @FXML Label lab3Label;

    private @FXML Label referentLabel;
    private @FXML Label managerLabel;

    private @FXML Label cupLabel;
    private @FXML Label projectNameLabel;
    private @FXML Label descriptionLabel;
    private @FXML Label startDateLabel;
    private @FXML Label endDateLabel;
    private @FXML Label deadlineLabel;

    /// CONSTRUCTOR
    public ProjectCardController(
            Project project,
            @Nullable Employee employee,
            @NotNull TableView tableView
    ) {
        this.project = project;
        this.employee = employee;
        this.sourceTable = tableView;
    }

    /// FXML METHODS
    private @FXML void initialize() {
        // imposta tutte le label
        cupLabel.setText(project.getCup());
        projectNameLabel.setText(project.getName());
        descriptionLabel.setText(project.getDescription());

        startDateLabel.setText(project.getStartDate().toString());
        endDateLabel.setText(project.getEndDate() == null ? "Non prevista" : project.getEndDate().toString());
        deadlineLabel.setText(project.getDeadline() == null ? "Non prevista" : project.getDeadline().toString());

        referentLabel.setText(project.getScientificReferent() == null ? "" : project.getScientificReferent().getFullName());
        managerLabel.setText(project.getManager() == null ? "" : project.getManager().getFullName());

        // label dei 3 laboratori
        switch (project.getLaboratories().size() - 1) {
            case 2:
                lab3Label.setText(project.getLaboratories().get(2).getName());
            case 1:
                lab2Label.setText(project.getLaboratories().get(1).getName());
            case 0:
                lab1Label.setText(project.getLaboratories().get(0).getName());
        }

        // soltanto chi è scientific Manager (Senior) può vedere il butto
        if (employee != null) {
            switch (employee.getType()) {
                case junior, middle:
                    break;
                case senior:
                    // TODO: cambiare nome della tabella
                    if (sourceTable.getId().equals("NOME TABELLA DEI PROGETTI IN CUI IL SENIOR È MANAGER"))
                        takePartButton.setVisible(true);
                    break;
                case manager:
                    // il pulsante per terminare un progetto può essere visibile solo se
                    // la card è stata aperta dalla tabella "projectsTable"
                    if (sourceTable.getId().equals("projectsTable")) {
                        // soltanto chi è Manager può vedere il button
                        endProjectButton.setVisible(true);

                        // se la data di fine non è null, vuol dire che il progetto è concluso
                        if (project.getEndDate() != null) {
                            endProjectButton.setDisable(true);
                            endProjectButton.setText("Già concluso");
                        }
                    }
                    break;
            }
        }


    }

    // TODO: implementare funzione
    //  serve il laboratorio
    private @FXML void projectTakePart() {

    }


    /**
     * Il pulsante è visibile solo ad un impiegato Manager
     */
    private @FXML void endProject() {
        ProjectDAOImplementation projectDAO = new ProjectDAOImplementation();

        projectDAO.endProject(project.getCup(), EmpType.manager);

        // aggiorna il model
        project.setEndDate(LocalDate.now());


        // eliminiamo il riferimento al progetto, ai laboratori che vi stavano partecipato ma non il viceversa
        // quindi di un progetto concluso possiamo vedere gli ultimi 3 laboratori che ci hanno lavorato
        if (!project.getLaboratories().isEmpty()) {
            project.getLaboratories().forEach(laboratory ->
                    laboratory.getProjects().remove(project)
            );
        }

        // chiudere la schermata
        Stage currentStage = (Stage) endProjectButton.getScene().getWindow();

        currentStage.close();

        sourceTable.refresh();
    }
}
