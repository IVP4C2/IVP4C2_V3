package nl.edu.avans.ivp4c2.domain;

/**
 * AlreadyLoggedInException class will be thrown when someone uses a wrong employeeNumber
 * @author IVP4C2
 */

public class AlreadyLoggedInException extends Exception {
	
    private static final long serialVersionUID = 1L;

    /**
     * The constructor
     * @param message this parameter will be used to set a message for the exception
     */
    public AlreadyLoggedInException(String message) {
    	super(message);
    }

}
