package nl.edu.avans.ivp4c2.manager;

import nl.edu.avans.ivp4c2.domain.Customer;
import nl.edu.avans.ivp4c2.domain.Employee;
import nl.edu.avans.ivp4c2.domain.LoginException;
import nl.edu.avans.ivp4c2.datastorage.RegisterDAO;
import nl.edu.avans.ivp4c2.datastorage.EmployeeDAO;
import nl.edu.avans.ivp4c2.presentation.CustomerException;

public class RegisterManager {
	private Customer customer;
	
	// Constructor
	public RegisterManager() {
	}
	
	// Methods
//	public boolean customerExistence(String emailaddress) {
//		CustomerDAO customerDAO = new CustomerDAO();
//		boolean penis44 = customerDAO.customerExistence(emailaddress);
//		return penis44;
//	}
//	
//	public Customer registerCustomer(String lastname, String nameInitials, String firstname, String address, String residence, String zipcode, String emailaddress) {
//		
//		return customer;
//	}
//	
	
	 public Customer findCustomer(String emailaddress) throws NullPointerException {
		  customer = null;
		 
	         if (customer == null) {
	        	 RegisterDAO registerDAO = new RegisterDAO();
	             customer = registerDAO.findCustomer(emailaddress);
	         } else {
	        	 throw new NullPointerException("Klant bestaal al!");
		 } 
         return customer;
     }
	 
	 
	 
	 public Customer registerCustomer(String lastname, String nameInitials, String firstname, String address, String residence, String zipcode, String emailaddress) {
		 RegisterDAO registerDAO = new RegisterDAO();
		 customer = registerDAO.registerCustomer(lastname, nameInitials, firstname, address, residence, zipcode, emailaddress);
		 return customer; 
	 }
}
