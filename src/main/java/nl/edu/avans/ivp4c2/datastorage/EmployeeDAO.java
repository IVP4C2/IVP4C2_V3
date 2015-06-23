package nl.edu.avans.ivp4c2.datastorage;

import nl.edu.avans.ivp4c2.domain.Employee;
import nl.edu.avans.ivp4c2.domain.NoDBConnectionException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

/**
 *Database Access Object for the Employee class. Handles all operation regarding the Employee class in which a database is used
 * @author IVP4C2
 */
public class EmployeeDAO {

	public EmployeeDAO() {
		// Nothing to be initialized. This is a stateless class. Constructor
		// has been added to explicitely make this clear.
	}

	/**
	 * Tries to find the Employee by the given employeeNumber in the database.
	 *
	 * @param employeeNumber
	 *            identifies the employee to be loaded from the database
	 * @return the Employee object to be found. In case employee could not be
	 *         found, null is returned.
	 * @throws NoDBConnectionException if there is no database connection
	 */
	public Employee findEmployee(int employeeNumber) throws NoDBConnectionException {
		Employee employee = null;

		// First open a database connnection
		DatabaseConnection connection = new DatabaseConnection();
		if (connection.openConnection()) {
			// If a connection was successfully setup, execute the SELECT
			// statement.
			ResultSet resultset = connection
					.executeSQLSelectStatement("SELECT employee_id, firstname, lastname FROM employee WHERE employee_id = "
							+ employeeNumber + ";");
			Timestamp beginShift = new Timestamp(new Date().getTime());
			if (resultset != null) {
				try {
					// The employeeNumber is unique for a employee, so in case
					// the resultset does contain data, we need its first entry.
					
					// If a connection was successfully setup, execute the INSERT STATEMENT
			
					
					if (resultset.next()) {
						int employeeNumberFromDb = resultset
								.getInt("employee_id");
						String firstNameFromDb = resultset
								.getString("firstname");
						String lastNameFromDb = resultset.getString("lastname");

						employee = new Employee(employeeNumberFromDb,
								firstNameFromDb, lastNameFromDb, beginShift);

					}
				} catch (SQLException e) {
					System.out.println(e);
					employee = null;
				} 
			}
			String query = ("INSERT INTO `active_employee` "
					+ "(`fk_employee_id`, `starttime`, `endtime`)" + "VALUES ('" + employeeNumber + "', '"+beginShift+"', " +
					"'"+beginShift+"');");
	

			connection.executeUpdateStatement(query);
			// else an error occurred leave 'member' to null.

			// We had a database connection opened. Since we're finished,
			// we need to close it.
			connection.closeConnection();
		} else {
			throw new NoDBConnectionException("");
		}

		return employee;

	}

	public boolean setEmployeeEnd(Employee employee) throws SQLException{
		boolean result = false;
		DatabaseConnection connection = new DatabaseConnection();
		if(connection.openConnection()) {
			String updateStatement = "UPDATE `active_employee` SET `endtime` = '"+new Timestamp(new Date().getTime())+"' " +
					"WHERE `starttime` = "+employee.getBeginShift()+";";
			result = connection.executeUpdateStatement(updateStatement);
		}
		return result;
	}

	/**
	 * @param employee the employee object to be removed
	 * @return true if the query to insert the time of logging out
	 * was a success, otherwise false
	 *
	 */
	public boolean removeEmployee(Employee employee) {
		int employeeNumber = employee.getEmployeeNumber();

		// First open a database connnection
		DatabaseConnection connection = new DatabaseConnection();

		if (connection.openConnection()) {
			// If a connection was successfully setup, execute the INSERT STATEMENT
	/*		String query = ("INSERT INTO `avans_hartigehap_c2`.`employee` "
					+ "(`initials`, `firstname`, `lastname`, `address`, `residence`, `zipcode`, "
					+ "`email`) " + "VALUES ('" + nameInitials + "', '"
					+ firstname + "', '" + lastname + "', '" + address + "', '"
					+ residence + "', " + "'" + zipcode + "', '" + emailaddress + "')
					WHERE employeeNumber = " + employeeNumber +";");*/

/*			connection.executeUpdateStatement(query);
			connection.closeConnection();*/
			System.out.println("Query is succesvol");
			return true;
		} else {
			System.out.println("Query was niet succesvol");
			return false;
		}
	}
}

