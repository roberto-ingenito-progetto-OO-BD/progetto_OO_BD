package com.company.Model;

import java.time.LocalDate;
import java.util.ArrayList;

public class ScientificManager extends Employee {
    private ArrayList<Laboratory> laboratories;

    public ScientificManager(String cf, String firstName, String lastName, String email, String role, float salary, EmpType type, LocalDate hireDate) {
        super(cf, firstName, lastName, email, role, salary, type, hireDate);
    }

}
