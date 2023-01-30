package com.company.Model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Senior extends Employee {
    private ArrayList<Laboratory> laboratories = new ArrayList<>();
    private ArrayList<Project> projects = new ArrayList<>();

    /// CONSTRUCTOR
    public Senior(
            String cf,
            String firstName,
            String lastName,
            String email,
            String role,
            float salary
    ) {
        super(cf, firstName, lastName, email, role, salary, EmpType.senior);
    }

    public Senior(Employee employee) {
        super(
                employee.getCf(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getRole(),
                employee.getSalary(),
                employee.getType()
        );
    }


    /**
     * @return Restituisce i laboratori in cui lui è manager
     */
    /// GETTERS
    public ArrayList<Laboratory> getLaboratories() {
        return laboratories;
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public void setLaboratories(@NotNull ArrayList<Laboratory> laboratories) {
        this.laboratories = laboratories;
    }

    public void setProjects(@NotNull ArrayList<Project> projects) {
        this.projects = projects;
    }
}
