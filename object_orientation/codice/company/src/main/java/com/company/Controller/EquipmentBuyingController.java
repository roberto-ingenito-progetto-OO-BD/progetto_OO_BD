package com.company.Controller;

import com.company.Model.*;
import com.company.PostgresDAO.ProjectDAOImplementation;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Controller della finestra che permette di acquistare l'attrezzatura
 */
public class EquipmentBuyingController {
    private final Project currentProject;
    private final EquipmentRequest currentEquipmentRequest;
    private BigDecimal remainingFundsValue;
    private float totalPrice;

    /// FXML OBJECTS
    private @FXML Label labNameLabel;
    private @FXML Label errorLabel;
    private @FXML Label selectedProjectName;
    private @FXML Label remainingFunds;
    private @FXML Label equipmentNameLabel;
    private @FXML Label techSpecsLabel;
    private @FXML Label quantityLabel;
    private @FXML Label totalPriceLabel;

    private @FXML TextField priceTextField;

    private @FXML Button buyEquipmentButton;
    private @FXML Button abortOperationButton;

    private @FXML TableView<Equipment> equipmentLog;


    /// CONSTRUCTOR

    /**
     * Instantiates a new Equipment buying controller.
     *
     * @param project          the project
     * @param equipmentRequest the equipment request
     */
    public EquipmentBuyingController(Project project, @Nullable EquipmentRequest equipmentRequest) {
        this.currentProject = project;
        this.currentEquipmentRequest = equipmentRequest;

    }

    /// FXML METHODS

    /**
     * Funzione che si avvia subito dopo il costruttore <br/>
     * Carica i fondi del progetto selezionato, imposta i testi delle label, carica l'attrezzatura già presente nel laboratorio,
     * imposta le tabelle.
     */
    private @FXML void initialize() {
        // recuperare i fondi disponibili per l'acquisto dal db
        ProjectDAOImplementation projectDAO = new ProjectDAOImplementation();
        remainingFundsValue = projectDAO.remainingEquipmentFunds(currentProject.getCup());

        ArrayList<Equipment> equipments = new ArrayList<>();

        // carica le colonne della tabella
        equipmentLog.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
        equipmentLog.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("price"));
        equipmentLog.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));


        // se la richiesta è vuota non rendere cliccabile il pulsante e caricare tutti gli equipment acquistati per tutti i laboratori
        if(currentEquipmentRequest == null){
            buyEquipmentButton.setDisable(true);
            priceTextField.setDisable(true);
            equipmentLog.getItems().addAll(currentProject.getEquipments());
            labNameLabel.setText("Tutte le attrezzature acquistate: ");
            remainingFunds.setText("€" + remainingFundsValue.setScale(2, RoundingMode.UNNECESSARY));
            return;
        } else {
            // carica gli equipment che sono stati acquistati dal progetto soltanto per il laboratorio richiesto
            currentProject.getLaboratories().forEach(laboratory -> {
                if (laboratory.getLabCode() == currentEquipmentRequest.getLaboratory().getLabCode()) {
                    equipments.addAll(laboratory.getEquipments());
                }
            });

            // caricare la tabella con le precedenti attrezzature acquistate dal progetto per questo laboratorio
            equipmentLog.getItems().addAll(equipments);

            // settare le label con le informazioni in ingresso
            selectedProjectName.setText(currentProject.getName());
            equipmentNameLabel.setText(equipmentNameLabel.getText() + " " + currentEquipmentRequest.getName());
            techSpecsLabel.setText(techSpecsLabel.getText() + " " + currentEquipmentRequest.getSpecs());
            quantityLabel.setText(quantityLabel.getText() + " " + currentEquipmentRequest.getQuantity());
            labNameLabel.setText(currentEquipmentRequest.getLaboratory().getName());
            remainingFunds.setText("€" + remainingFundsValue.setScale(2, RoundingMode.UNNECESSARY));
        }
    }

    /**
     * Funzione che viene eseguita al click del pulsante "Annulla" <br/>
     * Annulla l'acquisto dell'attrezzatura richiesta e torna alla finestra precedente
     */
    private @FXML void abortOperation() {
        Stage currentStage = (Stage) abortOperationButton.getScene().getWindow();
        currentStage.close();
    }


    /**
     * Funzione che viene eseguita al click del pulsante "Conferma" <br/>
     * Esegue l'inserimento dell'attrezzatura nel database, successivamente aggiorna il model
     */
    private @FXML void buyEquipment() {
        ProjectDAOImplementation projectDAO = new ProjectDAOImplementation();
        ArrayList<Equipment> newEquipments = new ArrayList<>();

        // controllare che sia inserito un prezzo valido
        if (!totalPriceLabel.getText().isEmpty()) {
            float unitPrice = Float.parseFloat(priceTextField.getText());
            errorLabel.setText("");
            if (totalPrice <= remainingFundsValue.floatValue()) {
                errorLabel.setText("");
                try {
                    projectDAO.buyEquipment(currentProject, currentEquipmentRequest, unitPrice);

                    // aggiornare il model
                    for (int i = 0; i < currentEquipmentRequest.getQuantity(); i++) {
                        newEquipments.add(
                                new Equipment(
                                        currentEquipmentRequest.getName(),
                                        currentEquipmentRequest.getType(),
                                        currentEquipmentRequest.getSpecs(),
                                        unitPrice,
                                        LocalDate.now()
                                )
                        );
                    }

                    currentProject.getEquipments().addAll(newEquipments);
                    currentProject.getLaboratories().forEach(laboratory -> {
                        if (laboratory.getLabCode() == currentEquipmentRequest.getLaboratory().getLabCode()) {
                            laboratory.getEquipments().addAll(newEquipments);
                        }
                    });

                    // aggiornare la tabella e la label
                    equipmentLog.getItems().addAll(newEquipments);
                    remainingFundsValue = projectDAO.remainingEquipmentFunds(currentProject.getCup());
                    remainingFunds.setText("€" + remainingFundsValue.setScale(2, RoundingMode.UNNECESSARY));
                    buyEquipmentButton.setDisable(true);
                    currentProject.getEquipmentRequests().remove(currentEquipmentRequest);
                } catch (Exception err) {
                    throw new RuntimeException(err);
                }

            } else {
                errorLabel.setText("Non abbastanza fondi");
            }
        } else {
            errorLabel.setText("Nessun prezzo stabilito");
        }
    }

    /**
     * Aggiorna il testo che mostra il prezzo totale
     */
    private @FXML void updateTotalPrice() {
        if (!priceTextField.getText().isEmpty()) {
            totalPrice = Float.parseFloat(priceTextField.getText());
            totalPrice = totalPrice * currentEquipmentRequest.getQuantity();
            // totalPrice = Float.parseFloat(new DecimalFormat(",##").format(totalPrice));
            totalPriceLabel.setText(String.valueOf(totalPrice));
        } else {
            totalPriceLabel.setText("");
        }
    }
}
