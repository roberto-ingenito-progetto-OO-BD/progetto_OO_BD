package com.company.DAO;

import com.company.Model.EmpType;
import com.company.Model.Project;

import java.util.ArrayList;

public interface ManagerDAO {
    /**
     * @param cf attributo dell'impiegato loggato, necessario alla query.
     * @param empType tipo dell'impiegato loggato, che istanzia la connessione al db.
     * @return Restituisce i progetti in cui quell'impiegato è Manager
     */
    ArrayList<Project> managerWorkingProjects(String cf, EmpType empType);
}
