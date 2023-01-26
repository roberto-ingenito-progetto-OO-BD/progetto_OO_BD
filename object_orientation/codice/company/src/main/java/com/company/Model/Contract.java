package com.company.Model;

import java.time.LocalDate;

public class Contract {

    LocalDate hireDate;
    LocalDate expiration;
    float pay;
    private ProjectSalaried projectSalaried;
    private Project project;

    public Contract(LocalDate hireDate, LocalDate expiration, float pay) {
        this.hireDate = hireDate;
        this.expiration = expiration;
        this.pay = pay;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }
    public void setProject(Project project) {
        this.project = project;
    }
    public void setProjectSalaried (ProjectSalaried projectSalaried) {
        this.projectSalaried =  projectSalaried;
    }

    public LocalDate getExpiration() {
        return expiration;
    }
    public Project getProject() {
        return project;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public void setExpiration(LocalDate expiration) {
        this.expiration = expiration;
    }

    public float getPay() {
        return pay;
    }

    public void setPay(float pay) {
        this.pay = pay;
    }

    public ProjectSalaried getProjectSalaried() {
        return projectSalaried;
    }
}