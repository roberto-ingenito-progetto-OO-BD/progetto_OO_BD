package com.company.DAO;


import com.company.Model.EmpType;

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
    boolean projectSalariedLogin(String email, String password);
}
