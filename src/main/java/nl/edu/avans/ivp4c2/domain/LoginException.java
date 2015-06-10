package nl.edu.avans.ivp4c2.domain;

/**
 * Created by Bram on 29-5-2015.
 */
public class LoginException extends Exception{

    private static final long serialVersionUID = 1L;


    // Constructor
    public LoginException(String message) {
    super(message);
    }
}
