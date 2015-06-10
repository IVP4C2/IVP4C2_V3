package nl.edu.avans.ivp4c2.presentation;

import nl.edu.avans.ivp4c2.domain.Customer;
import nl.edu.avans.ivp4c2.manager.CustomerManager;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class RegisterSection extends JPanel {

	// Attributes
	private JPanel registerPanel;
	private JPanel registerOutput;
	private JTextField lastnameField;
	private JTextField initialsField;
	private JTextField firstnameField;
	private JTextField addressField;
	private JTextField residenceField;
	private JTextField zipcodeField;
	private JTextField emailField;
//	private JTextField dateField;
	private JTextArea registeredCustomer;
	private JButton registerButton;
	
	
	// Constructor
	public RegisterSection() {
			
		//setLayout(new BorderLayout());
			this.registerPanel = new JPanel( new GridLayout(8,2, 10 , 20) );
			this.registerOutput = new JPanel( new GridLayout(8, 2, 10, 20) );
		
			lastnameField = new JTextField(50);
			initialsField = new JTextField(50);
			firstnameField = new JTextField(50);
			addressField = new JTextField(50);
			residenceField = new JTextField(50);
			zipcodeField = new JTextField(50);
			emailField = new JTextField(50);
			registerButton = new JButton("Inschrijven");
			registerButton.addActionListener( new registerHandler() );
			
			Font font = new Font("SansSerif", Font.PLAIN, 18);
			registeredCustomer = new JTextArea();
			registeredCustomer.setFont(font);
		
			
			registerPanel.add(new JLabel( "Naam: Dhr/Mw" ));
			registerPanel.add(lastnameField);
			
			registerPanel.add(new JLabel( "Voorletter(s):" ));
			registerPanel.add(initialsField);
			
			registerPanel.add(new JLabel( "Voornaam:" ));
			registerPanel.add(firstnameField);
			
			registerPanel.add(new JLabel( "Adres + huisnr:" ));
			registerPanel.add(addressField);
			
			registerPanel.add(new JLabel("Plaats:"));
			registerPanel.add(residenceField);
			
			registerPanel.add(new JLabel( "Postcode en plaats:" ));
			registerPanel.add(zipcodeField);
			
			registerPanel.add(new JLabel( "E-mailadres:" ));
			registerPanel.add(emailField);
			
			registerPanel.add(new JLabel());
			registerPanel.add(registerButton);
			
			add(registerPanel);
			add(registerOutput);
			
	}
	
	// Inner class
	class registerHandler extends Component implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Customer customer = null;
			CustomerManager customerManager = new CustomerManager();
			
			String emailaddress = emailField.getText();
			
			// Check is customer already exists, if exists show catch message
			try {
			customer =  customerManager.findCustomer(emailaddress);
			} catch(CustomerException ce) {
						JOptionPane.showMessageDialog(registerPanel, "Bestaal al", "asdfasdfsadf", JOptionPane.ERROR_MESSAGE);
			}
				
				if(customer == null) {
					String lastname = lastnameField.getText();
					String nameInitials = initialsField.getText();
					String firstname = firstnameField.getText();
					String address = addressField.getText();
					String residence = residenceField.getText();
					String zipcode = zipcodeField.getText();
					String email = emailField.getText();
									
							try {
								customer = customerManager.registerCustomer(lastname, nameInitials, firstname, address, residence, zipcode, email);
							} catch (CustomerException cep) {
								JOptionPane.showMessageDialog(registerPanel, cep.getMessage(), "", JOptionPane.ERROR_MESSAGE);;
							}
							
							System.out.println("customer toegevoegd: " + customer);
							
							registeredCustomer.setText(
									"De volgende klant is toegevoegd:"
											+ "\n"
											+ "\n"
											+ "Achternaam: " + " " + customer.getFirstname()  
											+ "\n"
											+ "Voorletter(s): " + " " + customer.getNameInitials()
											+ "\n"
											+ "Voornaam: " + " " + customer.getFirstname()
											+ "\n"
											+ "Adres + huisnr: " + " " + customer.getFirstname()
											+ "\n"
											+ "Plaats: " + " " + customer.getFirstname()
											+ "\n"
											+ "Postcode en plaats: " + " " + customer.getFirstname()
											+ "\n"
											+ "E-mailadres: " + " " + customer.getFirstname()
											);
							
							
							registerOutput.add(registeredCustomer);
							registeredCustomer.setEditable(false);
							registerPanel.revalidate();
							registerPanel.removeAll();
						} 
		}
	}	
}
