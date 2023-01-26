package com.company.Model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Project {
    private String cup;
    private String name;

    private float funds;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate deadline;

    private final Laboratory[] laboratories = new Laboratory[3];
    private Manager manager;
    private ScientificReferent scientificReferent;
    private ArrayList<EquipmentRequest> equipmentRequests;
    private ArrayList<Equipment> equipments;
    private ArrayList<Contract> contracts;

    public Project(
            String cup,
            String name,
            String description,
            LocalDate startDate,
            LocalDate endDate,
            LocalDate deadline
    ) {
        this.cup = cup;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.deadline = deadline;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getCup() {
        return cup;
    }

    public void setCup(String cup) {
        this.cup = cup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getFunds() {
        return funds;
    }

    public void setFunds(float funds) {
        this.funds = funds;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Laboratory[] getLaboratories() {
        return laboratories;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public ScientificReferent getScientificReferent() {
        return scientificReferent;
    }

    public void setScientificReferent(ScientificReferent scientificReferent) {
        this.scientificReferent = scientificReferent;
    }

    public ArrayList<EquipmentRequest> getEquipmentRequests() {
        return equipmentRequests;
    }

    public void setEquipmentRequests(ArrayList<EquipmentRequest> equipmentRequests) {
        this.equipmentRequests = equipmentRequests;
    }

    public ArrayList<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(ArrayList<Equipment> equipments) {
        this.equipments = equipments;
    }

    public ArrayList<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(ArrayList<Contract> contracts) {
        this.contracts = contracts;
    }
}
