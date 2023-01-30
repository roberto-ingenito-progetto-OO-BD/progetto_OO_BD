package com.company.PostgresDAO;

import com.company.Connection.DatabaseConnection;
import com.company.DAO.SeniorDAO;
import com.company.Model.EmpType;
import com.company.Model.Laboratory;
import com.company.Model.Project;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SeniorDAOImplementation implements SeniorDAO {
    @Override
    public ArrayList<Project> isReferentProjects(String cf, EmpType empType) {
        DatabaseConnection db;
        ResultSet resultSet;
        Project project;
        ArrayList<Project> projects = new ArrayList<>();
        String query = "SELECT * " +
                "FROM project as p , base_emp as b " +
                "WHERE b.cf = p.cf_scientific_referent AND b.cf = '" + cf + "'";

        try {
            db = DatabaseConnection.baseEmpInstance(empType);
            resultSet = db.connection.createStatement().executeQuery(query);

            while (resultSet.next()) {
                project = new Project(
                        resultSet.getString("cup"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getDate("start_date").toLocalDate(),
                        resultSet.getDate("end_date") == null ? null : resultSet.getDate("start_date").toLocalDate(),
                        resultSet.getDate("deadline") == null ? null : resultSet.getDate("start_date").toLocalDate()
                );
                projects.add(project);
            }
            return projects;
        } catch (SQLException err) {
            throw new RuntimeException(err);
        }
    }

    @Override
    public ArrayList<Laboratory> isManagerLaboratory(String cf, EmpType empType) {
        DatabaseConnection db;
        ResultSet resultSet;
        Laboratory laboratory;
        ArrayList<Laboratory> laboratories = new ArrayList<>();

        String query = "SELECT lab_code, lab_name, topic " +
                "FROM laboratory " +
                "WHERE cf_scientific_manager = '" + cf + "'";

        try {
            db = DatabaseConnection.baseEmpInstance(empType);
            resultSet = db.connection.createStatement().executeQuery(query);
            db.connection.close();

            while (resultSet.next()) {
                laboratory = new Laboratory(
                        resultSet.getInt("lab_code"),
                        resultSet.getString("lab_name"),
                        resultSet.getString("topic")
                );
                laboratories.add(laboratory);
            }

            return laboratories;
        } catch (SQLException err) {
            throw new RuntimeException(err);
        }
    }
}
