package com.company.PostgresDAO;

import com.company.Connection.DatabaseConnection;
import com.company.DAO.ManagerDAO;
import com.company.Model.EmpType;
import com.company.Model.Project;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManagerDAOImplements implements ManagerDAO {
    @Override
    public ArrayList<Project> managerWorkingProjects(String cf, EmpType empType) {
        DatabaseConnection db;
        ResultSet resultSet;
        Project project;
        ArrayList<Project> projects = new ArrayList<>();
        String query = "SELECT * " +
                "FROM project as p , base_emp as b " +
                "WHERE b.cf = p.cf_manager AND b.cf = '" + cf + "'";

        try {
            db = DatabaseConnection.baseEmpInstance(empType);
            resultSet = db.connection.createStatement().executeQuery(query);
            db.connection.close();

            while(resultSet.next()){
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
}
