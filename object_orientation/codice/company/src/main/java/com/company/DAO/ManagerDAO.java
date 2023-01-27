package com.company.DAO;

import com.company.Model.EmpType;
import com.company.Model.Project;

import java.util.ArrayList;

public interface ManagerDAO {
    ArrayList<Project> isManagerProjects(String cf, EmpType empType);
}
