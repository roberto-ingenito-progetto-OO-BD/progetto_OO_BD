package com.company.Controller;

import com.company.GUI.ProjectCard;
import com.company.Model.*;
import com.company.PostgresDAO.ManagerDAOImplements;
import com.company.PostgresDAO.ProjectDAOImplementation;
import com.company.PostgresDAO.SeniorDAOImplementation;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.ArrayList;

public class EmployeeDashboardController {
    private final Employee employee;
    public TableView<EquipmentRequest> equipmentRequestTable;
    public TableColumn<EquipmentRequest, String> equipmentNameColumn;
    public TableColumn<EquipmentRequest, String> equipmentTypeColumn;
    public TableColumn<EquipmentRequest, Integer> equipmentQuantityColumn;
    public TableView<ProjectSalaried> projectSalariedTable;
    public TableColumn<ProjectSalaried, String> projectSalariedFirstNameColumn;

    public TableColumn<ProjectSalaried, String> projectSalariedRoleColumn;
    public Button hireProjectSalaried;
    public TableColumn<ProjectSalaried, String> projectSalariedLastNameColumn;
    private Senior currentSenior = null;
    private Manager currentManager = null;
    private final ArrayList<Project> employeeProjects = new ArrayList<>();
    @FXML
    public TableView<Project> projectsTable;
    @FXML
    public TableColumn<Project, String> projectCUPColumn;
    @FXML
    public TableColumn<Project, String> projectNameColumn;
    @FXML
    public TableColumn<Project, LocalDate> projectEndDateColumn;
    private @FXML Tab laboratoryTab;
    private @FXML Tab projectsTab;

    private @FXML ListView<String> equipmentListView;

    private @FXML Button viewAllProjectsButton;

    private @FXML Label projectsLabel;
    private @FXML Label laboratoryLabel;

    private @FXML Label userNameLabel;
    private @FXML Label empTypeLabel;
    private @FXML Label laboratoryTopicLabel;
    private @FXML Label laboratoryNameLabel;
    private @FXML Label laboratoryManagerLabel;

    private @FXML StackPane noWorkingProjectLabel;

    private @FXML TabPane tabPane;

    private @FXML TableView<Project> labWorkingProjectsTableView;
    private @FXML TableColumn<Project, String> cupColumn;
    private @FXML TableColumn<Project, String> nameColumn;

    /// CONSTRUCTOR
    public EmployeeDashboardController(Employee employee) {
        this.employee = employee;
    }

    /// FXML METHODS
    private @FXML void initialize() {
        ProjectDAOImplementation projectDAO = new ProjectDAOImplementation();
        // imposta il testo che appare quando la lista è vuota
        equipmentListView.setPlaceholder(new Label("Non c'è attrezzatura in questo laboratorio"));

        // imposta il testo che appare quando la tabella è vuota
        labWorkingProjectsTableView.setPlaceholder(new Label("Il laboratorio non lavora a nessun progetto"));

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
                labWorkingProjectsTableView.getItems().add(project);
            }

