package nl.edu.avans.ivp4c2.manager;

import nl.edu.avans.ivp4c2.domain.Customer;
import nl.edu.avans.ivp4c2.domain.CustomerExistsException;
import nl.edu.avans.ivp4c2.domain.Employee;
import nl.edu.avans.ivp4c2.domain.LoginException;
import nl.edu.avans.ivp4c2.domain.NoDBConnectionException;
import nl.edu.avans.ivp4c2.datastorage.RegisterDAO;
import nl.edu.avans.ivp4c2.datastorage.EmployeeDAO;
import nl.edu.avans.ivp4c2.presentation.CustomerException;

public class RegisterManager {
	private Customer customer;
	
	// Constructor
	public RegisterManager() {
	}
	
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
