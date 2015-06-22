package nl.edu.avans.ivp4c2.manager;

import nl.edu.avans.ivp4c2.domain.Customer;
import nl.edu.avans.ivp4c2.domain.Employee;
import nl.edu.avans.ivp4c2.domain.LoginException;
import nl.edu.avans.ivp4c2.datastorage.CustomerDAO;
import nl.edu.avans.ivp4c2.datastorage.EmployeeDAO;
import nl.edu.avans.ivp4c2.presentation.CustomerException;

/**
 * CustomerManager class will manage all customers. This manager can find customers by a emailaddress
 * it can also register a new customer.
 * @author IVP4C2
 *
 */

public class CustomerManager {
	private Customer customer;
	
	// Constructor
	public CustomerManager() {

	}

	/**
	 * findCustomer method can find a customer by his emailadress, because every emailaddress is unique
	 * @param emailaddress find a customer by his emailaddress
	 * @return a customer object
	 * @throws CustomerException may fire when a client already exists
	 */
	 public Customer findCustomer(String emailaddress) throws CustomerException {
	         if (customer == null) {
	             // when the employee isn't loaded from the database yet, we need to do that first
	             // create the employeeDAO to find employees in the database
	             CustomerDAO customerDAO = new CustomerDAO();
	             customer = customerDAO.findCustomer(emailaddress);
	         } else {
	        	 throw new CustomerException("Klant bestaal al!");
		 } 
         return customer;
     }

	/**
	 * registerCustomer method will register a new customer, keep in mind that a emailaddress make a customer unique,
	 * so every emailaddress can only be used once!
	 * @param lastname of the customer
	 * @param nameInitials of the customer
	 * @param firstname of the customer
	 * @param address of the customer
	 * @param residence of the customer
	 * @param zipcode of the customer
	 * @param emailaddress pf the customer
	 * @return a new customer
	 * @throws CustomerException if not all fields are filled in
	 */
	 public Customer registerCustomer(String lastname, String nameInitials, String firstname, String address, String residence, String zipcode, String emailaddress) throws CustomerException  {
		 if(lastname != null && nameInitials != null && firstname != null && address != null && residence != null && zipcode != null && emailaddress != null) {
			 CustomerDAO customerDAO = new CustomerDAO();
			 return customerDAO.registerCustomer(lastname, nameInitials, firstname, address, residence, zipcode, emailaddress); 
		 } else {
			 throw new CustomerException("Vul alle velden in!");
		 }
	 }
}
