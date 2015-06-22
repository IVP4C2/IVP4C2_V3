package nl.edu.avans.ivp4c2.domain;

import java.util.regex.Pattern;

/**
 * This class checks whether the email address and zip code meets the requirements.
 * This class contains two regular expressions (patterns) one for a email and one for the zipcode
 * @author IVP4c2
 */

public class RegisterValidator {
	
	// Attributes
	private final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private final  String ZIPCODE_PATTERN = "^[1-9][0-9]{3} ?[A-Za-z]{2}$";
	private Pattern patternEmail;
	private Pattern patternZipcode;
	private java.util.regex.Matcher matcher;

	/**
	 * patternEmail will initialize the EMAIL_PATTERN
	 * patternZipcode will initialize the ZIPCODE_PATTERN
	 */
	public RegisterValidator() {
		patternEmail = Pattern.compile(EMAIL_PATTERN);
		patternZipcode = Pattern.compile(ZIPCODE_PATTERN);
	}
	
	// Methods

	/**
	 * Validate a emailaddress
	 *
	 * @param EMAIL_PATTERN (parameter) will check of the emailaddress is correct
	 *
	 * @return true = valid emailaddress, false invalid = emailaddress
	 */
	public boolean validateEmail(final String EMAIL_PATTERN) {
		matcher = patternEmail.matcher(EMAIL_PATTERN); 
		return matcher.matches();
	}
	
	/**
	 * Validate a DUTCH zipcode
	 * for example:
	 * 4822 DD = true
	 * 4422DD  = true
	 * 
	 * @param ZIPCODE_PATTERN (parameter) will check of the zipcode is correct
	 *            
	 * @return true = valid zipcode, false = invalid  zipcode
	 */
	public boolean validateZipcode(final String ZIPCODE_PATTERN) {
		matcher = patternZipcode.matcher(ZIPCODE_PATTERN); 
		return matcher.matches();
	}
}
