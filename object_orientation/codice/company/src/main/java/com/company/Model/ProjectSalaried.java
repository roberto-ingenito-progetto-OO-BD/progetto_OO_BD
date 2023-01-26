package com.company.Model;

import java.util.ArrayList;

public class ProjectSalaried {
    private ArrayList<Contract> contracts;

    private String cf;
    private String firstName;
    private String lastName;
    private String email;
    private String role;

    public ProjectSalaried(String cf, String firstName, String lastName, String email, String role) {
        this.cf = cf;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }

    public void setContracts(ArrayList<Contract> contracts) {
        this.contracts = contracts;
    }

    public ArrayList<Contract> getContracts() {
        return contracts;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRole() {
        // restituisce il ruolo con la prima lettera in maiuscolo
        return role.substring(0, 1).toUpperCase() + role.substring(1);
    }

    public String getCf() {
        return cf;
    }

}