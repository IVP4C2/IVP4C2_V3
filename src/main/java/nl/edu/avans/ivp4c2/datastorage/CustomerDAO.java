package nl.edu.avans.ivp4c2.datastorage;
import nl.edu.avans.ivp4c2.domain.Customer;
import nl.edu.avans.ivp4c2.domain.Employee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

/**
 *
 * @author IVP4C2
 */
public class CustomerDAO {
    private LocalDate registerDate;

    public CustomerDAO () {
        // Nothing to be initialized. This is a stateless class. Constructor
        // has been added to explicitely make this clear.
    }

    public Customer findCustomer(String emailaddress) {
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
                        String residenceFromDb = resultset.getString("residence");
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
        }

        return customer;

    }

    public Customer registerCustomer(String lastname, String nameInitials, String firstname, String address, String residence, String zipcode, String emailaddress) {
        Customer customer = null;
        DatabaseConnection connection = null;


        // First open a database connnection
        connection = new DatabaseConnection();
        if (connection.openConnection()) {
            System.out.println("connectie is open");

            String query = ("INSERT INTO `avans_hartigehap_c2`.`customer` "
                    + "(`initials`, `firstname`, `lastname`, `address`, `residence`, `zipcode`, "
                    + "`email`) "
                    + "VALUES ('" + nameInitials + "', '" + firstname + "', '" + lastname + "', '" + address + "', '" + residence + "', "
                    + "'" + zipcode + "', '" + emailaddress + "');");

            //customer = new Customer(String lastname, String nameInitials, String firstname, String address, String residence, y zipcode, emailaddress);

            connection.executeUpdateStatement(query);
            System.out.println(query);
            System.out.println("Record is toegevoegd!");

            customer = new Customer(lastname, nameInitials, firstname, address, residence,  zipcode, emailaddress);

            connection.closeConnection();

        }
        return customer;
    }
}

