package com.company.PostgresDAO;

import com.company.Connection.DatabaseConnection;
import com.company.DAO.ProjectSalariedDAO;
import com.company.Model.Contract;
import com.company.Model.Project;
import com.company.Model.ProjectSalaried;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProjectSalariedDAOImplementation implements ProjectSalariedDAO {

    @Override
    public boolean projectSalariedLogin(String email, String password) {
        try {
            DatabaseConnection db = DatabaseConnection.getInstance("login_user", "login");
            ResultSet resultSet = db.connection.createStatement().executeQuery("select project_salaried_login('" + email + "', '" + password + "')");
            db.connection.close();

            resultSet.next();
            return resultSet.getString("project_salaried_login").equals("t");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProjectSalaried getProjectSalariedData(String email) {
        String query = "SELECT cf , first_name, last_name, email, role FROM project_salaried WHERE email = '" + email + "'";
        ProjectSalaried loggedProjectSalaried;
        DatabaseConnection db;
        ResultSet rs;
        try {

            db = DatabaseConnection.projSalariedInstance();
            rs = db.connection.createStatement().executeQuery(query);
            db.connection.close();

            rs.next();

            loggedProjectSalaried = new ProjectSalaried(
                    rs.getString("cf"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("role")
            );

            return loggedProjectSalaried;

        } catch (SQLException err) {
            throw new RuntimeException(err);
        }
    }

    @Override
    public ArrayList<Contract> getContracts(ProjectSalaried projectSalaried) {
        ArrayList<Contract> contracts = new ArrayList<>();
        Contract contract;
        Project project;
        DatabaseConnection db;
        ResultSet rs;
        String query;

        query = "SELECT w.pay, w.hire_date, w.expiration, p.cup, p.name, p.funds, p.description, p.start_date, p.end_date, p.deadline" +
                " FROM works_on as w, project as p " +
                "WHERE w.cup = p.cup AND w.cf = '" + projectSalaried.getCf() + "'";

        try {
            db = DatabaseConnection.projSalariedInstance();
            rs = db.connection.createStatement().executeQuery(query);
            db.connection.close();

            // scorrere il resultSet
            while (rs.next()) {

                // per ogni contratto chiama il costruttore
                contract = new Contract(
                        rs.getDate("hire_date").toLocalDate(),
                        rs.getDate("expiration").toLocalDate(),
                        rs.getFloat("pay")
                );

                // crea nuova istanza di progetto
                project = new Project(
                        rs.getString("cup"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date") == null ? null : rs.getDate("end_date").toLocalDate(),
                        rs.getDate("deadline") == null ? null : rs.getDate("deadline").toLocalDate()
                );

                // inserisce in contratto il riferimento a project
                contract.setProject(project);
                contract.setProjectSalaried(projectSalaried);

                // inserisce nell'array di contratti ogni contratto ottenuto dal resulSet
                contracts.add(contract);
            }

            return contracts;
        } catch (SQLException err) {
            throw new RuntimeException(err);
        }
    }

}