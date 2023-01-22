package com.company.Model;

import java.time.LocalDate;
import java.util.ArrayList;

public class ScientificReferent extends Employee {
    private ArrayList<Project> projects;

    public ScientificReferent(String cf, String firstName, String lastName, String email, String role, float salary, EmpType type, LocalDate hireDate) {
        super(cf, firstName, lastName, email, role, salary, type, hireDate);
    }

}
