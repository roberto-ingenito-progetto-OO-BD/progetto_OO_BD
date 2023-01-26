package com.company.DAO;

import com.company.Model.Employee;
import com.company.Model.Laboratory;
import com.company.Model.Manager;
import com.company.Model.ScientificReferent;

import java.util.ArrayList;

public interface ProjectDAO {
    Laboratory[] getWorkingLaboratories(String cup);
    ScientificReferent getProjectReferent(String cup);
    Manager getProjectManager(String cup);
}
