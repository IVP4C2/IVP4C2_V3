package nl.edu.avans.ivp4c2.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bram on 22-5-2015.
 */
public class Employee {
    private int employeeNumber;
    private String employeeFirstName;
    private String employeeLastName;
    private String employeeInitials;
    private Calendar beginShift;
    private Calendar endSShift;

    private final ArrayList<Order> listDoneOrders;

    public Employee(int employeeNumber, String employeeFirstName, String employeeLastName) {
        this.employeeNumber = employeeNumber;
        this.employeeFirstName = employeeFirstName;
        this.employeeLastName = employeeLastName;
        beginShift = Calendar.getInstance();

        // Here will be initialized the list of Orders that have been done by the Employee.
        listDoneOrders = new ArrayList<Order>();
    }

    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public String getEmployeeFirstName() {
        return employeeFirstName;
    }

    public String getEmployeeLastName() {
        return employeeLastName;
    }

    public String getEmployeeInitials() {
        return employeeInitials;
    }
//
//    public void setOrders(Order o) {
//        for(Loan o : ListdoneOrders) {
//            listDone
//        }
//    }

    public void setEmployeeFirstName(String employeeFirstName) {
        this.employeeFirstName = employeeFirstName;
    }

    public void setEmployeeNumber(int employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public void setEmployeeLasttName(String employeeLastName) {
        this.employeeLastName = employeeLastName;
    }




    @Override
    public String toString() {
        return employeeFirstName + " " + employeeLastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;

        if (employeeNumber != employee.employeeNumber) return false;
        if (!employeeFirstName.equals(employee.employeeFirstName)) return false;
        return employeeLastName.equals(employee.employeeLastName);

    }

    @Override
    public int hashCode() {
        int result = employeeNumber;
        result = 31 * result + employeeFirstName.hashCode();
        result = 31 * result + employeeLastName.hashCode();
        return result;
    }
}



