package nl.edu.avans.ivp4c2.manager;

import nl.edu.avans.ivp4c2.datastorage.EmployeeDAO;
import nl.edu.avans.ivp4c2.domain.Employee;
import nl.edu.avans.ivp4c2.domain.AlreadyLoggedInException;
import nl.edu.avans.ivp4c2.domain.NoDBConnectionException;
import nl.edu.avans.ivp4c2.domain.WrongEmployeeNumberException;
import java.util.*;

/**
 * LoginManager class will manage all logins. It contain a list of employees.
 * @author IVP4C2
 */
public class LoginManager {
	private Employee employee;
	private HashSet<Employee> employeeList = new HashSet<Employee>();
	private boolean existence = false;

	public LoginManager() {
		//Empty constructor
	}

	//Methods
	/**
	 * @param employeeNumber the number of the employee
	 * @return employee object if it has been found in the database, null otherwise
	 * @throws AlreadyLoggedInException if user already logged in
	 * @throws WrongEmployeeNumberException if the employee number is invalid
	 * @throws NoDBConnectionException if there is no database connection
	 */
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

	/** This method will give a Collection of employees, that will be used to create
	 * the JCombBox
	 */
	public HashSet<Employee> getEmployees() {
		return employeeList;
	}


	/**
	 * removeEmployeeFromList will remove an employee from the employeeList
	 * Only if there is an active database connection
	 * @param e is a employee
	 * @return true or false
	 * @throws NoDBConnectionException if there is no Database connection
	 */
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

	/**
	 * checkForExistence will check if an employee already exists in the employeeList
	 * @param e is a employee
	 */
	public void checkForExistence(Employee e) {
		if (employeeList.contains(e)) {
			existence = true;
		}else {
			existence = false;
		}
		
	}

	/**
	 * throwDBConnectionException
	 * @throws NoDBConnectionException if there is no database connection.
	 */
	public void throwDBConnectionException() throws NoDBConnectionException {
		throw new NoDBConnectionException("Hoi");
	}
}

