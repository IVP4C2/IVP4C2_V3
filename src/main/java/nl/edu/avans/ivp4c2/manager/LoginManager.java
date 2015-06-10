package nl.edu.avans.ivp4c2.manager;


import com.sun.org.apache.xpath.internal.SourceTree;

import nl.edu.avans.ivp4c2.datastorage.EmployeeDAO;
import nl.edu.avans.ivp4c2.domain.Employee;
import nl.edu.avans.ivp4c2.domain.AlreadyLoggedInException;
import nl.edu.avans.ivp4c2.domain.NoDBConnectionException;
import nl.edu.avans.ivp4c2.domain.WrongEmployeeNumberException;
import java.util.*;

/**
 * Created by Bram on 22-5-2015.
 */
public class LoginManager {
	private Employee employee;
	private HashSet<Employee> employeeList = new HashSet<Employee>();
	private boolean existence = false;

	public LoginManager() {
		/*employeeList = new HashSet<Employee>();*/
		// paymentList = new ArrayList<Payment>();
		// employeeList.add(new Employee(2, "Test", "Test"));
		// System.out.println(employeeList.get(2));

	}

	//Methods
	public Employee findEmployee(int employeeNumber) throws AlreadyLoggedInException, WrongEmployeeNumberException, NoDBConnectionException {
        Employee employee = null;
        
        if(employee == null) {
            // when the employee isn't loaded from the database yet, we need to do that first
            // create the employeeDAO to find employees in the database
            EmployeeDAO employeeDAO = new EmployeeDAO();
            employee = employeeDAO.findEmployee(employeeNumber);
        }
        
        if (employee != null) {
        	checkForExistence(employee);
        	
        	if (existence == true) {
        		existence = false;
        		throw new AlreadyLoggedInException("U bent al ingelogd");
        	}else if (existence == false) {
        		employeeList.add(employee);
        	}
        } else {
        	throw new WrongEmployeeNumberException("");
        }

        return employee;
    }

	// This method will give a list of employees, that will be used to create
	// the JCombBox
	public HashSet<Employee> getEmployees() {

		return employeeList;
	}


	//This methode will remove an employee from the employeeList
	//Only if there is an active database connection
	public boolean removeEmployeeFromList(Employee e) throws NoDBConnectionException {

		EmployeeDAO employeeDAO = new EmployeeDAO();
        boolean databaseConnection = employeeDAO.removeEmployee(e);
        
        if (databaseConnection == true) {
			if (employeeList.contains(e)) {
				employeeList.remove(e);
			}
			return true;
        }else {
        	throwDBConnectionException();
        	return false;
        }
	}
	
	//This methode will check if an employee already exists
	//in the employeeList
	public void checkForExistence(Employee e) {
		if (employeeList.contains(e)) {
			existence = true;
		}else {
			existence = false;
		}
		
	}
	
	public void throwDBConnectionException() throws NoDBConnectionException {
		throw new NoDBConnectionException("Hoi");
	}
}

