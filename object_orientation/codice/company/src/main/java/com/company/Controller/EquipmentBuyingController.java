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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;

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
    private @FXML TableView<Equipment> EquipmentLog;
    private Project currentProject;
    private EquipmentRequest currentEquipmentRequest;

    private BigDecimal remainingFundsValue;

    public EquipmentBuyingController(Project project, EquipmentRequest equipmentRequest) {
        this.currentProject = project;
        this.currentEquipmentRequest = equipmentRequest;
    }

    private @FXML void initialize() {
        // recuperare i fondi disponibili per l'acquisto dal db
        ProjectDAOImplementation projectDAO = new ProjectDAOImplementation();
        remainingFundsValue = projectDAO.remainingEquipmentFunds(currentProject.getCup());

        ArrayList<Equipment> equipments = new ArrayList<>();
        currentProject.getLaboratories().forEach(laboratory -> {
            if(laboratory.getLabCode() == currentEquipmentRequest.getLaboratory().getLabCode()){
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

    private @FXML void abortOperation(ActionEvent actionEvent) {
    }

    private @FXML void buyEquipment(ActionEvent actionEvent) {
        // controllare che sia inserito un prezzo valido
        if(!totalPriceLabel.getText().isEmpty()){
           Float price = Float.parseFloat(totalPriceLabel.getText());
           if(price < remainingFundsValue.floatValue()){



           } else {
               System.out.println("non abbastanza fondi");
               return;
           }
        }
    }

    private @FXML void updateTotalPrice(){
        if(!priceTextField.getText().isEmpty()){
            Float totalPrice = Float.parseFloat(priceTextField.getText());
            System.out.println(totalPrice);
            totalPrice = totalPrice * currentEquipmentRequest.getQuantity();
            totalPriceLabel.setText(totalPrice.toString());
        } else {
            if(!totalPriceLabel.getText().isEmpty()){
                totalPriceLabel.setText("");
            }
        }
    }
}
