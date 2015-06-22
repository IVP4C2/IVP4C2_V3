package nl.edu.avans.ivp4c2.domain;

/**
 * This EmptyFieldsException will be thrown if a TextField is empty.
 * @author IVP4C2
 */

public class EmptyFieldsException extends Exception{
    private static final long serialVersionUID = 1L;

    /**
     * The constructor will initialize a message for the exception
     * @param message will set a message exception
     */
    public EmptyFieldsException(String message) {
    	super(message);
    }

}
