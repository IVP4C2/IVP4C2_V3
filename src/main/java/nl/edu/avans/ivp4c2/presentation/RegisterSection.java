package nl.edu.avans.ivp4c2.presentation;

import nl.edu.avans.ivp4c2.domain.Customer;
import nl.edu.avans.ivp4c2.domain.CustomerExistsException;
import nl.edu.avans.ivp4c2.domain.NoDBConnectionException;
import nl.edu.avans.ivp4c2.domain.RegisterValidator;
import nl.edu.avans.ivp4c2.manager.RegisterManager;

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
	// private JTextField dateField;
	private JTextArea registeredCustomer;
	private JButton registerButton;

	// Constructor
	public RegisterSection() {

		// setLayout(new BorderLayout());
		this.registerPanel = new JPanel(new GridLayout(8, 2, 10, 20));
		this.registerOutput = new JPanel(new GridLayout(8, 2, 10, 20));

		lastnameField = new JTextField(50);
		initialsField = new JTextField(50);
		firstnameField = new JTextField(50);
		addressField = new JTextField(50);
		residenceField = new JTextField(50);
		zipcodeField = new JTextField(50);
		emailField = new JTextField(50);
		registerButton = new JButton("Inschrijven");
		registerButton.addActionListener(new registerHandler());

		Font font = new Font("SansSerif", Font.PLAIN, 18);
		registeredCustomer = new JTextArea();
		registeredCustomer.setFont(font);

		registerPanel.add(new JLabel("Naam: Dhr/Mw"));
		registerPanel.add(lastnameField);

		registerPanel.add(new JLabel("Voorletter(s):"));
		registerPanel.add(initialsField);

		registerPanel.add(new JLabel("Voornaam:"));
		registerPanel.add(firstnameField);

		registerPanel.add(new JLabel("Adres + huisnr:"));
		registerPanel.add(addressField);

		registerPanel.add(new JLabel("Plaats:"));
		registerPanel.add(residenceField);

		registerPanel.add(new JLabel("Postcode en plaats:"));
		registerPanel.add(zipcodeField);

		registerPanel.add(new JLabel("E-mailadres:"));
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
			RegisterManager customerManager = new RegisterManager();
			String emailaddress = emailField.getText();
			
			//Maak register validator aan: 
			RegisterValidator registervalidator = new RegisterValidator();

			// Check is customer already exists, if exists show catch message
			if (!emailField.getText().equals("")) {
				if (registervalidator.validateEmail(emailaddress)) {
					try {
						customer = customerManager.findCustomer(emailaddress);
					} catch (NoDBConnectionException | CustomerExistsException ndce) {
						JOptionPane
								.showMessageDialog(
										this,
										"Geen connectie met de database, probeer later opnieuw",
										"Foutmelding: Aanmelden",
										JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(registerHandler.this,
							"Voer een geldig emailadres in!",
							"Foutmelding: Ongeldig emailadres",
							JOptionPane.ERROR_MESSAGE);
				}
				if (customer != null) {
					JOptionPane.showMessageDialog(registerHandler.this,
							"De klant bestaat al", "Foutmelding: Bestaal al",
							JOptionPane.ERROR_MESSAGE);
				} else {
					if (!lastnameField.getText().equals("")
							&& !initialsField.getText().equals("")
							&& !firstnameField.getText().equals("")
							&& !addressField.getText().equals("")
							&& !residenceField.getText().equals("")
							&& !zipcodeField.getText().equals("")
							&& !emailField.getText().equals("")) {
						
						if (registervalidator.validateZipcode(zipcodeField.getText())) {

							try {
								customer = customerManager.registerCustomer(
										lastnameField.getText(),
										initialsField.getText(),
										firstnameField.getText(),
										addressField.getText(),
										residenceField.getText(),
										zipcodeField.getText(),
										emailField.getText());
							} catch (NoDBConnectionException e1) {
								JOptionPane
										.showMessageDialog(
												this,
												"Geen connectie met de database, probeer later opnieuw",
												"Foutmelding: Aanmelden",
												JOptionPane.ERROR_MESSAGE);
							}
							try {
								System.out.println("customer toegevoegd: "
										+ customer);
								registeredCustomer
										.setText("De volgende klant is toegevoegd:"
												+ "\n" + "\n" + "Achternaam: "
												+ " "
												+ customer.getLastname()
												+ "\n"
												+ "Voorletter(s): "
												+ " "
												+ customer.getNameInitials()
												+ "\n"
												+ "Voornaam: "
												+ " "
												+ customer.getFirstname()
												+ "\n"
												+ "Adres:"
												+ " "
												+ customer.getAddress()
												+ "\n"
												+ "Plaats: "
												+ " "
												+ customer.getResidence()
												+ "\n"
												+ "Postcode: "
												+ " "
												+ customer.getZipcode()
												+ "\n"
												+ "E-mailadres: "
												+ " "
												+ customer.getEmailaddress());
	
								registerPanel.revalidate();
								registerPanel.removeAll();
								registerOutput.add(registeredCustomer);
								registeredCustomer.setEditable(false);
							} catch (NullPointerException ne) {
							}
						} else {
							JOptionPane.showMessageDialog(registerHandler.this,
									"Voer een geldige postcode in",
									"Foutmelding: Ongeldige postcode",
									JOptionPane.ERROR_MESSAGE);
						}
					}else {
						JOptionPane.showMessageDialog(registerHandler.this,
						"Vul alle invulvelden in!",
						"Foutmelding: gegevens niet ingevuld",
						JOptionPane.ERROR_MESSAGE);
					}
				}
			} else {
				JOptionPane.showMessageDialog(registerHandler.this,
						"Vul alle invulvelden in!",
						"Foutmelding: gegevens niet ingevuld",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
