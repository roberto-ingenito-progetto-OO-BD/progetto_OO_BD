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

}
