package com.company.DAO;

import com.company.Model.Contract;
import com.company.Model.ProjectSalaried;

import java.util.ArrayList;

/**
 * The interface Project salaried dao.
 */
public interface ProjectSalariedDAO {

    /**
     * Project salaried login boolean.
     * Istanzia una connessione al db ed esegue l'autentificazione.
     * @param email email salvata all'interno della tabella dei project salaried
     * @param password password memorizzata all'interno della tabella
     * @return restituisce un valore booleano che indica se l'autentificazione è andata a buon fine.
     * <p> restituisce falso se l'autentificazione è fallita</p>
     */
    boolean projectSalariedLogin(String email, String password);

    /**
     * Gets project salaried data.
     *
     * @param email email del project salaried che ha effettuato il login.
     * @return recupera le informazioni del project salaried e istanzia un nuovo oggetto del model da restituire alla dashboard del project salaried.
     */
    ProjectSalaried getProjectSalariedData(String email);

    /**
     * Gets contracts.
     *
     * @param projectSalaried istanza del project salaried loggato.
     * @return restituisce un array list di contratti che appartengono al project salaried dato in input.
     * <p> riferimento vuoto se il project salaried non possiede contratti </p>
     */
    ArrayList<Contract> getContracts(ProjectSalaried projectSalaried);

    /**
     * Gets all project salaried.
     *
     * @return restituisce un array list di tutti i project salaried presenti nel database.
     */
    ArrayList<ProjectSalaried> getAllProjectSalaried();
}