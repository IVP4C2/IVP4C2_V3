package nl.edu.avans.ivp4c2.domain;

public class WrongEmployeeNumberException extends Exception{

    private static final long serialVersionUID = 1L;

    // Constructor
    public WrongEmployeeNumberException(String message) {
    	super(message);
    }
}
