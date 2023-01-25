package com.company.PostgresDAO;

import com.company.Connection.DatabaseConnection;
import com.company.DAO.LaboratoryDAO;
import com.company.Model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LaboratoryDAOImplementation implements LaboratoryDAO {
    @Override
    public ArrayList<Project> getProjects(Laboratory laboratory, EmpType empType) {
        DatabaseConnection db;
        ArrayList<Project> projects = new ArrayList<>();
        ResultSet resultSet;

        String query = switch (empType) {
            case junior, middle -> // non puo vedere i fondi e le foreign keys
                    "SELECT P.cup, P.name, P.description, P.start_date, P.end_date, P.deadline \n";
            case senior, manager ->
                    "SELECT P.cup, P.funds, P.name, P.description, P.start_date, P.end_date, P.deadline, P.cf_manager, P.cf_scientific_referent \n";
        };

        query = query +
                "FROM take_part AS T  JOIN  project AS P  ON T.cup = P.cup \n" +
                "WHERE T.end_date IS NULL AND T.lab_code = " + laboratory.getLabCode();

        try {
            db = DatabaseConnection.baseEmpInstance(empType);
            resultSet = db.connection.createStatement().executeQuery(query);

            while (resultSet.next()) {
                Project project = new Project(
                        resultSet.getString("cup"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getDate("start_date").toLocalDate(),
                        resultSet.getDate("end_date") == null ? null : resultSet.getDate("start_date").toLocalDate(),
                        resultSet.getDate("deadline") == null ? null : resultSet.getDate("start_date").toLocalDate()
                );

                if (empType == EmpType.senior || empType == EmpType.manager)
                    project.setFunds(resultSet.getFloat("funds"));

                projects.add(project);
            }

            return projects;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ScientificManager getScientificManager(Laboratory laboratory, EmpType empType) {
        return null;
    }

    @Override
    public ArrayList<Employee> getEmployees(Laboratory laboratory, EmpType empType) {
        return null;
    }
}
