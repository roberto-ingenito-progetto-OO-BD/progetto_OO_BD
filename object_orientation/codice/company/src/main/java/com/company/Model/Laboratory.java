package com.company.Model;

import java.util.ArrayList;

public class Laboratory {
    private int labCode;
    private String name;
    private String topic;

    private ArrayList<Project> projects;
    private ArrayList<EquipmentRequest> equipmentRequests;
    private ArrayList<Equipment> equipment;
    private Senior scientificManager;
    private ArrayList<Employee> employees;


    /// CONSTRUCTOR
    public Laboratory(
            int labCode,
            String name,
            String topic
    ) {
        this.labCode = labCode;
        this.name = name;
        this.topic = topic;
    }

    /// GETTERS
    public String getName() {
        return name;
    }

    public String getTopic() {
        return topic;
    }

    public int getLabCode() {
        return labCode;
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public ArrayList<Equipment> getEquipment() {
        return equipment;
    }

    public Senior getScientificManager() {
        return scientificManager;
    }

    /// SETTERS
    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }

    public void setEquipment(ArrayList<Equipment> equipment) {
        this.equipment = equipment;
    }

    public void setScientificManager(Senior scientificManager) {
        this.scientificManager = scientificManager;
    }


}
