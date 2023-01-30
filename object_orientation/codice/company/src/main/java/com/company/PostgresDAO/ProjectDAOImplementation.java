package com.company.PostgresDAO;

import com.company.Connection.DatabaseConnection;
import com.company.DAO.ProjectDAO;
import com.company.Model.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ProjectDAOImplementation implements ProjectDAO {
    @Override
    public ArrayList<Laboratory> getWorkingLaboratories(String cup) {
        DatabaseConnection db;
        ResultSet rs;
        Laboratory laboratory;
        ArrayList<Laboratory> laboratories = new ArrayList<>();
        String query = "SELECT laboratory.lab_code , lab_name , topic \n" +
                "FROM laboratory , take_part , project \n" +
                "WHERE laboratory.lab_code = take_part.lab_code \n" +
                "AND take_part.end_date IS NULL AND take_part.cup = project.cup AND take_part.cup = '" + cup + "'";

        try {
            db = DatabaseConnection.ProjAdminInstance();
            rs = db.connection.createStatement().executeQuery(query);
            db.connection.close();

            int i = 0;
            while (rs.next()) {
                laboratory = new Laboratory(
                        rs.getInt("lab_code"),
                        rs.getString("lab_name"),
                        rs.getString("topic")
                );
                laboratories.add(laboratory);
                i++;
            }
            return laboratories;
        } catch (SQLException err) {
            throw new RuntimeException(err);
        }

    }

    @Override
    public Senior getProjectReferent(String cup) {
        DatabaseConnection db;
        ResultSet resultSet;
        String query = "SELECT * " +
                "FROM base_emp AS BE  JOIN  project AS P  ON P.cf_scientific_referent = BE.cf " +
                "WHERE P.cup =  '" + cup + "'";

        try {
            db = DatabaseConnection.ProjAdminInstance();
            resultSet = db.connection.createStatement().executeQuery(query);
            db.connection.close();
            resultSet.next();

            return new Senior(
                    resultSet.getString("cf"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("email"),
                    resultSet.getString("role"),
                    resultSet.getFloat("salary")
            );
        } catch (SQLException err) {
            throw new RuntimeException(err);
        }
    }

    @Override
    public Manager getProjectManager(String cup) {
        DatabaseConnection db;
        ResultSet resultSet;
        String query = "SELECT * " +
                "FROM base_emp , project " +
                "WHERE project.cf_manager = base_emp.cf AND project.cup =  '" + cup + "'";

        try {
            db = DatabaseConnection.ProjAdminInstance();
            resultSet = db.connection.createStatement().executeQuery(query);
            db.connection.close();
            resultSet.next();

            return new Manager(
                    resultSet.getString("cf"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("email"),
                    resultSet.getString("role"),
                    resultSet.getFloat("salary")
            );
        } catch (SQLException err) {
            throw new RuntimeException(err);
        }

    }

    @Override
    public void endProject(String cup, EmpType empType) {
        DatabaseConnection db;
        PreparedStatement sts;
        LocalDate localDate = LocalDate.now();
        Date currentDate = Date.valueOf(localDate);
        String query = "UPDATE project SET end_date = '" + currentDate + "'" + " WHERE cup = '" + cup + "'";

        try {
                db = DatabaseConnection.baseEmpInstance(empType);
                sts = db.connection.prepareStatement(query);
                sts.executeUpdate();
                db.connection.close();
        } catch (SQLException err) {
            throw new RuntimeException(err);
        }
    }

    @Override
    public void hireProjectSalaried(String cup, ProjectSalaried projectSalaried, Contract contract, EmpType empType) {
        DatabaseConnection db;
        PreparedStatement sts;
        String query = "call hire_project_salaried(?,?,?,?,?,?,?,?,?,?,?)";


        try {
            db = DatabaseConnection.baseEmpInstance(empType);
            sts = db.connection.prepareCall(query);
            sts.setString(1, projectSalaried.getCf());
            sts.setString(2, projectSalaried.getFirstName());
            sts.setString(3, projectSalaried.getLastName());
            sts.setString(4, projectSalaried.getEmail());
            sts.setString(5, "password");
            sts.setString(6, projectSalaried.getRole());
            sts.setDate(7, Date.valueOf(projectSalaried.getBirthDate()));
            sts.setBigDecimal(8, BigDecimal.valueOf(contract.getPay()));
            sts.setDate(9, Date.valueOf(contract.getHireDate()));
            sts.setDate(10, Date.valueOf(contract.getExpiration()));
            sts.setString(11, cup);
            sts.getParameterMetaData();
            sts.execute();
            db.connection.close();
        } catch (SQLException err) {
            throw new RuntimeException(err);
        }
    }

    @Override
    public BigDecimal remainingProjectSalariedFunds(String cup) {
        DatabaseConnection db;
        ResultSet rs;
        BigDecimal returnValue;
        String query = "SELECT * FROM remaining_project_funds WHERE cup = '" + cup  + "'";

        try {
            db = DatabaseConnection.baseEmpInstance(EmpType.senior);
            rs = db.connection.createStatement().executeQuery(query);
            db.connection.close();
            returnValue  = rs.getBigDecimal("funds_to_hire");
            System.out.println(returnValue);
            return returnValue;
        } catch (SQLException err) {
            throw new RuntimeException(err);
        }
    }

    @Override
    public float remainingEquipmentFunds(String cup) {
        return 0;
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
                        resultSet.getString("role"),
                        resultSet.getDate("birth_date").toLocalDate()
                );
                    contract.setProjectSalaried(projectSalaried);
                    projectSalaried.addContract(contract);
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
