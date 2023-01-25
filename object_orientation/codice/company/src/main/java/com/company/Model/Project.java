package com.company.Model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Project {
    private final String cup;
    private final String name;

    private float funds;
    private final String description;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalDate deadline;

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

    // GETTER
    public String getName() {
        return name;
    }

    // SETTER
    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public void setScientificReferent(ScientificReferent scientificReferent) {
        this.scientificReferent = scientificReferent;
    }

    public void setEquipmentRequests(ArrayList<EquipmentRequest> equipmentRequests) {
        this.equipmentRequests = equipmentRequests;
    }

    public void setEquipments(ArrayList<Equipment> equipments) {
        this.equipments = equipments;
    }

    public void setContracts(ArrayList<Contract> contracts) {
        this.contracts = contracts;
    }

    public void setFunds(float funds) {
        this.funds = funds;
    }
}
