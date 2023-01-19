package com.company.DAO;


import com.company.Model.EmpType;

public interface LoginDAO {
    /**
     * @return EmpType se il login è riuscito <br/> null altrimenti
     */
    EmpType baseEmpLogin(String email, String password);

    /**
     * @return true se il login è avvenuto correttamente <br/> false altrimenti
     */
    boolean projectSalariedLogin(String email, String password);
}
