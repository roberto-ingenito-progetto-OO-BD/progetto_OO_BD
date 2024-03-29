package com.company.Model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class Employee {
    private final String cf;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String role;
    private final float salary;
    private final EmpType type;

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

    /// CONSTRUCTOR
    public Employee(
            String cf,
            String firstName,
            String lastName,
            String email,
            String role,
            float salary,
            EmpType type
    ) {
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
