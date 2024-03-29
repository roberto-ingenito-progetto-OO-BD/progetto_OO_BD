package com.company.Controller;

import com.company.GUI.EquipmentBuyingScreen;
import com.company.GUI.ProjectCard;
import com.company.GUI.SelectedLaboratoryCard;
import com.company.Model.*;
import com.company.PostgresDAO.ProjectDAOImplementation;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;
import com.company.GUI.HiringScreen;


import java.util.ArrayList;

public class EmployeeDashboardController {
    private final Employee employee;

    /// FXML OBJECTS
    private @FXML TabPane tabPane;

    private @FXML Tab laboratoryTab;
    private @FXML Tab projectsTab;

    private @FXML ListView<String> laboratoryEquipmentListView;

    private @FXML Button hireProjectSalaried;

    private @FXML Label projectsButtonLabel;
    private @FXML Label laboratoryButtonLabel;

    private @FXML Label userNameLabel;
    private @FXML Label empTypeLabel;
    private @FXML Label laboratoryTopicLabel;
    private @FXML Label laboratoryNameLabel;
    private @FXML Label laboratoryManagerLabel;

    private @FXML StackPane noWorkingProjectLabel;

    /**
     * laboratory working projects table
     * tabella dei progetti in cui lavora il laboratorio dove lavora l'impiegato loggato
     */
    private @FXML TableView<Project> labWorkingProjectsTable;

    /**
     * tabella dei laboratori in cui l'impiegato loggato ne è manager
     */
    private @FXML TableView<Laboratory> empManagerLabsTable;

    /**
     * tabella delle richieste di attrezzatura
     */
    private @FXML TableView<EquipmentRequest> equipmentRequestTable;

    /**
     * tabella degli impiegati assunti
     */
    private @FXML TableView<ProjectSalaried> hiredProjectSalariedTable;

    /**
     * tabella dei progetti di cui un impiegato (manager) è manager
     */
    private @FXML TableView<Project> projectsTable;

    /// CONSTRUCTOR
    public EmployeeDashboardController(Employee employee) {
        this.employee = employee;
    }

    /// FXML METHODS


    /**
     * Funzione che si avvia subito dopo il costruttore <br/>
     * Carica le schermate in base al tipo dell'impiegato loggato
     */
    private @FXML void initialize() {
        // imposta nome, cognome e tipo dell'impiegato nella barra in alto
        userNameLabel.setText(employee.getFirstName() + " " + employee.getLastName());
        empTypeLabel.setText(employee.getType().toString());

        setPlaceholders();

        loadProjectTab();

        loadLaboratoryTab();

        // imposta quali elementi possono essere visualizzati in base al tipo dell'impiegato loggato
        switch (employee.getType()) {
            case junior, middle:
                projectsButtonLabel.setVisible(false); // rendo non visibile il pulsante "Proggetti"
                break;
        }

        // impedisce al tabPane di cambiare pagina con la shortcut CTRL+TAB da tastiera
        tabPane.addEventFilter(KeyEvent.KEY_PRESSED, event -> event.consume());
    }

    private @FXML void goToProjectTab() {
        changeTab(projectsTab);
    }

    private @FXML void goToLaboratoryTab() {
        changeTab(laboratoryTab);
    }

    private @FXML void onLogOutClick() {
        Stage oldStage = (Stage) userNameLabel.getScene().getWindow();
        LoginController.logOut(oldStage);
    }

    // TAB LABORATORIO

    /**
     * Funzione che viene eseguita quando viene cliccata una riga
     * della tabella "labWorkingProjectTable" <br/>
     * Apre una card dove mostra le informazioni del laboratorio selezionato
     */
    private @FXML void onLabWorkingProjectTableRowClick() {
        ProjectCard projectCard;
        Stage newStage;
        Scene newScene;

        // prende il progetto della riga selezionata
        @Nullable Project selectedProject = labWorkingProjectsTable.getSelectionModel().getSelectedItem();

        // toglie la selezione dall'elementi cliccato, altrimenti rimane selezionato
        labWorkingProjectsTable.getSelectionModel().clearSelection();

        if (selectedProject != null) {
            // crea una nuova schermata con le informazioni del progetto riferito al contratto selezionato
            projectCard = new ProjectCard();

            newScene = projectCard.getScene(selectedProject, employee, labWorkingProjectsTable);

            newStage = new Stage();
            newStage.setTitle("Project informations");
            newStage.setScene(newScene);
            newStage.setResizable(false);

            // imposta la nuova finistra come "modal",
            // quindi blocca tutti gli eventi delle altre finestre finché questa non viene chiusa
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.show();
        }
    }