            // riempie la ListView "equipmentListView"
            for (Equipment equipment : employee.getLaboratory().getEquipment()) {
                equipmentListView.getItems().add(
                        equipment.getName() + ", " + equipment.getType() + ", " + equipment.getTechSpecs()
                );
            }

        }
        // l'impiegato non lavora in un laboratorio
        else {
            // rendo non visibile l'intera tab
            // il doppio getParent arriva al padre di tutti gli elementi nella tab
            labWorkingProjectsTableView.getParent().getParent().setVisible(false);

            // rendo visibile la label "noWorkingProjectLabel" per fornire il messaggio
            noWorkingProjectLabel.setVisible(true);
        }

        // imposta quali elementi possono essere visualizzati in base al tipo dell'impiegato loggato
        switch (employee.getType()) {
            case junior, middle:
                projectsLabel.setVisible(false); // rendo non visibile il pulsante "Proggetti"
                projectsTab.setDisable(true); // Disabilito la tab dei progetti
                break;

            case senior, manager :

                if (employee.getType() == EmpType.senior){
                    currentSenior = new Senior(employee);
                    SeniorDAOImplementation seniorDAO = new SeniorDAOImplementation();
                    currentSenior.setProjects(
                            seniorDAO.isReferentProjects(currentSenior.getCf(), currentSenior.getType())
                    );
                    // caricare informazioni del project tab
                    currentSenior.getProjects().forEach(project -> {
                        employeeProjects.add(project);
                    });
                } else {
                    currentManager = new Manager(employee);
                    ManagerDAOImplements managerDAO = new ManagerDAOImplements();
                    currentManager.setProjects(
                            managerDAO.isManagerProjects(currentManager.getCf(), currentManager.getType())
                    );
                    // caricare informazioni del project tab
                    currentManager.getProjects().forEach(project -> {
                        employeeProjects.add(project);
                    });
                }

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
                ObservableList<Project> projectsList = projectsTable.getItems();

                employeeProjects.forEach(project -> {
                    projectsList.add(project);
                });
                projectsTable.setItems(projectsList);

                // carica le colonne della tabella equipment Request
                equipmentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                equipmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
                equipmentQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

                // carica le colonne della tabella project Salaried
                projectSalariedFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
                projectSalariedLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
                projectSalariedRoleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
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

    private @FXML void onTableRowClick() {
        ProjectCard projectCard;
        @Nullable Project selected = labWorkingProjectsTableView.getSelectionModel().getSelectedItem();
        labWorkingProjectsTableView.getSelectionModel().clearSelection();

        Stage newStage;
        Scene newScene;

        if (selected != null) {
            // crea una nuova schermata con le informazioni del progetto riferito al contratto selezionato
            projectCard = new ProjectCard();
            newScene = projectCard.getScene(selected, null);

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


    private @FXML void showSelectedProject(MouseEvent mouseEvent) {
        ProjectCard projectCard;
        Stage newStage;
        Scene newScene;

        // se le tabelle sono riempite , svuotarle
        if(!equipmentRequestTable.getItems().isEmpty()) {
            equipmentRequestTable.getItems().clear();
        }
        if(!projectSalariedTable.getItems().isEmpty()) {
            projectSalariedTable.getItems().clear();
        }

        Project selectedProject = projectsTable.getSelectionModel().getSelectedItem();

        if(selectedProject != null) {

        if(mouseEvent.getButton() == MouseButton.SECONDARY) {
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
            if (selectedProject.getEquipmentRequests().isEmpty()) {
                // TODO impostare la tabella in modo che mostri un messaggio se non vi sono attrezzature
                System.out.println("nessuna richiesta");
            } else {
                    ObservableList<EquipmentRequest> selectedProjectEquipmentRequests = equipmentRequestTable.getItems();
                    selectedProject.getEquipmentRequests().forEach(equipmentRequest -> {
                        // riempire la lista e caricare la tabella
                        selectedProjectEquipmentRequests.add(equipmentRequest);

                });
                        equipmentRequestTable.setItems(selectedProjectEquipmentRequests);
            }

            // carica tutti i project salaried del progetto selezionato nella tabella degli impiegati
            if (selectedProject.getContracts().isEmpty()) {
                // TODO impostare la tabella in modo che mostri un messaggio se non vi sono attrezzature
                System.out.println("nessun project salaried");
            } else {
                    ObservableList<ProjectSalaried> selectedProjectProjectSalaried = projectSalariedTable.getItems();
                    selectedProject.getContracts().forEach(contract -> {
                        // riempire lista e caricare la tabella
                        selectedProjectProjectSalaried.add(
                                contract.getProjectSalaried()
                        );
                    });
                        projectSalariedTable.setItems(selectedProjectProjectSalaried);
            }
        }

    }

    @FXML
    public void getSelectedRequest(MouseEvent mouseEvent) {

    }

    @FXML
    public void getSelectedEquipment(MouseEvent mouseEvent) {
    }

    @FXML
    public void showHiringScene(ActionEvent actionEvent) {
    }


    /// METHODS
    private void changeTab(Tab tab) {
        projectsLabel.setOpacity(0.4);
        laboratoryLabel.setOpacity(0.4);

        if (tab == projectsTab) projectsLabel.setOpacity(1);

        if (tab == laboratoryTab) laboratoryLabel.setOpacity(1);

        tabPane.getSelectionModel().select(tab);

    }
}