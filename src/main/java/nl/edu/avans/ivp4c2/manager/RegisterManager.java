package nl.edu.avans.ivp4c2.manager;

import nl.edu.avans.ivp4c2.domain.Customer;
import nl.edu.avans.ivp4c2.domain.CustomerExistsException;
import nl.edu.avans.ivp4c2.domain.Employee;
import nl.edu.avans.ivp4c2.domain.LoginException;
import nl.edu.avans.ivp4c2.domain.NoDBConnectionException;
import nl.edu.avans.ivp4c2.datastorage.RegisterDAO;
import nl.edu.avans.ivp4c2.datastorage.EmployeeDAO;
import nl.edu.avans.ivp4c2.presentation.CustomerException;

/**
 * RegisterManager will handle all register operations regarding customers.
 * @author IVP4C2
 */

public class RegisterManager {
	private Customer customer;
	
	// Constructor
	public RegisterManager() {
	}

	/**
	 * @param emailaddress the emailaddress of the customer to be registred
	 * @return customer object if the emailaddress has already been registered, null otherwise
	 * @throws CustomerExistsException if a customer already exists
	 * @throws NoDBConnectionException if there is no database connection
	 */
	 public Customer findCustomer(String emailaddress) throws CustomerExistsException, NoDBConnectionException {
		  customer = null;
		 
	         if (customer == null) {
	        	 RegisterDAO registerDAO = new RegisterDAO();
	        	 try {
	        		 customer = registerDAO.findCustomer(emailaddress);
	        	 } catch (NoDBConnectionException ndce) {
	        		 throw new NoDBConnectionException("No Database Connection");
	        	 }
	         } else {
	        	 throw new CustomerExistsException("Klant bestaal al!");
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
	 * @return customer object if the customer is added to the database succesfully, null otherwise
	 * @throws NoDBConnectionException if there is no database connection
	 */
	 public Customer registerCustomer(String lastname, String nameInitials, String firstname, String address, String residence, String zipcode, String emailaddress) throws NoDBConnectionException {
		 RegisterDAO registerDAO = new RegisterDAO();
		 try {
		 	customer = registerDAO.registerCustomer(lastname, nameInitials, firstname, address, residence, zipcode, emailaddress);
		 } catch(NoDBConnectionException ndce) {
			 throw new NoDBConnectionException("No Database Connection");
		 }
		 return customer; 
	 }
}
