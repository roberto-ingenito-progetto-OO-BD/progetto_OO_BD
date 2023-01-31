package com.company.Model;

import org.jetbrains.annotations.Nullable;
import java.time.LocalDate;

public class Contract {
    private final LocalDate hireDate;
    private final LocalDate expiration;
    private final float pay;
    private ProjectSalaried projectSalaried;
    private Project project;

    /// CONSTRUCTOR
    public Contract(LocalDate hireDate, LocalDate expiration, float pay) {
        this.hireDate = hireDate;
        this.expiration = expiration;
        this.pay = pay;
    }

    /// GETTER
    public LocalDate getHireDate() {
        return hireDate;
    }

    @Nullable public LocalDate getExpiration() {
        return expiration;
    }

    public Project getProject() {
        return project;
    }

    public float getPay() {
        return pay;
    }

    public ProjectSalaried getProjectSalaried() {
        return projectSalaried;
    }

    /// SETTER
    public void setProject(Project project) {
        this.project = project;
    }

    public void setProjectSalaried (ProjectSalaried projectSalaried) {
        this.projectSalaried =  projectSalaried;
    }


}