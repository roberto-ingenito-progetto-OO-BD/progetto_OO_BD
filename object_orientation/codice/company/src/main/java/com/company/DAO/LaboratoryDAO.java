package com.company.DAO;

import com.company.Model.*;

import java.util.ArrayList;

/**
 * The interface Laboratory dao.
 */
public interface LaboratoryDAO {
    /**
     * Gets projects.
     *
     * @param laboratory il laboratorio di cui si vogliono reperire i progetti a cui lavora.
     * @param empType    il tipo di impiegato che esegue questa funzione (se è un junior o un middle i fondi non vengono caricati nel model dei progetti)
     * @return restituisce tutti i progetti a cui lavora il laboratorio. <p> restituisce un riferimento vuoto se non ci sono progetti a cui il laboratorio lavora </p>
     */
    ArrayList<Project> getProjects(Laboratory laboratory, EmpType empType);

    /**
     * Gets scientific manager.
     *
     * @param laboratory il laboratorio di cui si vuole reperire lo scientific Manager.
     * @param empType    il tipo di impiegato con cui instanziare la connessione al db.
     * @return restituisce un istanza di Senior , in particolare il Senior che è scientific Manager del laboratorio passato come parametro.
     */
    Senior getScientificManager(Laboratory laboratory, EmpType empType);

    /**
     * Gets employees.
     *
     * @param laboratory il laboratorio di cui si vogliono reperire tutti gli impiegati afferenti.
     * @param empType    il tipo di impiegato che istanzia la connessione al db.
     * @return restituisce l'array list degli impiegati che lavorano a quel laboratorio. <p> se nessun impiegato lavora , viene restituito un riferimento vuoto </p>
     */
    ArrayList<Employee> getEmployees(Laboratory laboratory, EmpType empType);

    /**
     * Gets equipment.
     *
     * @param laboratory il laboratorio di cui si vogliono reperire tutte le attrezzature possedute.
     * @param empType    il tipo di impiegato che istanzia la connessione al db.
     * @return restituisce l'array list di attrezzature (Equipment) che appartengono al laboratorio. <p> se il laboratorio non ha attrezzature restituisce un array list vuoto </p>
     */
    ArrayList<Equipment> getEquipment(Laboratory laboratory, EmpType empType);

    /**
     * Leave project int.
     *
     * @param labCode    Laboratorio che vuole abbandonare il progetto
     * @param projectCUP Progetto che vuole abbandonare il laboratorio
     * @param empType    Tipo dell'impiegato loggato che istanzia la connessione al db, solo un senior ha i permessi per portare a termine quest'operazione sul db.
     * @return Restituisce la quantità di tuple aggiornate, in particolare se restituisce 0 allora non è stato abbandonato il progetto
     */
    int leaveProject(int labCode, String projectCUP, EmpType empType);

    /**
     * Join project.
     *
     * @param labCode    Laboratorio che vuole partecipare al progetto
     * @param projectCUP Progetto a cui vuole partecipare il laboratorio
     * @param empType    Tipo dell'impiegato loggato che istanzia la connessione al db, solo un senior ha i permessi per portare a termine quest'operazione sul db.
     */
    void joinProject(int labCode, String projectCUP, EmpType empType);

    void equipmentRequest(EquipmentRequest equipmentRequest);
}
