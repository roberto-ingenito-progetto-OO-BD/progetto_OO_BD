package com.company.Model;

import java.util.UUID;

public class EquipmentRequest {

    private String code;
    private String name;
    private String specs;
    private String type;
    private int quantity;
    private Laboratory laboratory;


    public EquipmentRequest(String code, String name, String specs, String type, int quantity) {
        this.code = code;
        this.name = name;
        this.specs = specs;
        this.type = type;
        this.quantity = quantity;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getSpecs() {
        return specs;
    }

    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public Laboratory getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(Laboratory laboratory) {
        this.laboratory = laboratory;
    }

}
