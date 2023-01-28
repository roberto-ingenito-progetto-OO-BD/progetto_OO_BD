package com.company.Controller;

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

import java.time.LocalDate;
import java.util.ArrayList;

public class EmployeeDashboardController {
    private final Employee employee;

    /// FXML OBJECTS
    private @FXML TabPane tabPane;

    private @FXML Tab laboratoryTab;
    private @FXML Tab projectsTab;
    private @FXML Tab hiringTab;
    private @FXML Tab purchaseTab;

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

    // laboratory working projects table
    // tabella dei progetti in cui lavora il laboratorio dove lavora l'impiegato loggato
    private @FXML TableView<Project> labWorkingProjectsTable;
    private @FXML TableColumn<Project, String> cupColumn;
    private @FXML TableColumn<Project, String> labWorkingProjectNameColumn;

    // tabella dei laboratori in cui l'impiegato loggato ne è manager
    private @FXML TableView<Laboratory> empManagerLabsTable;
    private @FXML TableColumn<Laboratory, String> labCodeColumn;
    private @FXML TableColumn<Laboratory, String> labNameColumn;

    // equipment request table
    private @FXML TableView<EquipmentRequest> equipmentRequestTable;
    private @FXML TableColumn<EquipmentRequest, String> equipmentNameColumn;
    private @FXML TableColumn<EquipmentRequest, String> equipmentTypeColumn;
    private @FXML TableColumn<EquipmentRequest, Integer> equipmentQuantityColumn;

    // project salaried table
    private @FXML TableView<ProjectSalaried> hiredProjectSalariedTable;
    private @FXML TableColumn<ProjectSalaried, String> hiredProjSalFirstNameColumn;
    private @FXML TableColumn<ProjectSalaried, String> hiredProjSalRoleColumn;
    private @FXML TableColumn<ProjectSalaried, String> hiredProjSalLastNameColumn;

    // projects table
    // tabella dei progetti di cui un impiegato (manager) è manager
    private @FXML TableView<Project> projectsTable;
    private @FXML TableColumn<Project, String> projectCUPColumn;
    private @FXML TableColumn<Project, String> projectNameColumn;
    private @FXML TableColumn<Project, LocalDate> projectEndDateColumn;

    /// CONSTRUCTOR
    public EmployeeDashboardController(Employee employee) {
        this.employee = employee;
    }

    /// FXML METHODS
    private @FXML void initialize() {
        ProjectDAOImplementation projectDAO = new ProjectDAOImplementation();
        ArrayList<Project> employeeProjects = new ArrayList<>();

        // imposta il testo che appare quando la lista è vuota
        laboratoryEquipmentListView.setPlaceholder(new Label("Non c'è attrezzatura in questo laboratorio"));

        // imposta il testo che appare quando la tabella è vuota
        labWorkingProjectsTable.setPlaceholder(new Label("Il laboratorio non lavora a nessun progetto"));
        equipmentRequestTable.setPlaceholder(new Label("Nessuna richiesta pendente"));
        hiredProjectSalariedTable.setPlaceholder(new Label("Nessun impiegato assunto"));
        empManagerLabsTable.setPlaceholder(new Label("Non ci sono laboratori in cui sei manager"));

        // imposta nome, cognome e tipo dell'impiegato nella barra in alto
        userNameLabel.setText(employee.getFirstName() + " " + employee.getLastName());
        empTypeLabel.setText(employee.getType().toString());

        // l'impiegato lavora in un laboratorio
        if (employee.getLaboratory() != null) {
            // imposta (nelle apposite label) il nome, il topic e il manager
            // del laboratorio in cui lavora l'impiegato loggato
            laboratoryNameLabel.setText(employee.getLaboratory().getName());
            laboratoryTopicLabel.setText(employee.getLaboratory().getTopic());
            laboratoryManagerLabel.setText(employee.getLaboratory().getScientificManager().getFullName());

            // imposta il tipo delle colonne della tabella "labWorkingProjectsTableView"
            // poi riempie la tabella "labWorkingProjectsTableView"
            cupColumn.setCellValueFactory(new PropertyValueFactory<>("cup"));
            labWorkingProjectNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            labWorkingProjectsTable.getItems().addAll(
                    employee.getLaboratory().getProjects()
            );

            // riempie la ListView "equipmentListView"
            for (Equipment equipment : employee.getLaboratory().getEquipment()) {
                laboratoryEquipmentListView.getItems().add(
                        equipment.getName() + ", " + equipment.getType() + ", " + equipment.getTechSpecs()
                );
            }

            labCodeColumn.setCellValueFactory(new PropertyValueFactory<>("labCode"));
            labNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            if (employee instanceof Senior) {
                empManagerLabsTable.getItems().addAll(
                        ((Senior) employee).getLaboratories()
                );
            }
        }
        // l'impiegato non lavora in un laboratorio
        else {
            // rendo non visibile l'intera tab
            // il doppio getParent arriva al padre di tutti gli elementi nella tab
            labWorkingProjectsTable.getParent().getParent().setVisible(false);

            // rendo visibile la label "noWorkingProjectLabel" per fornire il messaggio
            noWorkingProjectLabel.setVisible(true);
        }

        // imposta quali elementi possono essere visualizzati in base al tipo dell'impiegato loggato
        switch (employee.getType()) {
            case junior, middle:
                projectsButtonLabel.setVisible(false); // rendo non visibile il pulsante "Proggetti"
                break;
            case senior:
                Senior senior = (Senior) employee;
                employeeProjects.addAll(senior.getProjects()); // carica le informazioni della tabella "projectsTable" nella tab "Progetti"
                break;
            case manager:
                Manager manager = (Manager) employee;
                employeeProjects.addAll(manager.getProjects()); // carica le informazioni della tabella "projectsTable" nella tab "Progetti"
                break;
        }

        switch (employee.getType()) {
            case senior, manager:
                // per ogni progetto caricare le richieste di attrezzatura e informazioni aggiuntive
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
                });

