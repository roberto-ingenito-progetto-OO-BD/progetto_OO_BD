package com.company.DAO;

import com.company.Model.Laboratory;
import com.company.Model.Manager;
import com.company.Model.Senior;

public interface ProjectDAO {
    Laboratory[] getWorkingLaboratories(String cup);
    Senior getProjectReferent(String cup);
    Manager getProjectManager(String cup);
}
