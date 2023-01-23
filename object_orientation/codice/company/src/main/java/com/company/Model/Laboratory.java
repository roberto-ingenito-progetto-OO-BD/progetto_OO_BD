package com.company.Model;

import java.util.ArrayList;

public class Laboratory {
    private String labCode;
    private String name;
    private String topic;

    private ArrayList<Project> projects;
    private ArrayList<EquipmentRequest> equipmentRequests;
    private ScientificManager scientificManager;
    private ArrayList<Employee> employees;

    public Laboratory(String labCode, String name, String topic) {
        this.labCode = labCode;
        this.name = name;
        this.topic = topic;
    }

    // GETTERS
    public String getName() {
        return name;
    }
}
