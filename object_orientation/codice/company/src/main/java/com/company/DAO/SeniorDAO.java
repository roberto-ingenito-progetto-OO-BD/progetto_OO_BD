package com.company.DAO;

import com.company.Model.EmpType;
import com.company.Model.Laboratory;
import com.company.Model.Project;

import java.util.ArrayList;

/**
 * The interface Senior dao.
 */
public interface SeniorDAO {
    /**
     * Is referent projects array list.
     *
     * @param cf      attributo identificante (lato db) dell'impiegato loggato
     * @param empType tipo dell'impiegato loggato, necessario ad istanziare il tipo di connessione giusta al db.
     * @return restituisce l'array list dei progetti di cui è referente scientifico. <p> riferimento vuoto se non è referente di nessun progetto </p>
     */
    ArrayList<Project> isReferentProjects(String cf, EmpType empType);

    /**
     * Is manager laboratory array list.
     *
     * @param cf      attributo identificante (lato db) dell'impiegato loggato
     * @param empType tipo dell'impiegato loggato, necessario ad istanziare il tipo di connessione giusta al db.
     * @return restituisce l'array list dei laboratori di cui è manager scientifico. <p> riferimento vuoto se non è manager di nessun laboratorio
     */
    ArrayList<Laboratory> isManagerLaboratory(String cf, EmpType empType);
}
