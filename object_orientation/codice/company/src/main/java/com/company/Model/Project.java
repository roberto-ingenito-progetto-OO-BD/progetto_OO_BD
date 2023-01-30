package com.company.Model;

import org.jetbrains.annotations.Nullable;

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
    private ArrayList<Laboratory> laboratories;
    private Manager manager;
    private Senior scientificReferent;

    private ArrayList<EquipmentRequest> equipmentRequests;
    private ArrayList<Equipment> equipments = new ArrayList<>();
    private ArrayList<Contract> contracts;

    private final int maxLabs = 3;

    /// CONSTRUCTOR
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
    public Project(){}

    /// GETTERS
    public LocalDate getEndDate() {
        return endDate;
    }

    public String getCup() {
        return cup;
    }

    public String getName() {
        return name;
    }

    public float getFunds() {
        return funds;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public ArrayList<Laboratory> getLaboratories() {
        return laboratories;
    }

    public Manager getManager() {
        return manager;
    }

    public Senior getScientificReferent() {
        return scientificReferent;
    }

    public ArrayList<EquipmentRequest> getEquipmentRequests() {
        return equipmentRequests;
    }

    public ArrayList<Equipment> getEquipments() {
        return equipments;
    }

    public ArrayList<Contract> getContracts() {
        return contracts;
    }

    /// SETTERS
    public void setCup(String cup) {
        this.cup = cup;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFunds(float funds) {
        this.funds = funds;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void setLaboratories(ArrayList<Laboratory> laboratories) {
        if(laboratories.size() <= maxLabs){
            this.laboratories = laboratories;
        }
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public void setScientificReferent(Senior scientificReferent) {
        this.scientificReferent = scientificReferent;
    }

    public void setEquipmentRequests(@Nullable ArrayList<EquipmentRequest> equipmentRequests) {
        this.equipmentRequests = equipmentRequests;
    }

    public void setEquipments(ArrayList<Equipment> equipments) {
        this.equipments = equipments;
    }

    public void addEquipment(Equipment equipment){
        this.equipments.add(equipment);
    }

    public void setContracts(@Nullable ArrayList<Contract> contracts) {
        this.contracts = contracts;
    }

    /// METHODS
    public void addLaboratory(Laboratory laboratory){
        if(laboratories.size() <= maxLabs - 1){
            laboratories.add(laboratory);
        }
    }

    public void addContract(Contract contract) {
        this.contracts.add(contract);
    }

}