    /**
     * Funzione che viene eseguita quando viene cliccata una riga
     * della tabella "empManagerLabsTable" <br/>
     * Apre una nuova finestra dove mostra delle informazioni riguardo al laboratorio selezionato
     */
    private @FXML void onLabClick() {
        // prende il laboratorio della riga selezionata
        @Nullable Laboratory selectedLaboratory = empManagerLabsTable.getSelectionModel().getSelectedItem();

        // toglie la selezione dall'elementi cliccato, altrimenti rimane selezionato
        empManagerLabsTable.getSelectionModel().clearSelection();

        if (selectedLaboratory != null) {

            // se l'impiegato lavora ad un laboratorio e questo è il laboratorio che sta andando a modificare
            // allora imposta i progetti del laboratorio selezionato "selectedLaboratory" uguale al riferimento
            //      dei progetti del laboratorio in cui lavora l'impiegato
            // In questo modo si andranno a modificare i progetti direttamente nella lista dei progetti del laboratorio dell'impiegato
            //      per poi andare ad aggiornare la tabella "labWorkingProjectsTable"
            if (employee.getLaboratory() != null && employee.getLaboratory().getLabCode() == selectedLaboratory.getLabCode()) {
                selectedLaboratory.setProjects(employee.getLaboratory().getProjects());
            }

            showSelectedLaboratory(selectedLaboratory);
        }
    }

    // TAB PROGETTI

