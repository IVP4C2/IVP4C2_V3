package nl.edu.avans.ivp4c2.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This Employee class can create new employees, those employees can handle table orders.
 * A employee has a employeeNumber that make him/her unique.
 * Every employee has a list with done orders, that will be saved in the database.
 * @author IVP4C2
 */
public class Employee {
    private int employeeNumber;
    private String employeeFirstName;
    private String employeeLastName;
    private String employeeInitials;
    private Timestamp beginShift;
    private Timestamp endSShift;

    private final List<Order> listDoneOrders;

    public Employee(int employeeNumber, String employeeFirstName, String employeeLastName, Timestamp beginShift) {
        this.employeeNumber = employeeNumber;
        this.employeeFirstName = employeeFirstName;
        this.employeeLastName = employeeLastName;
        this.beginShift = beginShift;

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

    public void setEmployeeFirstName(String employeeFirstName) {
        this.employeeFirstName = employeeFirstName;
    }

    public void setEmployeeNumber(int employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public void setEmployeeLasttName(String employeeLastName) {
        this.employeeLastName = employeeLastName;
    }

    public Timestamp getBeginShift() {
        return beginShift;
    }

    public Timestamp getEndSShift() {
        return endSShift;
    }

    public void setEndSShift(Timestamp endSShift) {
        this.endSShift = endSShift;
    }

    /**
     * Override toString, equals and hashcode method so there are no double objects
     */
    @Override
    public String toString() {
        return employeeFirstName + " " + employeeLastName;
    }

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



