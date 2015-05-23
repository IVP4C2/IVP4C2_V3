package nl.edu.avans.ivp4c2.datastorage;
import nl.edu.avans.ivp4c2.domain.Employee;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author IVP4C2
 */
public class EmployeeDAO {

    public EmployeeDAO () {
        // Nothing to be initialized. This is a stateless class. Constructor
        // has been added to explicitely make this clear.
    }

    /**
     * Tries to find the Employee by the given employeeNumber in the database.
     *
     * @param employeeNumber identifies the employee to be loaded from the database
     * @return the Employee object to be found. In case employee could not be found,
     * null is returned.
     */
    public Employee findEmployee(int employeeNumber) {
        Employee employee = null;

        // First open a database connnection
        DatabaseConnection connection = new DatabaseConnection();
        if (connection.openConnection()) {
            // If a connection was successfully setup, execute the SELECT statement.
            ResultSet resultset = connection.executeSQLSelectStatement(
                    "SELECT employee_id, firstname, lastname FROM employee WHERE employee_id = " + employeeNumber + ";");

            if (resultset != null) {
                try {
                    // The employeeNumber is unique for a employee, so in case the resultset does contain data, we need its first entry.
                    if (resultset.next()) {
                        int employeeNumberFromDb = resultset.getInt("employee_id");
                        String firstNameFromDb = resultset.getString("firstname");
                        String lastNameFromDb = resultset.getString("lastname");

                        employee = new Employee(employeeNumberFromDb, firstNameFromDb, lastNameFromDb);

                    }
                } catch (SQLException e) {
                    System.out.println(e);
                    employee = null;
                }
            }
            // else an error occurred leave 'member' to null.

            // We had a database connection opened. Since we're finished,
            // we need to close it.
            connection.closeConnection();
        }

        return employee;

    }
}
