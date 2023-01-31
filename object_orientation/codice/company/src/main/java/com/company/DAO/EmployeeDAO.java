package com.company.DAO;


import com.company.Model.EmpType;
import com.company.Model.Employee;
import com.company.Model.Laboratory;
import org.jetbrains.annotations.Nullable;

/**
 * The interface Employee dao.
 */
public interface EmployeeDAO {
    /**
     * Base emp login emp type.
     * Istanzia una connessione al db ed esegue l'autentificazione.
     * @param email     email salvata all'interno della tabella degli impiegati
     * @param password  password memorizzata (non in chiaro) all'interno della tabella degli impiegati
     * @return EmpType , restituisce il tipo di impiegato se c'è una corrispondenza. Il valore restituito serve per instanziare tutte le prossime connessioni al db e per caricare le informazioni della GUI in maniera diversa.
     * <p> restituisce null se non viene trovato nessun impigato</p>
     */
    EmpType baseEmpLogin(String email, String password);

    /**
     * Gets employee data.
     *
     * @param empType tipo di impiegato che effettua la connessione al db.
     * @param email   email (univoca) per ottenere la tupla corretta dal db.
     * @return restituisce le informazioni dell'impiegato da inserire nel model. Viene eseguita soltanto dopo il login quindi non restituisce mai null.
     */
    Employee getEmployeeData(EmpType empType, String email);

    /**
     * Gets working laboratory.
     *
     * @param loggedEmpType the logged emp type
     * @param empCf         the emp cf (identificatore primario nella tabella impiegato del db)
     * @return restituisce le informazioni del laboratorio da inserire nel model, a cui l'impiegato lavora. <p> Se l'impiegato non lavora in nessun laboratorio restituisce null </p>
     */
    @Nullable
    Laboratory getWorkingLaboratory(EmpType loggedEmpType, String empCf);
}
