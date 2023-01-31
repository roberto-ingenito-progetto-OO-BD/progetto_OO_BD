package com.company.Controller;

import com.company.Model.*;
import com.company.PostgresDAO.ProjectDAOImplementation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;

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

    private @FXML TableView<Equipment> EquipmentLog;


    /// CONSTRUCTOR
    public EquipmentBuyingController(Project project, EquipmentRequest equipmentRequest) {
        this.currentProject = project;
        this.currentEquipmentRequest = equipmentRequest;
    }

    /// FXML METHODS
    private @FXML void initialize() {
        // recuperare i fondi disponibili per l'acquisto dal db
        ProjectDAOImplementation projectDAO = new ProjectDAOImplementation();
        remainingFundsValue = projectDAO.remainingEquipmentFunds(currentProject.getCup());

        ArrayList<Equipment> equipments = new ArrayList<>();
        currentProject.getLaboratories().forEach(laboratory -> {
            if (laboratory.getLabCode() == currentEquipmentRequest.getLaboratory().getLabCode()) {
                equipments.addAll(laboratory.getEquipments());
            }
        });

        // settare le label con le informazioni in ingresso
        selectedProjectName.setText(currentProject.getName());
        equipmentNameLabel.setText(equipmentNameLabel.getText() + " " + currentEquipmentRequest.getName());
        techSpecsLabel.setText(techSpecsLabel.getText() + " " + currentEquipmentRequest.getSpecs());
        quantityLabel.setText(quantityLabel.getText() + " " + currentEquipmentRequest.getQuantity());
        labNameLabel.setText(currentEquipmentRequest.getLaboratory().getName());
        remainingFunds.setText("€" + remainingFundsValue.setScale(2, RoundingMode.UNNECESSARY));

        // caricare le informazioni della richiesta di equipaggiamento

        // caricare la tabella con le precedenti attrezzature acquistate dal progetto per questo laboratorio
        EquipmentLog.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
        EquipmentLog.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("price"));
        EquipmentLog.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
        EquipmentLog.getItems().addAll(equipments);

    }

    private @FXML void abortOperation() {
        Stage currentStage = (Stage) abortOperationButton.getScene().getWindow();
        currentStage.close();
    }

    private @FXML void buyEquipment(ActionEvent actionEvent) {
        ProjectDAOImplementation projectDAO = new ProjectDAOImplementation();
        ArrayList<Equipment> newEquipments = new ArrayList<>();

        // controllare che sia inserito un prezzo valido
        if (!totalPriceLabel.getText().isEmpty()) {
            float unitPrice = Float.parseFloat(priceTextField.getText());
            errorLabel.setText("");
            if (totalPrice < remainingFundsValue.floatValue()) {
                errorLabel.setText("");
                try {
                        projectDAO.buyEquipment(currentProject, currentEquipmentRequest, unitPrice);
                        // aggiornare il model

                        for (int i = 0; i < currentEquipmentRequest.getQuantity(); i++){
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
                            if(laboratory.getLabCode() == currentEquipmentRequest.getLaboratory().getLabCode()){
                                laboratory.getEquipments().addAll(newEquipments);
                            }
                        });
                             // aggiornare la tabella e la label
                            EquipmentLog.getItems().addAll(newEquipments);
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
