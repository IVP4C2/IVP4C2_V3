package nl.edu.avans.ivp4c2.domain;

public class CustomerExistsException extends Exception {
	private static final long serialVersionUID = 1L;

	// Constructor
	public CustomerExistsException(String message) {
		super(message);
	}

}
