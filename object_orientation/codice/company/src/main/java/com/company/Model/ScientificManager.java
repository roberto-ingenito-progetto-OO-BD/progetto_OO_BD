package com.company.Model;

import java.util.ArrayList;

public class ScientificManager extends Employee {
    private ArrayList<Laboratory> laboratories;

    public ScientificManager(String cf, String firstName, String lastName, String email, String role, float salary, EmpType type) {
        super(cf, firstName, lastName, email, role, salary, type);
    }
}
