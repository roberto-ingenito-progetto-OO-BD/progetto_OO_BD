package com.company.DAO;

import com.company.Model.*;

import java.util.ArrayList;

public interface ProjectDAO {
    ArrayList<Laboratory> getWorkingLaboratories(String cup);
    Senior getProjectReferent(String cup);
    Manager getProjectManager(String cup);

    void endProject(String cup, EmpType empType);
    ArrayList<Contract> getProjectContracts(String cup);

    ArrayList<EquipmentRequest> getEquipmentRequests(String cup);

    void hireProjectSalaried(String cup, ProjectSalaried projectSalaried, Contract contract, EmpType empType);
}
