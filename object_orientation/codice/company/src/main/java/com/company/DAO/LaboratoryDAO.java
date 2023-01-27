package com.company.DAO;

import com.company.Model.*;

import java.util.ArrayList;

public interface LaboratoryDAO {
    ArrayList<Project> getProjects(Laboratory laboratory, EmpType empType);

    Senior getScientificManager(Laboratory laboratory, EmpType empType);

    ArrayList<Employee> getEmployees(Laboratory laboratory, EmpType empType);
}