    /**
     * Funzione che è eseguita quando viene cliccata una riga della tabella "projectsTable", tabella in cui
     * sono mostrati i progetti in cui l'impiegato loggato è referente o manager. <br/>
     * Con click SINISTRO, vengono aggiornate le tabelle "equipmentRequestTable" e "hiredProjectSalariedTable", in modo
     * tale da mostrare le richieste aperte e gli impiegati assunti del progetto selezionato. <br/>
     * Con click DESTRO, oltre a fare le cose che sono fatte con click sinistro, viene mostrata anche la
     * card per mostrare le informazioni del progetto selezionato.
     */
    private @FXML void showSelectedProject(MouseEvent mouseEvent) {
        ProjectCard projectCard;
        Stage newStage;
        Scene newScene;

        Project selectedProject = projectsTable.getSelectionModel().getSelectedItem();

        if (selectedProject != null) {
            // se le tabelle sono riempite , svuotarle
            if (!equipmentRequestTable.getItems().isEmpty()) equipmentRequestTable.getItems().clear();
            if (!hiredProjectSalariedTable.getItems().isEmpty()) hiredProjectSalariedTable.getItems().clear();

            // se il progetto È concluso, il pulsante per assumere un impiegato viene disabilitato
            // viene abilitato se NON È concluso

            // carica tutti gli equipment request del progetto selezionato nella tabella di equipment
            // riempie la tabella delle richieste
            equipmentRequestTable.getItems().addAll(selectedProject.getEquipmentRequests());

            // carica tutti i project salaried del progetto selezionato nella tabella degli impiegati
            // riempire lista e caricare la tabella
            selectedProject.getContracts().forEach(
                    contract -> hiredProjectSalariedTable.getItems().add(contract.getProjectSalaried())
            );

            // Con click destro, apre la card con le informazioni del progetto selezionato
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                projectCard = new ProjectCard();
                newScene = projectCard.getScene(selectedProject, employee, projectsTable);

                newStage = new Stage();
                newStage.setTitle("Project Informations");
                newStage.setScene(newScene);
                newStage.setResizable(false);

                // apre lo stage come un modal, cosi che siano bloccato gli eventi dello
                // stage precedente che si trova al di sotto
                newStage.initModality(Modality.APPLICATION_MODAL);
                newStage.showAndWait();
            }
            equipmentRequestTable.setDisable(selectedProject.getEndDate() != null);
            hireProjectSalaried.setDisable(selectedProject.getEndDate() != null);
        }
    }

    /**
     * Mostra la finestra per assumere un nuovo impiegato
     */
    private @FXML void showHiringScreen() {
        Project selectedProject = projectsTable.getSelectionModel().getSelectedItem();
        HiringScreen hiringScreen = new HiringScreen();

        Scene scene = hiringScreen.getScene(selectedProject, employee);
        Stage currentStage = (Stage) labWorkingProjectsTable.getScene().getWindow();
        Stage newStage = new Stage();

        newStage.setTitle("Hiring Screen");
        newStage.setScene(scene);
        newStage.setResizable(false);

        // nasconde la finestra attuale
        // apre la nuova finestra e aspetta che questa venga chiusa
        // una volta chiusa, apre nuovamente la finestra attuale
        currentStage.hide();
        newStage.showAndWait();

        // aggiorna la tabella delle richieste una volta che la pagina di buy equipment viene chiusa
        projectsTable.getSelectionModel().clearSelection();
        equipmentRequestTable.getItems().clear();
        hiredProjectSalariedTable.getItems().clear();
        currentStage.show();
    }

    /**
     * Funzione che viene eseguita quando viene cliccata una riga nella tabella "equipmentRequestTable",
     * tabella che mostra le richieste di attrezzatura del progetto selezionato nella tabella "projectsTable" <br/>
     * Apre la finestra per l'acquisto dell'attrezzatura selezionata
     */
    private @FXML void getSelectedEquipment() {
        Project selectedProject = projectsTable.getSelectionModel().getSelectedItem();
        EquipmentRequest selectedEquipmentRequest = equipmentRequestTable.getSelectionModel().getSelectedItem();

        if (selectedProject != null) {
            EquipmentBuyingScreen equipmentBuyingScreen = new EquipmentBuyingScreen();

            Stage currentStage = (Stage) projectsTable.getScene().getWindow();
            Stage newStage = new Stage();
            Scene newScene = equipmentBuyingScreen.getScene(selectedProject, selectedEquipmentRequest);

            newStage.setTitle("Equipment Buying Screen");
            newStage.setScene(newScene);
            newStage.setResizable(false);

            // nasconde la finestra attuale
            // apre la nuova finestra e aspetta che questa venga chiusa
            // una volta chiusa, esegue il restante codice
            currentStage.hide();
            newStage.showAndWait();


            if(selectedEquipmentRequest == null){
                projectsTable.getSelectionModel().clearSelection();
                currentStage.show();
                return;
            }

            // aggiorno nel model la list view "laboratoryEquipmentListView"
            // dato che il senior che acquista l'attrezzatura per il laboratorio X, ci potrebbe lavorare, dovrà quindi
            // visualizzare la nuova attrezzatura nella tab "Laboratorio"
            if (employee.getLaboratory() != null && employee.getLaboratory().getLabCode() == selectedEquipmentRequest.getLaboratory().getLabCode()) {
                laboratoryEquipmentListView.getItems().clear();
                for (Equipment equipment : selectedProject.getEquipments()) {
                    laboratoryEquipmentListView.getItems().add(
                            equipment.getName() + ", " + equipment.getType() + ", " + equipment.getTechSpecs()
                    );
                }
                laboratoryEquipmentListView.refresh();
            }

            // aggiorna la tabella delle richieste una volta che la pagina di buy equipment viene chiusa
            equipmentRequestTable.getItems().clear();
            hiredProjectSalariedTable.getItems().clear();
            projectsTable.getSelectionModel().clearSelection();
            currentStage.show();
        }
    }

    /// METHODS

    /**
     * Aggiorna le opacità dei testi dei pulsanti per cambiare tab, cosi da far capire quale Tab è selezionata <br/>
     * Successivamente cambia la Tab con quella passata in input
     *
     * @param tab Riferimento alla Tab cliccata
     */
    private void changeTab(Tab tab) {
        projectsButtonLabel.setOpacity(0.4);
        laboratoryButtonLabel.setOpacity(0.4);

        if (tab == projectsTab) projectsButtonLabel.setOpacity(1);

        if (tab == laboratoryTab) laboratoryButtonLabel.setOpacity(1);

        tabPane.getSelectionModel().select(tab);
    }

    /**
     * Apre una nuova finestra dove mostra delle informazioni riguardo al laboratorio passato in input
     */
    private void showSelectedLaboratory(Laboratory laboratory) {
        SelectedLaboratoryCard selectedLaboratoryCard = new SelectedLaboratoryCard();
        Scene scene = selectedLaboratoryCard.getScene(laboratory, ((Senior) employee).getProjects());

        Stage currentStage = (Stage) tabPane.getScene().getWindow();
        Stage newStage = new Stage();

        newStage.setTitle("Laboratory informations");
        newStage.setScene(scene);
        newStage.setResizable(false);

        newStage.show();

        // nasconde la finestra corrente
        currentStage.hide();

        // quando si chiude la nuova finestra, verrà ri-mostrata quella corrente
        // e verrà aggiornata la tabella "labWorkingProjectsTable",
        //      dato che se l'impiegato lavora in quel laboratorio e ne è anche manager, se prende parte ad un progetto
        //      oppure ne lascia uno, allora dovrà essere aggiornata anche la tabella che mostra i progetti a cui lavora quel laboratorio
        newStage.setOnCloseRequest(event -> {
            currentStage.show();

            // se il laboratorio aggiornato è lo stesso in cui l'impiegato lavora
            // allora aggiorna la tabella "labWorkingProjectsTable" in cui sono mostrati i progetti ai quali partecipa
            //   il laboratorio dove lavora l'impiegato
            if (employee.getLaboratory() != null && employee.getLaboratory().getLabCode() == laboratory.getLabCode()) {
                labWorkingProjectsTable.getItems().clear();
                labWorkingProjectsTable.getItems().addAll(laboratory.getProjects());
            }
            labWorkingProjectsTable.refresh();

            projectsTable.refresh();
        });
    }

    /**
     * Imposta i placeholder delle TableView e delle ListView presenti nella GUI
     */
    private void setPlaceholders() {
        laboratoryEquipmentListView.setPlaceholder(new Label("Non c'è attrezzatura in questo laboratorio"));

        labWorkingProjectsTable.setPlaceholder(new Label("Il laboratorio non lavora a nessun progetto"));
        equipmentRequestTable.setPlaceholder(new Label("Nessuna richiesta pendente"));
        hiredProjectSalariedTable.setPlaceholder(new Label("Nessun impiegato assunto"));
        empManagerLabsTable.setPlaceholder(new Label("Non ci sono laboratori in cui sei manager"));
    }

    /**
     * Carica ed imposta tutti gli elementi presenti nella tab "Laboratorio"
     */
    private void loadLaboratoryTab() {
        // l'impiegato lavora in un laboratorio
        if (employee.getLaboratory() != null) {
            // imposta (nelle apposite label) il nome, il topic e il manager
            // del laboratorio in cui lavora l'impiegato loggato
            laboratoryNameLabel.setText(employee.getLaboratory().getName());
            laboratoryTopicLabel.setText(employee.getLaboratory().getTopic());
            laboratoryManagerLabel.setText(employee.getLaboratory().getScientificManager().getFullName());

            // imposta il tipo delle colonne della tabella "labWorkingProjectsTableView"
            // poi riempie la tabella "labWorkingProjectsTableView"
            labWorkingProjectsTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("cup"));
            labWorkingProjectsTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
            labWorkingProjectsTable.getItems().addAll(
                    employee.getLaboratory().getProjects()
            );

            // riempie la ListView "equipmentListView"
            for (Equipment equipment : employee.getLaboratory().getEquipments()) {
                laboratoryEquipmentListView.getItems().add(
                        equipment.getName() + ", " + equipment.getType() + ", " + equipment.getTechSpecs()
                );
            }

            empManagerLabsTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("labCode"));
            empManagerLabsTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
            if (employee instanceof Senior) {
                empManagerLabsTable.getItems().addAll(
                        ((Senior) employee).getLaboratories()
                );
            }
        } else {
            // rendo non visibile l'intera tab
            // il doppio getParent arriva al padre di tutti gli elementi nella tab
            labWorkingProjectsTable.getParent().getParent().setVisible(false);

            // rendo visibile la label "noWorkingProjectLabel" per fornire il messaggio
            noWorkingProjectLabel.setVisible(true);
        }
    }

    /**
     * Carica ed imposta tutti gli elementi presenti nella tab "Progetti"
     */
    private void loadProjectTab() {
        ProjectDAOImplementation projectDAO = new ProjectDAOImplementation();
        ArrayList<Project> employeeProjects = new ArrayList<>();

        if (employee instanceof Senior)
            employeeProjects.addAll(((Senior) employee).getProjects());
        else if (employee instanceof Manager)
            employeeProjects.addAll(((Manager) employee).getProjects());

        switch (employee.getType()) {
            case senior, manager:
                // per ogni progetto caricare le informazioni aggiuntive
                employeeProjects.forEach(project -> {
                    project.setEquipmentRequests(
                            projectDAO.getEquipmentRequests(project.getCup())
                    );
                    project.setLaboratories(
                            projectDAO.getWorkingLaboratories(project.getCup())
                    );
                    project.setManager(
                            projectDAO.getProjectManager(project.getCup())
                    );
                    project.setScientificReferent(
                            projectDAO.getProjectReferent(project.getCup())
                    );
                    project.setContracts(
                            projectDAO.getProjectContracts(project.getCup())
                    );
                    project.getLaboratories().forEach(laboratory ->
                            projectDAO.getBuyedEquipments(project, laboratory)
                    );
                    // per ogni contratto settare il riferimento al progetto stesso
                    project.getContracts().forEach(contract -> contract.setProject(project));
                    project.getLaboratories().forEach(laboratory -> {
                                projectDAO.getBuyedEquipments(project, laboratory);
                    });
                });

                // carica la tabella con i project
                projectsTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("cup"));
                projectsTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
                projectsTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("endDate"));
                projectsTable.getItems().addAll(employeeProjects);

                // carica le colonne della tabella equipment Request
                equipmentRequestTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
                equipmentRequestTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("type"));
                equipmentRequestTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("quantity"));

                // carica le colonne della tabella project Salaried
                hiredProjectSalariedTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("firstName"));
                hiredProjectSalariedTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("lastName"));
                hiredProjectSalariedTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("role"));
                break;
        }
    }
}
