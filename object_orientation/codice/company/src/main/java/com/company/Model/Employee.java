package com.company.Model;

import java.time.LocalDate;

public class Employee {
    protected String cf;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String role;
    protected LocalDate hireDate;
    protected float salary;
    protected EmpType type;
    private Laboratory laboratory;

    public Employee(String cf, String firstName, String lastName, String email, String role, float salary, EmpType type, LocalDate hireDate) {
        this.cf = cf;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.salary = salary;
        this.type = type;
        this.hireDate = hireDate;
    }

    public void setWorkingLaboratory(Laboratory lab){
        this.laboratory = lab;
    }

    public String getCF() {
        return this.cf;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public EmpType getType() {
        return this.type;
    }

    public String getRole () {
        return this.role;
    }

}
