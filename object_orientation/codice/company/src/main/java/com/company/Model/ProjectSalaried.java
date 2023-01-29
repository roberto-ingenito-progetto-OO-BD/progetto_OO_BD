package com.company.Model;

import java.time.LocalDate;
import java.util.ArrayList;

public class ProjectSalaried {
    private ArrayList<Contract> contracts = new ArrayList<>();

    private String cf;
    private String firstName;
    private String lastName;
    private String email;
    private String role;

    private LocalDate birthDate;

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
        return role.substring(0, 1).toUpperCase() + role.substring(1);
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
    public void addContract(Contract contract) {
        this.contracts.add(contract);
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /// METHODS
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }
}