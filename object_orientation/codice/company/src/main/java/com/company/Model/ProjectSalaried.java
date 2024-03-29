package com.company.Model;

import java.time.LocalDate;
import java.util.ArrayList;

public class ProjectSalaried {
    private ArrayList<Contract> contracts = new ArrayList<>();

    private final String cf;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String role;

    private final LocalDate birthDate;

    /// CONSTRUCTOR
    public ProjectSalaried(String cf, String firstName, String lastName, String email, String role, LocalDate birthDate) {
        this.cf = cf;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.birthDate = birthDate;
    }


    /// GETTER
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
        return role.substring(0, 1).toUpperCase() + role.substring(1).toLowerCase();
    }

    public LocalDate getBirthDate() {return birthDate;}

    public String getCf() {
        return cf;
    }

    public String getEmail() {
        return email;
    }

    /// SETTER
    public void setContracts(ArrayList<Contract> contracts) {
        this.contracts = contracts;
    }

    /// METHODS
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public void addContract(Contract contract) {
        this.contracts.add(contract);
    }
}