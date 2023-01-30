package com.company.Model;

import java.util.ArrayList;

public class Laboratory {
    private int labCode;
    private String name;
    private String topic;

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

    public ArrayList<Equipment> getEquipment() {
        return equipments;
    }

    public Senior getScientificManager() {
        return scientificManager;
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public ArrayList<EquipmentRequest> getEquipmentRequests() {
        return equipmentRequests;
    }

    /// SETTERS
    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }
    public void addProject(Project newProject) {
        this.projects.add(newProject);
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

    public void setLabCode(int labCode) {
        this.labCode = labCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setEquipmentRequests(ArrayList<EquipmentRequest> equipmentRequests) {
        this.equipmentRequests = equipmentRequests;
    }

    public void setEquipmentRequest(EquipmentRequest equipmentRequest) {
        this.equipmentRequests.add(equipmentRequest);
    }

    public void setEmployees(ArrayList<Employee> employees) {
        this.employees = employees;
    }

    /// METHODS
    public void dropProject(Project project) {
        projects.remove(project);
    }
}
