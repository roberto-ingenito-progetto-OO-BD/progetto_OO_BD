package com.company.PostgresDAO;

import com.company.Connection.DatabaseConnection;
import com.company.DAO.ProjectDAO;
import com.company.Model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProjectDAOImplementation implements ProjectDAO {
    @Override
    public Laboratory[] getWorkingLaboratories(String cup) {
        DatabaseConnection db;
        ResultSet rs;
        Laboratory laboratory;
        Laboratory[] laboratories = new Laboratory[3];
        String query = "SELECT laboratory.lab_code , lab_name , topic \n" +
                        "FROM laboratory , take_part , project \n" +
                        "WHERE laboratory.lab_code = take_part.lab_code \n" +
                        "AND take_part.end_date IS NULL AND take_part.cup = project.cup AND take_part.cup = '" + cup + "'";

        try {
            db = DatabaseConnection.ProjAdminInstance();
            rs = db.connection.createStatement().executeQuery(query);
            db.connection.close();

            int i = 0;
            while(rs.next()){
                laboratory = new Laboratory(
                        rs.getInt("lab_code"),
                        rs.getString("lab_name"),
                        rs.getString("topic")
                );
                    laboratories[i] = laboratory;
                    i++;
            }
            return laboratories;
        } catch (SQLException err){
            throw new RuntimeException(err);
        }

    }

    @Override
    public Senior getProjectReferent(String cup) {
        DatabaseConnection db;
        ResultSet resultSet;
        Senior scientificReferent;
        String query = "SELECT * " +
                        "FROM base_emp , project " +
                        "WHERE project.cf_scientific_referent = base_emp.cf AND project.cup =  '" + cup + "'";

        try {
            db = DatabaseConnection.ProjAdminInstance();
            resultSet = db.connection.createStatement().executeQuery(query);
            db.connection.close();
            resultSet.next();

            EmpType currentEmployeeType = switch (resultSet.getString("type")) {
                case "junior" -> EmpType.junior;
                case "middle" -> EmpType.middle;
                case "senior" -> EmpType.senior;
                case "manager" -> EmpType.manager;
                default -> null;
            };

            scientificReferent = new Senior(
                    resultSet.getString("cf"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("email"),
                    resultSet.getString("role"),
                    resultSet.getFloat("salary"),
                    currentEmployeeType
            );


            return scientificReferent;
        } catch (SQLException err) {
            throw new RuntimeException(err);
        }
    }

    @Override
    public Manager getProjectManager(String cup) {
        DatabaseConnection db;
        ResultSet resultSet;
        Manager manager;
        String query = "SELECT * " +
                        "FROM base_emp , project " +
                        "WHERE project.cf_manager = base_emp.cf AND project.cup =  '" + cup + "'";

        try {
            db = DatabaseConnection.ProjAdminInstance();
            resultSet = db.connection.createStatement().executeQuery(query);
            db.connection.close();
            resultSet.next();

            EmpType currentEmployeeType = switch (resultSet.getString("type")) {
                case "junior" -> EmpType.junior;
                case "middle" -> EmpType.middle;
                case "senior" -> EmpType.senior;
                case "manager" -> EmpType.manager;
                default -> null;
            };

            manager = new Manager(
                    resultSet.getString("cf"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("email"),
                    resultSet.getString("role"),
                    resultSet.getFloat("salary"),
                    currentEmployeeType
            );
                return manager;
        } catch (SQLException err) {
            throw new RuntimeException(err);
        }

    }

    @Override
    public ArrayList<Contract> getProjectContracts(String cup) {
        DatabaseConnection db;
        ResultSet resultSet;
        Contract contract;
        ProjectSalaried projectSalaried;
        ArrayList<Contract> contracts = new ArrayList<>();
        String query = "SELECT * FROM project_salaried as ps , project as p , works_on as w WHERE ps.cf = w.cf AND w.cup = p.cup AND p.cup = '" + cup + "'";

        try {
            db = DatabaseConnection.ProjAdminInstance();
            resultSet = db.connection.createStatement().executeQuery(query);
            db.connection.close();

            while(resultSet.next()) {
                contract = new Contract(
                        resultSet.getDate("hire_date").toLocalDate(),
                        resultSet.getDate("expiration").toLocalDate(),
                        resultSet.getFloat("pay")
                );
                projectSalaried = new ProjectSalaried(
                        resultSet.getString("cf"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("role")
                );
                    contract.setProjectSalaried(projectSalaried);
                    contracts.add(contract);
            }
                return contracts;
        } catch (SQLException err) {
            throw new RuntimeException(err);
        }

    }

    @Override
    public ArrayList<EquipmentRequest> getEquipmentRequests(String cup) {
        DatabaseConnection db;
        ResultSet rs;
        ArrayList<EquipmentRequest> equipmentRequests = new ArrayList<>();
        EquipmentRequest equipmentRequest;
        Laboratory laboratory;
        String query = "SELECT * " +
                        "FROM equipment_request as er , project as p , laboratory as l " +
                        "WHERE er.lab_code = l.lab_code AND p.cup = er.cup AND p.cup =  '" + cup + "'";

        try {
            db = DatabaseConnection.ProjAdminInstance();
            rs = db.connection.createStatement().executeQuery(query);
            db.connection.close();

            while(rs.next()){
                // per ogni richiesta salvo anche le informazioni del laboratorio che ne ha fatto richiesta
                equipmentRequest = new EquipmentRequest(
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getString("specs"),
                        rs.getString("type"),
                        rs.getInt("quantity")
                );

                laboratory = new Laboratory(
                                rs.getInt("lab_code"),
                                rs.getString("lab_name"),
                                rs.getString("topic")
                );
                    equipmentRequest.setLaboratory(laboratory);
                    equipmentRequests.add(equipmentRequest);
            }
                return equipmentRequests;
        } catch (SQLException err) {
            throw new RuntimeException(err);
        }

    }
}
