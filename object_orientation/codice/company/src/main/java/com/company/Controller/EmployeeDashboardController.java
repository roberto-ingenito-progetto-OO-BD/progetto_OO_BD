package com.company.Controller;

import com.company.GUI.ProjectCard;
import com.company.Model.*;
import com.company.PostgresDAO.ManagerDAOImplements;
import com.company.PostgresDAO.ProjectDAOImplementation;
import com.company.PostgresDAO.SeniorDAOImplementation;
import javafx.event.ActionEvent;
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

    private final ArrayList<Project> employeeProjects = new ArrayList<>();

    /// FXML OBJECTS
    private @FXML TabPane tabPane;

    private @FXML Tab laboratoryTab;
    private @FXML Tab projectsTab;

    private @FXML ListView<String> laboratoryEquipmentListView;

    private @FXML Button viewAllProjectsButton;
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
    private @FXML TableView<Project> labWorkingProjectsTable;
    private @FXML TableColumn<Project, String> cupColumn;
    private @FXML TableColumn<Project, String> nameColumn;

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

        // imposta il testo che appare quando la lista è vuota
        laboratoryEquipmentListView.setPlaceholder(new Label("Non c'è attrezzatura in questo laboratorio"));

        // imposta il testo che appare quando la tabella è vuota
        labWorkingProjectsTable.setPlaceholder(new Label("Il laboratorio non lavora a nessun progetto"));
        equipmentRequestTable.setPlaceholder(new Label("Nessuna richiesta pendente"));
        hiredProjectSalariedTable.setPlaceholder(new Label("Nessun impiegato assunto"));

        // imposta nome, cognome e tipo dell'impiegato nella barra in alto
        userNameLabel.setText(employee.getFirstName() + " " + employee.getLastName());
        empTypeLabel.setText(employee.getType().toString());

        // l'impiegato lavora in un laboratorio
        if (employee.getLaboratory() != null) {
            // imposta il nome e il topic del laboratorio
            laboratoryNameLabel.setText(employee.getLaboratory().getName());
            laboratoryTopicLabel.setText(employee.getLaboratory().getTopic());
            laboratoryManagerLabel.setText(employee.getLaboratory().getScientificManager().getFullName());


            // imposta il tipo delle colonne della tabella "labWorkingProjectsTableView"
            cupColumn.setCellValueFactory(new PropertyValueFactory<>("cup"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

            // riempie la tabella "labWorkingProjectsTableView"
            for (Project project : employee.getLaboratory().getProjects()) {
                labWorkingProjectsTable.getItems().add(project);
            }

            // riempie la ListView "equipmentListView"
            for (Equipment equipment : employee.getLaboratory().getEquipment()) {
                laboratoryEquipmentListView.getItems().add(
                        equipment.getName() + ", " + equipment.getType() + ", " + equipment.getTechSpecs()
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
                projectsTab.setDisable(true); // Disabilito la tab dei progetti
                break;
            case senior:
                Senior senior = (Senior) employee;
                SeniorDAOImplementation seniorDAO = new SeniorDAOImplementation();

                senior.setProjects(
                        seniorDAO.isReferentProjects(senior.getCf(), senior.getType())
                );

                senior.setLaboratories(
                        seniorDAO.isManagerLaboratory(senior.getCf(), senior.getType())
                );

                // caricare informazioni del project tab
                employeeProjects.addAll(senior.getProjects());
                break;
            case manager:
                Manager manager = (Manager) employee;
                ManagerDAOImplements managerDAO = new ManagerDAOImplements();
                manager.setProjects(
                        managerDAO.managerWorkingProjects(manager.getCf(), manager.getType())
                );

                // caricare informazioni del project tab
                employeeProjects.addAll(manager.getProjects());
                break;
        }

        switch (employee.getType()) {
            case junior, middle:
                break;
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

        // rendo visibile il pulsante per visualizzare tutti i progetti sul database
        // il compito spetta solo al manager di laboratorio (senior)
        if (employee.getType() == EmpType.senior)
            viewAllProjectsButton.setVisible(true);
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
            selectedProject.getContracts().forEach(contract -> {
                hiredProjectSalariedTable.getItems().add(contract.getProjectSalaried());
            });
        }

    }

    private @FXML void getSelectedRequest(MouseEvent mouseEvent) {

    }

    private @FXML void getSelectedEquipment(MouseEvent mouseEvent) {
    }

    private @FXML void showHiringScene(ActionEvent actionEvent) {
    }


    /// METHODS
    private void changeTab(Tab tab) {
        projectsButtonLabel.setOpacity(0.4);
        laboratoryButtonLabel.setOpacity(0.4);

        if (tab == projectsTab) projectsButtonLabel.setOpacity(1);

        if (tab == laboratoryTab) laboratoryButtonLabel.setOpacity(1);

        tabPane.getSelectionModel().select(tab);

    }
}