package com.company.Controller;

import com.company.GUI.EquipmentRequestCard;
import com.company.Model.*;
import com.company.PostgresDAO.LaboratoryDAOImplementation;
import com.company.PostgresDAO.ProjectDAOImplementation;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Controller della schermata che fornisce informazioni riguardo i laboratori di cui l'impiegato loggato è manager
 */
public class SelectedLaboratoryCardController {
    final @NotNull Laboratory laboratory;
    final @NotNull ArrayList<Project> referentProjects;

    /// FXML OBJECTS
    private @FXML Label laboratoryNameLabel;

    // Tabella degli impiegati che lavorano al laboratorio selezionato
    private @FXML TableView<Employee> employeesTable;

    // Tabella dei progetti a cui partecipa il laboratorio selezionato
    private @FXML TableView<Project> workingProjectsTable;

    // Tabella dei progetti NON conclusi, ai quali partecipano meno di 3 laboratori e
    // a cui NON partecipa il laboratorio selezionato
    private @FXML TableView<Project> projectsTable;

    /// CONSTRUCTOR

    /**
     * @param selectedLaboratory Laboratorio selezionato
     * @param referentProjects   Progetti in cui l'impiegato è referente
     */
    public SelectedLaboratoryCardController(
            @NotNull Laboratory selectedLaboratory,
            @NotNull ArrayList<Project> referentProjects
    ) {
        this.laboratory = selectedLaboratory;
        this.referentProjects = referentProjects;
    }

    /// FXML METHODS

