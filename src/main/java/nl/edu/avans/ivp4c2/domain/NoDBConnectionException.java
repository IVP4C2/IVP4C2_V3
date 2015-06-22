package nl.edu.avans.ivp4c2.domain;

/**
 *  NoDBConnectionException  class will be thrown when someone use a wrong employeeNumber
 * @author IVP4C2
 */

public class NoDBConnectionException extends Exception {
	
    private static final long serialVersionUID = 1L;

    /**
     * The constructor
     * @param message this parameter will be used to set a message for the exception
     */
    public NoDBConnectionException(String message) {
    	super(message);
    }

}
