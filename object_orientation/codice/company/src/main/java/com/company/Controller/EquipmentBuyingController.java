package com.company.Controller;

import com.company.Model.*;
import com.company.PostgresDAO.ProjectDAOImplementation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class EquipmentBuyingController {
    private @FXML Label labNameLabel;
    private @FXML Label selectedProjectName;
    private @FXML Label remainingFunds;
    private @FXML Label equipmentNameLabel;
    private @FXML Label techSpecsLabel;
    private @FXML Label quantityLabel;
    private @FXML Label totalPriceLabel;
    private @FXML TextField priceTextField;
    private @FXML Button buyEquipmentButton;
    private @FXML Button abortOperationButton;
    private @FXML TableView EquipmentLog;
    private Project currentProject;
    private EquipmentRequest currentEquipmentRequest;

    public EquipmentBuyingController(Project project, EquipmentRequest equipmentRequest) {
        this.currentProject = project;
        this.currentEquipmentRequest = equipmentRequest;
    }

    private @FXML void initialize() {
        // recuperare i fondi disponibili per l'acquisto dal db
        ProjectDAOImplementation projectDAO = new ProjectDAOImplementation();
        BigDecimal remainingFundsValue = projectDAO.remainingEquipmentFunds(currentProject.getCup());

        System.out.println(currentProject.getFunds() - currentProject.getFunds()/2);
        System.out.println(remainingFundsValue);

        currentProject.getEquipments().forEach(equipment -> {
            equipment.getName();
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
    }

    private @FXML void abortOperation(ActionEvent actionEvent) {
    }

    private @FXML void buyEquipment(ActionEvent actionEvent) {
    }

    private @FXML void updateTotalPrice(){
        if(totalPriceLabel.getText().isEmpty()){
            return;
        } else {
            totalPriceLabel.setText("");
            BigDecimal unitPrice = new BigDecimal(priceTextField.getText());
            BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(currentEquipmentRequest.getQuantity()));
            totalPriceLabel.setText("Totale: €" + totalPrice);
        }
    }
}
