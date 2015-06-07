package nl.edu.avans.ivp4c2.manager;

import nl.edu.avans.ivp4c2.datastorage.EmployeeDAO;
import nl.edu.avans.ivp4c2.domain.Employee;
import nl.edu.avans.ivp4c2.domain.LoginException;

import java.util.*;

/**
 * Created by Bram on 22-5-2015.
 */
public class LoginManager {
    private Employee employee;
    private HashMap<Integer, Employee> employeeList;
    //private boolean existence = false;

    public LoginManager() {
        employeeList = new HashMap();
    }

    // Methods
    /**
     *  This method will be used to login a Employee
     * @param employeeNumber
     * @return a Employee
     */


    /** TO DO
     *
     * EmployeeDAO geeft een employee object terug
     * de manager zoekt een employee in de database dmv de dao
     * voegt deze toe aan de lijst
     * geef deze lijst aan de GUI en plaats alles in een Combobox
     */


    /**
     * @param employeeNumber will be used to find a Employee in the Database or local
     * @return a employee object
     */

    public Employee findEmployee(int employeeNumber) throws LoginException {
            employee = null;

            if (employee == null) {
                // when the employee isn't loaded from the database yet, we need to do that first
                // create the employeeDAO to find employees in the database
                EmployeeDAO employeeDAO = new EmployeeDAO();
                employee = employeeDAO.findEmployee(employeeNumber);

                if (employee != null)  {
                    if(checkForExistence(employee)) {
                        System.out.println("U bent al ingelogd");
                        throw new LoginException("U bent al ingelogd");
                    }
                    else {
                        // Cache the employee that has been found in the database in our main memory.
                        employeeList.put(employeeNumber, employee);
                        System.out.println("bestaat nog niet, object wordt toegevoegd");
                    }
                }
            }
            return employee;
        }


    public HashMap<Integer, Employee> getEmployees() {
        return employeeList;
    }

    public void logoutEmployee(Employee e) throws LoginException {
        if(employeeList.containsValue(e)) {
            employeeList.remove(e);
        } else {
            throw new LoginException("message");
        }
    }

    public boolean checkForExistence(Employee e) {
        if(employeeList.containsValue(e)) {
            return true;
        } else {
            return false;
        }
    }
}