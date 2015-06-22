package nl.edu.avans.ivp4c2.domain;

/**
 * WrongEmployeeNumberException class will be used when someone use a wrong employeeNumber
 * @author IVP4C2
 */

public class WrongEmployeeNumberException extends Exception{

    private static final long serialVersionUID = 1L;

    /**
     * The constructor
     * @param message this parameter will be used to set a message for the exception
     */
    public WrongEmployeeNumberException(String message) {
    	super(message);
    }
}
