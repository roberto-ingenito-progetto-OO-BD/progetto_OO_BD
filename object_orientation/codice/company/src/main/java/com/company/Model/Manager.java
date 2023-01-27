package com.company.Model;

import java.util.ArrayList;

public class Manager extends Employee {


    private ArrayList<Project> projects;

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }

    /// CONSTRUCTOR
    public Manager(
            String cf,
            String firstName,
            String lastName,
            String email,
            String role,
            float salary,
            EmpType type
    ) {
        super(cf, firstName, lastName, email, role, salary, type);
    }

    public Manager(Employee employee) {
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

    /// GETTER
    public ArrayList<Project> getProjects() {
        return projects;
    }

}
