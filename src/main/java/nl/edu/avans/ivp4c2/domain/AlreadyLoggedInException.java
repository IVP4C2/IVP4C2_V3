package nl.edu.avans.ivp4c2.domain;

public class AlreadyLoggedInException extends Exception {
	
    private static final long serialVersionUID = 1L;

    // Constructor
    public AlreadyLoggedInException(String message) {
    	super(message);
    }

}