                // caricare la tabella gui con i project
                projectCUPColumn.setCellValueFactory(new PropertyValueFactory<>("cup"));
                projectNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                projectEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

                projectsTable.getItems().addAll(employeeProjects);

                // carica le colonne della tabella equipment Request
                equipmentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                equipmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
                equipmentQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

                // carica le colonne della tabella project Salaried
                hiredProjSalFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
                hiredProjSalLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
                hiredProjSalRoleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
                break;
        }

        tabPane.addEventFilter(KeyEvent.KEY_PRESSED, event -> event.consume());
    }

    private @FXML void goToProjectTab() {
        changeTab(projectsTab);
    }

    private @FXML void goToLaboratoryTab() {
        changeTab(laboratoryTab);
    }

    private @FXML void goToHiringTab() {
        changeTab(hiringTab);
    }

    private @FXML void goToPurchaseTab() {
        changeTab(purchaseTab);
    }

    private @FXML void onLogOutClick() {
        Stage oldStage = (Stage) userNameLabel.getScene().getWindow();
        LoginController.logOut(oldStage);
    }

    // TAB LABORATORIO

    /**
     * Funzione che viene eseguita quando viene cliccata una riga
     * della tabella "labWorkingProjectTable"
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
     * della tabella "empManagerLabsTable"
     */
    private @FXML void onLabClick() {
        // prende il laboratorio della riga selezionata
        @Nullable Laboratory selectedLaboratory = empManagerLabsTable.getSelectionModel().getSelectedItem();

        // toglie la selezione dall'elementi cliccato, altrimenti rimane selezionato
        empManagerLabsTable.getSelectionModel().clearSelection();

        if (selectedLaboratory != null) showSelectedLaboratory(selectedLaboratory);
    }

    // TAB PROGETTI
    private @FXML void showSelectedProject(MouseEvent mouseEvent) {
        ProjectCard projectCard;
        Stage newStage;
        Scene newScene;

        // se le tabelle sono riempite , svuotarle
        if (!equipmentRequestTable.getItems().isEmpty()) {
            equipmentRequestTable.getItems().clear();
        }
        if (!hiredProjectSalariedTable.getItems().isEmpty()) {
            hiredProjectSalariedTable.getItems().clear();
        }

        Project selectedProject = projectsTable.getSelectionModel().getSelectedItem();

        if (selectedProject != null) {

            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                // apre schermata del progetto
                projectCard = new ProjectCard();
                newScene = projectCard.getScene(selectedProject, employee, projectsTable);
                newStage = new Stage();
                newStage.setTitle("Project Informations");
                newStage.setScene(newScene);
                newStage.setResizable(false);
                newStage.initModality(Modality.APPLICATION_MODAL);
                newStage.show();
            }

            // carica tutti gli equipment request del progetto selezionato nella tabella di equipment
            // riempie la tabella delle richieste
            for (EquipmentRequest request : selectedProject.getEquipmentRequests())
                equipmentRequestTable.getItems().add(request);


            // carica tutti i project salaried del progetto selezionato nella tabella degli impiegati
            // riempire lista e caricare la tabella
            selectedProject.getContracts().forEach(
                    contract -> hiredProjectSalariedTable.getItems().add(contract.getProjectSalaried())
            );
        }

    }

    private @FXML void getSelectedRequest(MouseEvent mouseEvent) {

    }

    private @FXML void getSelectedEquipment(MouseEvent mouseEvent) {
    }

    /// METHODS
    private void changeTab(Tab tab) {
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();

        // impedisce di cambiare tab se si trova in una delle seguenti tab
        if (currentTab == hiringTab) return;
        if (currentTab == purchaseTab) return;

        projectsButtonLabel.setOpacity(0.4);
        laboratoryButtonLabel.setOpacity(0.4);

        if (tab == projectsTab) projectsButtonLabel.setOpacity(1);

        if (tab == laboratoryTab) laboratoryButtonLabel.setOpacity(1);

        tabPane.getSelectionModel().select(tab);
    }

    private void showSelectedLaboratory(Laboratory laboratory) {
        SelectedLaboratoryCard selectedLaboratoryCard = new SelectedLaboratoryCard();
        Scene scene = selectedLaboratoryCard.getScene(laboratory);

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
            labWorkingProjectsTable.refresh();
        });
    }
}