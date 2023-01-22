package com.company.Model;

import java.util.ArrayList;

public class Manager extends Employee {
    private ArrayList<Project> projects;

    public Manager(String cf, String firstName, String lastName, String email, String role, float salary, EmpType type) {
        super(cf, firstName, lastName, email, role, salary, type);
    }

}
