package com.company.PostgresDAO;

import com.company.Connection.DatabaseConnection;
import com.company.DAO.EmployeeDAO;
import com.company.Model.EmpType;
import com.company.Model.Employee;
import com.company.Model.Laboratory;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDAOImplementation implements EmployeeDAO {
    @Override
    public EmpType baseEmpLogin(String email, String password) {
        try {
            DatabaseConnection db = DatabaseConnection.getInstance("login_user", "login");
            ResultSet resultSet = db.connection.createStatement().executeQuery("select base_emp_login('" + email + "', '" + password + "')");
            db.connection.close();

            resultSet.next();
            String baseEmpLogin = resultSet.getString("base_emp_login");

            if (baseEmpLogin == null) return null;

            return switch (baseEmpLogin) {
                case "junior" -> EmpType.junior;
                case "middle" -> EmpType.middle;
                case "senior" -> EmpType.senior;
                case "manager" -> EmpType.manager;
                default -> null;
            };
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean projectSalariedLogin(String email, String password) {
        // TODO: Implementare dashboard del salariato a progetto
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
    public Employee getEmployeeData(EmpType empType, String email) {
        String query = "SELECT * FROM base_emp WHERE email = '" + email + "'";
        Employee loggedEmployee;
        try {
            DatabaseConnection db = DatabaseConnection.baseEmpInstance(empType);
            ResultSet resultSet = db.connection.createStatement().executeQuery(query);
            db.connection.close();

            resultSet.next();

            EmpType currentEmployeeType = switch (resultSet.getString("type")) {
                case "junior" -> EmpType.junior;
                case "middle" -> EmpType.middle;
                case "senior" -> EmpType.senior;
                case "manager" -> EmpType.manager;
                default -> null;
            };

            // chiamare il costruttore di employee passando i dati ottenuti dal db
            loggedEmployee = new Employee(
                    resultSet.getString("cf"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("email"),
                    resultSet.getString("role"),
                    resultSet.getFloat("salary"),
                    currentEmployeeType
            );

            return loggedEmployee;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override @Nullable
    public Laboratory getWorkingLaboratory(EmpType loggedEmpType, String empCf) {
        String query = "SELECT L.lab_code, L.lab_name, L.topic\n" +
                "FROM works_at AS W   NATURAL JOIN   laboratory AS L\n" +
                "WHERE W.cf_base_emp = '" + empCf + "' AND\n" +
                "W.end_date IS NULL;";
        ResultSet resultSet;
        DatabaseConnection db;

        try {
            db = DatabaseConnection.baseEmpInstance(loggedEmpType);
            resultSet = db.connection.createStatement().executeQuery(query);
            db.connection.close();

            if(!resultSet.isBeforeFirst()) return null;

            resultSet.next();

            return new Laboratory(
                    resultSet.getInt("lab_code"),
                    resultSet.getString("lab_name"),
                    resultSet.getString("topic")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

}
