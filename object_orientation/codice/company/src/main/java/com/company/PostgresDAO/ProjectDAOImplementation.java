package com.company.PostgresDAO;

import com.company.Connection.DatabaseConnection;
import com.company.DAO.ProjectDAO;
import com.company.Model.*;

import java.sql.ResultSet;
import java.sql.SQLException;

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
}
