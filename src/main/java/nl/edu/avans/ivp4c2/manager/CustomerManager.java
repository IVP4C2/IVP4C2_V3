package nl.edu.avans.ivp4c2.manager;

import nl.edu.avans.ivp4c2.domain.Customer;
import nl.edu.avans.ivp4c2.domain.Employee;
import nl.edu.avans.ivp4c2.domain.LoginException;
import nl.edu.avans.ivp4c2.datastorage.CustomerDAO;
import nl.edu.avans.ivp4c2.datastorage.EmployeeDAO;
import nl.edu.avans.ivp4c2.presentation.CustomerException;

public class CustomerManager {
	private Customer customer;
	
	// Constructor
	public CustomerManager() {

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
	 
	 
	 
	 public Customer registerCustomer(String lastname, String nameInitials, String firstname, String address, String residence, String zipcode, String emailaddress) throws CustomerException  {
		 if(lastname != null && nameInitials != null && firstname != null && address != null && residence != null && zipcode != null && emailaddress != null) {
			 CustomerDAO customerDAO = new CustomerDAO();
			 return customerDAO.registerCustomer(lastname, nameInitials, firstname, address, residence, zipcode, emailaddress); 
		 } else {
			 throw new CustomerException("Vul alle velden in!");
		 }
	 }
}
