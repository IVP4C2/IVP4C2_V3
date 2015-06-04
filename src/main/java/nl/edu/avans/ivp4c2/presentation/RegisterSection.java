package nl.edu.avans.ivp4c2.presentation;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

public class RegisterSection extends JPanel {

	// Attributes
	private JTable registerTable;
	private JTextField lastnameField;
	private JTextField initialsField;
	private JTextField firstnameField;
	private JTextField addressField;
	private JTextField zipcodeField;
	private JTextField emailField;
	private JTextField dateField;
	private JButton registerButton;
	
	
	// Constructor
	public RegisterSection() {
		registerTable = new JTable();
		registerTable.setBorder( BorderFactory.createEtchedBorder());
		
		lastnameField = new JTextField(50);
		initialsField = new JTextField(50);
		firstnameField = new JTextField(50);
		addressField = new JTextField(50);
		zipcodeField = new JTextField(50);
		emailField = new JTextField(50);
		dateField = new JTextField(50);
		registerButton = new JButton("Inschrijven");
		
		
		add(new JLabel( "Naam: Dhr/Mw" ));
		add(lastnameField);
		add(new JLabel( "Voorletter(s):" ));
		add(initialsField);
		add(new JLabel( "Voornaam:" ));
		add(firstnameField);
		add(new JLabel( "Adres:" ));
		add(addressField);
		add(new JLabel( "Postcode en plaats:" ));
		add(zipcodeField);
		add(new JLabel( "E-mailadres:" ));
		add(emailField);
		add(new JLabel( "Datum:" ));
		add(dateField);
		add(registerButton);
			
	}
	// Methods
}
