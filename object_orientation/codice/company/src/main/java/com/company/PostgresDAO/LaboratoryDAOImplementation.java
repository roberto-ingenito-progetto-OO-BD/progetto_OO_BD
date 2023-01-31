package com.company.PostgresDAO;

import com.company.Connection.DatabaseConnection;
import com.company.DAO.LaboratoryDAO;
import com.company.Model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
            db.connection.close();

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
    public Senior getScientificManager(Laboratory laboratory, EmpType empType) {
        DatabaseConnection db;
        ResultSet resultSet;

        String query = "SELECT BE.cf, BE.first_name, BE.last_name, BE.email, BE.role, BE.salary\n" +
                "FROM base_emp AS BE JOIN laboratory AS L  ON BE.cf = L.cf_scientific_manager\n" +
                "WHERE L.lab_code = " + laboratory.getLabCode();

        try {
            db = DatabaseConnection.baseEmpInstance(empType);
            resultSet = db.connection.createStatement().executeQuery(query);
            resultSet.next();
            db.connection.close();

            // chiama il costruttore di employee passando i dati ottenuti dal db
            return new Senior(
                    resultSet.getString("cf"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("email"),
                    resultSet.getString("role"),
                    resultSet.getFloat("salary")
            );

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Employee> getEmployees(Laboratory laboratory, EmpType empType) {
        DatabaseConnection db;
        ArrayList<Employee> employees = new ArrayList<>();
        ResultSet resultSet;

        String query = "SELECT * \n" +
                "FROM base_emp AS BE  JOIN  works_at AS W  ON BE.cf = W.cf_base_emp \n" +
                "WHERE W.end_date IS NULL  AND  W.lab_code = " + laboratory.getLabCode();

        try {
            db = DatabaseConnection.baseEmpInstance(empType);
            resultSet = db.connection.createStatement().executeQuery(query);
            db.connection.close();

            while (resultSet.next()) {
                Employee employee;

                EmpType employeeType = switch (resultSet.getString("type")) {
                    case "junior" -> EmpType.junior;
                    case "middle" -> EmpType.middle;
                    case "senior" -> EmpType.senior;
                    case "manager" -> EmpType.manager;
                    default -> null;
                };

                employee = new Employee(
                        resultSet.getString("cf"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("role"),
                        resultSet.getFloat("salary"),
                        employeeType
                );

                employees.add(employee);
            }

            return employees;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Equipment> getEquipment(Laboratory laboratory, EmpType empType) {
        DatabaseConnection db;
        ArrayList<Equipment> equipments = new ArrayList<>();
        ResultSet resultSet;

        String query = "SELECT E.name, E.type, E.tech_specs, P.price\n" +
                "FROM equipment AS E, purchase AS P\n" +
                "WHERE E.code = P.equipment_code AND E.lab_code = " + laboratory.getLabCode();

        try {
            db = DatabaseConnection.baseEmpInstance(empType);
            resultSet = db.connection.createStatement().executeQuery(query);
            db.connection.close();

            while (resultSet.next()) {
                Equipment equipment = new Equipment(
                        resultSet.getString("name"),
                        resultSet.getString("type"),
                        resultSet.getString("tech_specs"),
                        resultSet.getFloat("price"),
                        resultSet.getDate("purchase_date").toLocalDate()
                );

                equipments.add(equipment);
            }

            return equipments;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int leaveProject(int labCode, String projectCUP, EmpType empType) {
        DatabaseConnection db;
        String query = "UPDATE take_part " +
                "SET end_date = '" + LocalDate.now() + "' " +
                "WHERE cup = '" + projectCUP + "' AND  lab_code = " + labCode + " AND end_date IS NULL";

        try {
            db = DatabaseConnection.baseEmpInstance(empType);
            int updatedRowNumber =  db.connection.createStatement().executeUpdate(query);
            db.connection.close();

            return updatedRowNumber;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void joinProject(int labCode, String projectCUP, EmpType empType) {
        DatabaseConnection db;
        String query = "INSERT INTO take_part (start_date, end_date, cup, lab_code) " +
                "VALUES ( '" + LocalDate.now() + "', null, '" + projectCUP + "', " + labCode + " )";

        try {
            db = DatabaseConnection.baseEmpInstance(empType);
            db.connection.createStatement().execute(query);
            db.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
