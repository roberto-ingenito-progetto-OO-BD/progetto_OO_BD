package com.company.DAO;

import com.company.Model.*;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * The interface Project dao.
 */
public interface ProjectDAO {
    /**
     * Gets working laboratories.
     *
     * @param cup attributo identificante (lato db) del progetto
     * @return restituisce l'array list di laboratori che lavorano al progetto attualmente <p> riferimento vuoto se non lavora nessun laboratorio </p>
     */
    ArrayList<Laboratory> getWorkingLaboratories(String cup);

    /**
     * Gets project referent.
     *
     * @param cup attributo identificante (lato db) del progetto
     * @return restituisce il Senior che è referente del progetto
     */
    Senior getProjectReferent(String cup);

    /**
     * Gets project manager.
     *
     * @param cup attributo identificante (lato db) del progetto
     * @return restituisce il Manager che è manager del progetto
     */
    Manager getProjectManager(String cup);

    /**
     * End project.
     *
     * @param cup     attributo identificante (lato db) del progetto
     * @param empType il tipo di impiegato che sta eseguendo l'operazione. Solo un Manager può concludere un progetto
     */
    void endProject(String cup, EmpType empType);

    /**
     * Gets project contracts.
     *
     * @param cup attributo identificante (lato db) del progetto
     * @return restituisce l'array list di contratti del progetto. <p> riferimento vuoto se non ci sono contratti appartenenti al progetto </p>
     */
    ArrayList<Contract> getProjectContracts(String cup);

    /**
     * Gets equipment requests.
     *
     * @param cup attributo identificante (lato db) del progetto
     * @return restituisce l'array list di richieste di attrezzatura fatte a quel progetto dai vari laboratori <p> riferimento vuoto se non ci sono richieste per quel progetto </p>
     */
    ArrayList<EquipmentRequest> getEquipmentRequests(String cup);

    /**
     * Hire project salaried.
     *
     * @param cup             attributo identificante (lato db) del progetto
     * @param projectSalaried riferimento al project salaried che si vuole assumere
     * @param contract        riferimento al contratto con cui viene assunto quel project salaried
     * @param empType         tipo di impiegato loggato che effettua la connessione al db (soltanto un Senior o un Manager possono assumere)
     */
    void hireProjectSalaried(String cup, ProjectSalaried projectSalaried, Contract contract, EmpType empType);

    /**
     * Remaining project salaried funds big decimal.
     *
     * @param cup attributo identificante (lato db) del progetto
     * @return restituisce il valore numerico che rappresenta i fondi rimasti per assumere project salaried
     */
    BigDecimal remainingProjectSalariedFunds(String cup);


    /**
     * Remaining equipment funds big decimal.
     *
     * @param cup attributo identificante (lato db) del progetto
     * @return restituisce il valore numerico che rappresenta i fondi rimasti per soddisfare le richieste di attrezzatura
     */
    BigDecimal remainingEquipmentFunds(String cup);

    /**
     * Gets available projects.
     *
     * @return Restituisce l'array list di tutti i progetti a cui stanno lavorando meno di tre laboratori (numero massimo consentito) <p> riferimento vuoto tutti i progetti sono al limite della partecipazione </p>
     */
    ArrayList<Project> getAvailableProjects();

    /**
     * Gets buyed equipments.
     * Imposta il riferimento all'array list di tutte le attrezzature acquistate dal progetto in ingresso per il laboratorio in ingresso.
     * <p> riferimento vuoto se non vi sono attrezzature acquistate </p>
     *
     * @param project    attributo identificante (lato db) del progetto
     * @param laboratory riferimeto al laboratorio che partecipa al progetto
     */
    void getBuyedEquipments(Project project, Laboratory laboratory);

    /**
     * Buy equipment.
     * Acquisto attrezzatura richiesta da parte del progetto. Eliminazione della richiesta e creazione nel model di tutte le attrezzature acquistate.
     *
     * @param project          riferimento al progetto
     * @param equipmentRequest riferimento alla richiesta di attrezzatura
     * @param price            prezzo per unita di attrezzatura stabilito nella GUI
     */
    void buyEquipment(Project project, EquipmentRequest equipmentRequest, Float price);
}
