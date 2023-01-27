package com.company.Model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;


public class Employee {
    private String cf;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private String role;
    private float salary;
    private EmpType type;

    @Nullable
    private Laboratory laboratory = null;

    // GETTERS
    public String getCf() {
        return cf;
    }

    public String getFirstName() {
        // restituisce firstName capitalizzato
        return firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
    }

    public String getLastName() {
        // restituisce lastName capitalizzato
        return lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getRole() {
        // restituisce il ruolo capitalizzato
        return role.substring(0, 1).toUpperCase() + role.substring(1);
    }

    public float getSalary() {
        return salary;
    }

    public EmpType getType() {
        return type;
    }

    @Nullable
    public Laboratory getLaboratory() {
        return laboratory;
    }

    /// SETTER
    public void setLaboratory(@NotNull Laboratory lab) {
        this.laboratory = lab;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    /// CONSTRUCTOR
    public Employee(String cf, String firstName, String lastName, String email, String role, float salary, EmpType type) {
        this.cf = cf;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.salary = salary;
        this.type = type;
    }

    /// METHODS
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }
}
