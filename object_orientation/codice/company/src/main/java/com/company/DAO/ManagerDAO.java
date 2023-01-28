package com.company.DAO;

import com.company.Model.EmpType;
import com.company.Model.Project;

import java.util.ArrayList;

public interface ManagerDAO {
    /**
     * @return Restituisce i progetti in cui quell'impiegato è Manager
     */
    ArrayList<Project> managerWorkingProjects(String cf, EmpType empType);
}
