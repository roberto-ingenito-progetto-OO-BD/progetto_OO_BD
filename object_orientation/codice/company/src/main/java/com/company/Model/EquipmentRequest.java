package com.company.Model;

public class EquipmentRequest {

    private String code;
    private final String name;
    private final String specs;
    private final String type;
    private final int quantity;
    private Laboratory laboratory;
    private Project project;

    /// CONSTRUCTOR
    public EquipmentRequest(String name, String specs, String type, int quantity) {
        this.name = name;
        this.specs = specs;
        this.type = type;
        this.quantity = quantity;
    }

    /// GETTER
    public Project getProject() {
        return project;
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

    /// SETTER
    public void setLaboratory(Laboratory laboratory) {
        this.laboratory = laboratory;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
