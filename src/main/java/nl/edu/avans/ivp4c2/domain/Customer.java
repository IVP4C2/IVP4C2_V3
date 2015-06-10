package nl.edu.avans.ivp4c2.domain;

import java.sql.Time;
import java.time.*;

import nl.edu.avans.ivp4c2.presentation.CustomerException;

public class Customer {
	// Attributes
	private String lastname;
	private String nameInitials;
	private String firstname;
	private String address;		// street + house number
	private String residence;	// place like 'Breda'
	private String zipcode;
	private String emailaddress;
	private LocalDate registerDate;
	
	// Constructor
	public Customer(String lastname, String nameInitials, String firstname, String address, String residence, String zipcode, String emailaddress) {
		this.lastname = lastname;
		this.nameInitials = nameInitials;
		this.firstname = firstname;
		this.address = address;
		this.residence = residence;
		this.zipcode = zipcode;
		this.emailaddress = emailaddress;
		registerDate = LocalDate.now();
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	

	public String getNameInitials() {
		return nameInitials;
	}

	public void setNameInitials(String nameInitials) {
		this.nameInitials = nameInitials;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getResidence() {
		return residence;
	}

	public void setResidence(String residence) {
		this.residence = residence;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getEmailaddress() {
		return emailaddress;
	}

	public void setEmailaddress(String emailaddress) {
		this.emailaddress = emailaddress;
	}
	
	public String toString() {
		return lastname + nameInitials + firstname + address + residence + zipcode + emailaddress;
	}
	
	public LocalDate getRegisterDate() {
		return registerDate;
	}
}
