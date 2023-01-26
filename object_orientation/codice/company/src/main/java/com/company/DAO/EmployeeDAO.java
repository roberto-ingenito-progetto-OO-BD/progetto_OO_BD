package com.company.DAO;


import com.company.Model.EmpType;
import com.company.Model.Employee;
import com.company.Model.Laboratory;
import org.jetbrains.annotations.Nullable;

public interface EmployeeDAO {
    /**
     * @return EmpType se il login è riuscito
     * <p> null altrimenti
     */
    EmpType baseEmpLogin(String email, String password);

    /**
     * @return true se il login è avvenuto correttamente
     * <p> false altrimenti
     */

    Employee getEmployeeData(EmpType empType, String email);

    @Nullable
    Laboratory getWorkingLaboratory(EmpType loggedEmpType, String empCf);
}
