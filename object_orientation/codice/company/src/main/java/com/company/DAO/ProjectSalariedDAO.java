package com.company.DAO;

import com.company.Model.Contract;
import com.company.Model.ProjectSalaried;

import java.util.ArrayList;

public interface ProjectSalariedDAO {

    boolean projectSalariedLogin(String email, String password);

    ProjectSalaried getProjectSalariedData(String email);

    ArrayList<Contract> getContracts(ProjectSalaried projectSalaried);
}