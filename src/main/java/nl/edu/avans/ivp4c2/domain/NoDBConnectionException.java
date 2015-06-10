package nl.edu.avans.ivp4c2.domain;

public class NoDBConnectionException extends Exception {
	
    private static final long serialVersionUID = 1L;

    // Constructor
    public NoDBConnectionException(String message) {
    	super(message);
    }

}
