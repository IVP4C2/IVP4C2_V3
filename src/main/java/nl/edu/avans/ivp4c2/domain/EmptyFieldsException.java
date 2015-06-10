package nl.edu.avans.ivp4c2.domain;

public class EmptyFieldsException extends Exception{
    private static final long serialVersionUID = 1L;

    // Constructor
    public EmptyFieldsException(String message) {
    	super(message);
    }

}
