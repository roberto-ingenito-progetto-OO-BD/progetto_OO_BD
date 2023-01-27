package com.company.Model;

import java.util.ArrayList;

public class Senior extends Employee {
    private ArrayList<Laboratory> laboratories;
    private ArrayList<Project> projects;

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


    /// GETTERS
    public ArrayList<Laboratory> getLaboratories() {
        return laboratories;
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public void setLaboratories(ArrayList<Laboratory> laboratories) {
        this.laboratories = laboratories;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }
}
