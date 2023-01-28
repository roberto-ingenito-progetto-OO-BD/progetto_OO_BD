package com.company.DAO;

import com.company.Model.EmpType;
import com.company.Model.Laboratory;
import com.company.Model.Project;

import java.util.ArrayList;

public interface SeniorDAO {
    ArrayList<Project> isReferentProjects(String cf, EmpType empType);

    ArrayList<Laboratory> isManagerLaboratory(String cf, EmpType empType);
}
