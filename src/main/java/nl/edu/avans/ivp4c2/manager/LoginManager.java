package nl.edu.avans.ivp4c2.manager;

import nl.edu.avans.ivp4c2.datastorage.EmployeeDAO;
import nl.edu.avans.ivp4c2.domain.Employee;

import java.util.*;

/**
 * Created by Bram on 22-5-2015.
 */
public class LoginManager {
    private Employee employee;
    private HashMap<Integer, Employee> employeeList;
    //private ArrayList<Payment> paymentList;

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

    public Employee findEmployee(int employeeNumber) {
        Employee employee = employeeList.get(employeeNumber);
        if (employee == null) {
            // when the employee isn't loaded from the database yet, we need to do that first
            // create the employeeDAO to find employees in the database
            EmployeeDAO employeeDAO = new EmployeeDAO();
            employee = employeeDAO.findEmployee(employeeNumber);

            if (employee != null) {
                // Cache the employee that has been found in the database in our main memory.
                employeeList.put(employeeNumber, employee);
            }
        }
        return employee;
    }

    public HashMap<Integer, Employee> getEmployees() {
        return employeeList;
    }
//
//    public void logoutEmployee(Employee e) {
//        for(Map.Entry<Integer, Employee> entry : employeeList.entrySet()) {
//           if(entry == e) {
//               employeeList.remove(entry);
//           }
//
//    private boolean isValidEmployeeNumber(int input) {
//        boolean status = false;
//        for(Map.Entry<Integer, Employee> entry : employeeList.entrySet()) {
//            if(entry.getKey() == input) {
//                return true;
//            }
//        }
//        return status;
//    }
//
//    public void setValidEmployeeNumber(int input) throws invalidEmployeeNumberException {
//
//    }

}
