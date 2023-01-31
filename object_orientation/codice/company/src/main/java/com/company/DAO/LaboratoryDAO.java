package com.company.DAO;

import com.company.Model.*;

import java.util.ArrayList;

public interface LaboratoryDAO {
    ArrayList<Project> getProjects(Laboratory laboratory, EmpType empType);

    Senior getScientificManager(Laboratory laboratory, EmpType empType);

    ArrayList<Employee> getEmployees(Laboratory laboratory, EmpType empType);

    ArrayList<Equipment> getEquipment(Laboratory laboratory, EmpType empType);

    void equipmentRequest(EquipmentRequest equipmentRequest);

    /**
     * @param labCode    Laboratorio che vuole abbandonare il progetto
     * @param projectCUP Progetto che vuole abbandonare il laboratorio
     * @param empType    Tipo dell'impiegato loggato, solo un senior ha i permessi per fare ciò
     * @return Restituisce la quantità di tuple aggiornate,
     * in particolare se restituisce 0 allora non è stato abbandonato il progetto
     */
    int leaveProject(int labCode, String projectCUP, EmpType empType);

    /**
     * @param labCode    Laboratorio che vuole partecipare al progetto
     * @param projectCUP Progetto a cui vuole partecipare il laboratorio
     * @param empType    Tipo dell'impiegato loggato, solo un senior ha i permessi per fare ciò
     */
    void joinProject(int labCode, String projectCUP, EmpType empType);
}