    /**
     * Funzione che viene eseguita dopo il costruttore <br/>
     * Imposta le tabelle e le varie label
     */
    private @FXML void initialize() {
        laboratoryNameLabel.setText(laboratory.getName());

        employeesTable.setPlaceholder(new Label("Nessun impiegato lavora al laboratorio"));
        workingProjectsTable.setPlaceholder(new Label("Il laboratorio non lavora a nessun progetto"));
        projectsTable.setPlaceholder(new Label("Non ci sono progetti a cui puoi lavorare"));

        // imposta le colonne della tabella "employeesTable"
        employeesTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("cf"));
        employeesTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("fullName"));

        // imposta le colonne della tabella "workingProjectsTable"
        workingProjectsTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("cup"));
        workingProjectsTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));

        // imposta le colonne della tabella "projectsTable"
        projectsTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("cup"));
        projectsTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        projectsTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("startDate"));
        projectsTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("deadline"));

        employeesTable.getItems().addAll(laboratory.getEmployees()); // riempie la tabella "employeesTable"
        workingProjectsTable.getItems().addAll(laboratory.getProjects()); // riempie la tabella "workingProjectsTable"
        projectsTable.getItems().addAll(getProjects()); // riempie la tabella "projectsTable"
    }

    /**
     * Funzione che viene eseguita quando viene cliccata una riga
     * della tabella "workingProjectsTable" <br/>
     * Apre un Alert Dialog che consente di scegliere se lasciare il progetto o richiedere attrezzatura al progetto selezionato<br/>
     */
    private @FXML void onWorkingProjectClick() {
        ButtonType leaveProjectButton = new ButtonType("Lascia progetto");
        ButtonType equipmentRequestButton = new ButtonType("Richiedi attrezzatura");

        Alert alert = new Alert(
                Alert.AlertType.NONE,
                "",
                equipmentRequestButton,
                leaveProjectButton
        );
        alert.setTitle("Attenzione");

        // quando viene cliccato il tasto "X" dell'alert, lo chiude
        alert.getDialogPane().getScene().getWindow().setOnCloseRequest(e -> alert.close());

        Project selectedProject = workingProjectsTable.getSelectionModel().getSelectedItem();
        workingProjectsTable.getSelectionModel().clearSelection();

        // se non è stato selezionato nulla, termina la funzione
        if (selectedProject == null) return;

        // avvia il dialog e aspetta il click di uno dei pulsanti
        // oppure aspetta la chiusura
        alert.showAndWait();
        if (alert.getResult() == leaveProjectButton)
            leaveProject(selectedProject);
        else if (alert.getResult() == equipmentRequestButton) {
            showEquipmentRequestCard(selectedProject);
        }
    }


    /**
     * Funzione che viene eseguita quando viene cliccata una riga
     * della tabella "projectsTable", tabella in cui sono mostrati i progetti a cui è possibile partecipare<br/>
     * Apre un Alert Dialog che permette di scegliere se confermare oppure no la partecipazione al progetto
     */
    private @FXML void onProjectClick() {
        ButtonType yesButton = new ButtonType("Si", ButtonBar.ButtonData.YES);
        Alert alert = new Alert(
                Alert.AlertType.NONE,
                "",
                ButtonType.NO,
                yesButton
        );
        alert.setTitle("Attenzione");

        Project selectedProject = projectsTable.getSelectionModel().getSelectedItem();
        projectsTable.getSelectionModel().clearSelection();

        // se non è stato selezionato nulla, termina la funzione
        if (selectedProject == null) return;

        alert.setContentText("Vuoi partecipare al progetto \""
                + selectedProject.getName()
                + "\"?"
        );

        // avvia il dialog e aspetta
        alert.showAndWait();
        if (alert.getResult() == yesButton) {
            joinProject(selectedProject);
        }


    }


    /// METHODS

    /**
     * Esegue la query sul database e aggiorna il model
     *
     * @param selectedProject Progetto al quale il laboratorio vuole partecipare
     */
    private void joinProject(Project selectedProject) {
        // Carico le ulteriori informazioni del progetto selezionato
        ProjectDAOImplementation projectDAO = new ProjectDAOImplementation();
        selectedProject.setEquipmentRequests(
                projectDAO.getEquipmentRequests(selectedProject.getCup())
        );
        selectedProject.setLaboratories(
                projectDAO.getWorkingLaboratories(selectedProject.getCup())
        );
        selectedProject.setManager(
                projectDAO.getProjectManager(selectedProject.getCup())
        );
        selectedProject.setScientificReferent(
                projectDAO.getProjectReferent(selectedProject.getCup())
        );
        selectedProject.setContracts(
                projectDAO.getProjectContracts(selectedProject.getCup())
        );

        LaboratoryDAOImplementation laboratoryDAO = new LaboratoryDAOImplementation();
        laboratoryDAO.joinProject(
                laboratory.getLabCode(),
                selectedProject.getCup(),
                EmpType.senior
        );

        // rimuove il progetto dalla tabella dei progetti partecipabili
        projectsTable.getItems().removeIf(project -> project.getCup().equals(selectedProject.getCup()));


        // aggiunge il progetto al laboratorio
        laboratory.getProjects().add(selectedProject);

        // aggiunge il progetto alla tabella dei progetti a cui partecipa il laboratorio
        workingProjectsTable.getItems().add(selectedProject);

        workingProjectsTable.refresh();
        projectsTable.refresh();

        // cerca nella lista dei progetti, di cui l'impiegato è referente,
        //      il progetto che intende lasciare
        // Se è presente, elimina dalla lista dei suoi laboratori, il laboratorio che lo sta lasciando
        referentProjects.stream()
                .filter(project -> project.getCup().equals(selectedProject.getCup())) // cerco il progetto tramite il CUP
                .findFirst()
                .ifPresent( // Se il progetto è stato trovato
                        project -> project.getLaboratories().add(laboratory)
                );

        // aggiunge il laboratorio, fra i laboratori partecipanti al progetto selezionato
        selectedProject.addLaboratory(laboratory);
    }

    /**
     * Esegue la query sul database e aggiorna il model
     *
     * @param selectedProject Progetto che il laboratorio intende lasciare
     */
    private void leaveProject(Project selectedProject) {
        LaboratoryDAOImplementation laboratoryDAO = new LaboratoryDAOImplementation();
        int result = laboratoryDAO.leaveProject(
                laboratory.getLabCode(),
                selectedProject.getCup(),
                EmpType.senior
        );

        if (result == 0) {
            System.out.println("nessuna tupla aggiornata");
            return;
        }

        // rimuove il progetto dalla tabella dei progetti a cui partecipa
        workingProjectsTable.getItems().removeIf(project -> project.getCup().equals(selectedProject.getCup()));

        // rimuove il progetto dal laboratorio
        laboratory.getProjects().removeIf(project -> project.getCup().equals(selectedProject.getCup()));


        // aggiunge il progetto alla tabella dei progetti partecipabili
        projectsTable.getItems().add(selectedProject);

        projectsTable.refresh();
        workingProjectsTable.refresh();

        // cerca nella lista dei progetti, di cui l'impiegato è referente,
        //      il progetto che intende lasciare
        // Se è presente, elimina dalla lista dei suoi laboratori, il laboratorio che lo sta lasciando
        referentProjects.stream()
                .filter(project -> project.getCup().equals(selectedProject.getCup())) // cerco il progetto tramite il CUP
                .findFirst()
                .ifPresent( // Se il progetto è stato trovato
                        project -> project.getLaboratories().removeIf( // rimuove il laboratorio dai suoi partecipanti
                                lab -> lab.getLabCode() == laboratory.getLabCode()
                        )
                );
    }

    /**
     * @return Restituisce tutti i progetti NON TERMINATI presenti nel database, escludendo i progetti ai quali già lavora
     */
    private ArrayList<Project> getProjects() {
        ProjectDAOImplementation projectDAO = new ProjectDAOImplementation();
        ArrayList<Project> projects = projectDAO.getAvailableProjects();

        // rimuove dall'array "projects", i progetti a cui il laboratorio già partecipa
        for (Project labProject : laboratory.getProjects()) {
            projects.removeIf(
                    dbProject -> dbProject.getCup().equals(labProject.getCup())
            );
        }

        return projects;
    }

    /**
     * Apre una schermata dove sono presenti dei campi che è possibile compilare per effettuare la richiesta di attrezzatura
     *
     * @param selectedProject Progetto selezionato al quale si vuole chiedere attrezzatura
     */
    private void showEquipmentRequestCard(Project selectedProject) {
        EquipmentRequestCard equipmentRequestCard = new EquipmentRequestCard(laboratory, selectedProject);
        Scene newScene = equipmentRequestCard.getScene();

        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.showAndWait();

        EquipmentRequest equipmentRequest = (EquipmentRequest) newStage.getUserData();
        if (equipmentRequest != null)
            referentProjects.stream()
                    .filter(project ->
                            project.getCup().equals(equipmentRequest.getProject().getCup())
                    )
                    .findFirst()
                    .ifPresent(project -> project.getEquipmentRequests().add(equipmentRequest));

    }
}
