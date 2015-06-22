package nl.edu.avans.ivp4c2.datastorage;
import nl.edu.avans.ivp4c2.domain.Customer;
import nl.edu.avans.ivp4c2.domain.Employee;
import nl.edu.avans.ivp4c2.domain.NoDBConnectionException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

/**
 * Database Access Object used for registering new Customers in the Database
 * @author IVP4C2
 */
public class RegisterDAO {
    public RegisterDAO () {
        // Nothing to be initialized. This is a stateless class. Constructor
        // has been added to explicitely make this clear.
    }

    /**
     * @param emailaddress the emailaddress of the customer
     * @return a customer object if the emailaddress has already
     * been registred in the database, null otherwise
     * @throws NoDBConnectionException if there is no Database connection
     */
    public Customer findCustomer(String emailaddress) throws NoDBConnectionException{
    	Customer customer = null;

        // First open a database connnection
        DatabaseConnection connection = new DatabaseConnection();
        if (connection.openConnection()) {
            // If a connection was successfully setup, execute the SELECT statement.
            ResultSet resultset = connection.executeSQLSelectStatement(
                    "SELECT * FROM customer WHERE email = '" + emailaddress + "';");

            if (resultset != null) {
                try {
                    // The employeeNumber is unique for a employee, so in case the resultset does contain data, we need its first entry.
                    if (resultset.next()) {
                        //int employeeNumberFromDb = resultset.getInt("employee_id");
                    	String lastNameFromDb = resultset.getString("lastname");
                    	String nameInitialsFromDb = resultset.getString("initials");
                    	String firstNameFromDb = resultset.getString("firstname");
                    	String addressFromDb = resultset.getString("address");
                    	String residenceFromDb = resultset.getString("city");
                        String zipcodeFromDb = resultset.getString("zipcode");
                        String emailaddressFromDb = resultset.getString("email");

                        customer = new Customer(lastNameFromDb, nameInitialsFromDb, firstNameFromDb, addressFromDb, residenceFromDb, zipcodeFromDb, emailaddressFromDb );
                    }
                } catch (SQLException e) {
                    System.out.println(e);
                    customer = null;
                }
            }
            // else an error occurred leave 'member' to null.

            // We had a database connection opened. Since we're finished,
            // we need to close it.
            connection.closeConnection();
        } else {
        	throw new NoDBConnectionException("No Database Connection");
        }

        return customer;

    }

    /**
     * @param lastname the lastname of the customer
     * @param nameInitials the initials of the customer
     * @param firstname the firstname of the customer
     * @param address the address of the customer
     * @param residence the residence of the customer
     * @param zipcode the zipcode of the customer
     * @param emailaddress the emailaddress of the customer
     * @return customer object if the insert query was a success
     * @throws NoDBConnectionException if there is no database connection
     */
    public Customer registerCustomer(String lastname, String nameInitials, String firstname, String address, String residence, String zipcode, String emailaddress) throws NoDBConnectionException{
    	Customer customer = null;
        // First open a database connnection
        DatabaseConnection connection = new DatabaseConnection();
        if (connection.openConnection()) {
        	String query = ("INSERT INTO `customer` "
					+ "(`initials`, `firstname`, `lastname`, `address`, `city`, `zipcode`, "
					+ "`email`) "
					+ "VALUES ('" + lastname + "', '" + nameInitials + "', '" + firstname + "', '" + address + "', '" + residence + "', "
					+ "'" + zipcode + "', '" + emailaddress + "');");
         	connection.executeUpdateStatement(query);
        	connection.closeConnection();
        	try {
        		customer = findCustomer(emailaddress);
        	} catch (NoDBConnectionException ndbce) {
        		//Do nothing for now 
        	}
        } else {
        	throw new NoDBConnectionException("No Database Connection");
        }
		return customer;
    }
}

