package nl.edu.avans.ivp4c2.domain;

/**
 * CustomerExistsException class will be thrown when someone use a wrong employeeNumber
 * @author IVP4C2
 */

public class CustomerExistsException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * The constructor
	 * @param message this parameter will be used to set a message for the exception
	 */
	public CustomerExistsException(String message) {
		super(message);
	}

}
