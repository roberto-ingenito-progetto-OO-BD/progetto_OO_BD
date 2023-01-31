package com.company.Model;

import java.util.ArrayList;

public class Laboratory {
    private final int labCode;
    private final String name;
    private final String topic;

    private ArrayList<Project> projects = new ArrayList<>();
    private ArrayList<EquipmentRequest> equipmentRequests;
    private ArrayList<Equipment> equipments = new ArrayList<>();
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

    public ArrayList<Equipment> getEquipments() {
        return equipments;
    }

    public Senior getScientificManager() {
        return scientificManager;
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    /// SETTERS
    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }

    public void setEquipment(ArrayList<Equipment> equipment) {
        this.equipments = equipment;
    }
    public void addEquipment(Equipment equipment) {
        this.equipments.add(equipment);
    }

    public void setScientificManager(Senior scientificManager) {
        this.scientificManager = scientificManager;
    }

    public void setEmployees(ArrayList<Employee> employees) {
        this.employees = employees;
    }

}
