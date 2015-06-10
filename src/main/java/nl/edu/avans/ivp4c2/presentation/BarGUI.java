package nl.edu.avans.ivp4c2.presentation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import nl.edu.avans.ivp4c2.datastorage.OrderDAO;
import nl.edu.avans.ivp4c2.domain.*;
import nl.edu.avans.ivp4c2.manager.BarManager;
import nl.edu.avans.ivp4c2.manager.LoginManager;
import nl.edu.avans.ivp4c2.manager.PaymentManager;

public class BarGUI extends JPanel {
	// Buttons
	private JComboBox<Employee> employeeBox;
	private JButton completeOrderButton;
	private JButton tableHistoryButton;
	private JButton signupButton;
	private JButton signInOutButton;

	private final int AMOUNT_OF_TABLEBUTTONS = 11;
	private JButton[] tableButton;

	private HashSet<Employee> employeeList;

	// Panels
	private JPanel panelNorth;
	private JPanel panelNorthLeft;
	private JPanel panelNorthRight;
	private JPanel panelWest;
	private JPanel panelCenter;
	private int activeTable;

	private JTextField logInOutField;
	private JButton loginButton;
	private JButton logoutButton;

	/*
	 * Initialize JTable to fill rightPanel and leftPanel. By doing so, we make
	 * sure there is always an object present in the layout
	 */
	private PaymentManager paymentManager;

	private BarManager barmanager;
	private LoginManager loginmanager;
	private OrderSection orderSection;
	private PaymentSection paymentSection;

	public BarGUI(BarManager barmanager) {
		this.barmanager = barmanager;
		this.loginmanager = new LoginManager();
		this.paymentManager = new PaymentManager();
		this.orderSection = new OrderSection();
		this.paymentSection = new PaymentSection();

		setLayout(new BorderLayout());

		Font font = new Font("SansSerif", Font.PLAIN, 24);

		// create new panels
		panelNorth = new JPanel();
		panelNorthLeft = new JPanel();
		panelNorthRight = new JPanel();
		panelWest = new JPanel();
		panelCenter = new JPanel();
		panelCenter.setBackground(Color.WHITE);

		panelNorth.setLayout(new BorderLayout()); // Added
		panelNorthLeft.setLayout(new GridLayout(1, 2));
		panelNorthLeft.setSize(600, 200);
		panelNorthRight.setLayout(new GridLayout(2, 5));
		panelWest.setLayout(new GridLayout(7, 1));
        /*Has room for one JPanel. The panel can change depending on the table Status.
        * panelCenter.removeAll() will remove the current JPanel and a new one can be placed */
		panelCenter.setLayout(new GridLayout(1, 1)); // Has room for one JPanel. The panel displayed here will change depending on the tableStatus

		// Setup North panel

		/* Reading and setting logo image */
		panelNorthLeft.add(new JLabel(new ImageIcon(getClass().getResource("/logo_resized.jpg"))));

		// Array with the ten table buttons
		tableButton = new JButton[AMOUNT_OF_TABLEBUTTONS];
		for (int tb = 1; tb <= 10; tb++) {
			tableButton[tb] = new JButton("" + tb);
			tableButton[tb].setBackground(Color.decode("#DFDFDF"));
			tableButton[tb].addActionListener(new TableButtonHandler());
			tableButton[tb].setFont(font);
			tableButton[tb].setBorder(BorderFactory.createEtchedBorder());
			panelNorthRight.add(tableButton[tb]);
		}


		employeeBox = new JComboBox<Employee>();

		signupButton = new JButton("Inschrijven");
		signupButton.setBackground(Color.decode("#DFDFDF"));
		signupButton.setFont(font);
		signupButton.setBorder(BorderFactory.createEtchedBorder());
		signupButton.addActionListener(new SignupHandler());
		completeOrderButton = new JButton();
		completeOrderButton.setText("Status Aanpassen");
		completeOrderButton.addActionListener(new CompleteOrderHandler());
		completeOrderButton.setBackground(Color.decode("#DFDFDF"));
		completeOrderButton.setFont(font);
		completeOrderButton.setBorder(BorderFactory.createEtchedBorder());
		tableHistoryButton = new JButton("Geschiedenis");
		tableHistoryButton.setBackground(Color.decode("#DFDFDF"));
		tableHistoryButton.setFont(font);
		tableHistoryButton.setBorder(BorderFactory.createEtchedBorder());
		logInOutField = new JTextField(11);
		logInOutField.setBorder(BorderFactory.createEtchedBorder());
		logInOutField.setMargin(new Insets(50, 50, 50, 50));
		logInOutField.setFont(font);
		loginButton = new JButton("Aanmelden");
		loginButton.setFont(font);
		loginButton.setBackground(Color.decode("#DFDFDF"));
		loginButton.setBorder(BorderFactory.createEtchedBorder());
		loginButton.addActionListener(new LogInOutHandler());
		logoutButton = new JButton(" Afmelden");
		logoutButton.setFont(font);
		logoutButton.setBackground(Color.decode("#DFDFDF"));
		logoutButton.setBorder(BorderFactory.createEtchedBorder());
		logoutButton.addActionListener(new LogInOutHandler());

		// Items added to panel West
		panelWest.add(employeeBox);
		panelWest.add(completeOrderButton);
		panelWest.add(tableHistoryButton);
		panelWest.add(signupButton);
		panelWest.add(logInOutField);
		panelWest.add(loginButton);
		panelWest.add(logoutButton);


		// Add all panels
		panelNorth.add(panelNorthLeft, BorderLayout.WEST); // Added
		panelNorth.add(panelNorthRight, BorderLayout.CENTER); // Added
		add(panelNorth, BorderLayout.NORTH);
		add(panelCenter, BorderLayout.CENTER);
		add(panelWest, BorderLayout.WEST);

		/*
		 * Calls setTableStatus() every second. Other methods that should be
		 * called every X seconds should be added here too
		 */
		ScheduledExecutorService exec = Executors
				.newSingleThreadScheduledExecutor();
		exec.scheduleAtFixedRate(new Runnable() {

			public void run() {
				setTableStatus();
			}

		}, 0, 1, TimeUnit.SECONDS);
	}

	// Methods

	/*
	 * Using new method to set table status. Atable can only have the
	 * status 'Bezet', 'Afrekenen' or 'Leeg'.
	 */
	public void setTableStatus() {
		List<Table> tableStatusOrder = barmanager.getOccupiedTables(); //Contains all 'Bezet' tables
		List<Table> tableStatusPayment = barmanager.getPaymentTables(); //Contains all 'Afrekenen' tables
		List<Table> tableStatusEmpty = barmanager.getEmptyTables(); //Contains all 'Leeg' tables
		Timestamp longestBarOrder = null; //Used to store the Date of the longest waiting bar order
		Timestamp longestKitchenOrder = null; //Used to store the Date of the longest waiting kitchen order
		Timestamp longestPaymentRequest = null; //Used to store the Date of the longest waiting payment request
		Set<Integer> barOrderTables = new HashSet<Integer>();  //reference to table which get a green color
		Set<Integer> kitchenOrderTables = new HashSet<Integer>(); //reference to table which get a yellow color
        /*Sinse only one table can have the longest waiting order,
        we only need to store a reference to the bar and kitchen table*/
		int longestBarTable = 0;
		int longestKitchenTable = 0;

		// Set tableButtons for tables with status 'Leeg'
		for (Table te : tableStatusEmpty) {
			int tb = te.getTableNumber();
			tableButton[tb].setBackground(Color.decode("#DFDFDF")); //Set button color. grey color.
			tableButton[tb].setEnabled(false); //Table is empty. Therefore, the button is disabled
			repaint();
		}


		// Set tableButtons for tables with status 'Bezet'
		for (Table to : tableStatusOrder) {
			int tb = to.getTableNumber();
			boolean hasKitchenOrder = false;
			tableButton[tb].setEnabled(true); //Table is occupied. Therefore, the button is enabled
			for(Order o : to.getOrders()) {
				if(o.getDestination() == 2) {
					kitchenOrderTables.add(tb);
					hasKitchenOrder = true;
					if(longestKitchenOrder == null || o.getOrderTime().before(longestKitchenOrder)) {
						longestKitchenTable = tb;
						longestKitchenOrder = o.getOrderTime();
					}
				}
				if(o.getDestination() == 1 && !hasKitchenOrder) {
					barOrderTables.add(tb);
					if(longestBarOrder == null || o.getOrderTime().before(longestBarOrder)) {
						longestBarTable = tb;
						longestBarOrder = o.getOrderTime();

					}
				}
			}
		}
        /*Set colors*/
		for(int i : barOrderTables) {
			tableButton[i].setBackground(Color.GREEN);
		}
		for(int i : kitchenOrderTables) {
			tableButton[i].setBackground(Color.YELLOW);
		}

		if(longestBarTable > 0) {
			tableButton[longestBarTable].setBackground(Color.decode("#008A2E"));
		}
		if(longestKitchenTable > 0) {
			tableButton[longestKitchenTable].setBackground(Color.ORANGE);
		}

		// Set tableButtons for tables with status 'Afrekenen'
		for (Table tp : tableStatusPayment) {
			int tb = tp.getTableNumber();
			tableButton[tb].setEnabled(true); //Table wants to pay. Therefore, the button is enabled
			tableButton[tb].setBackground(Color.RED);
			repaint();
		}

	}

	// Inner classes
	/**
	 *Adds a different JPanel to panelCenter depending on the table status
	 * Uses classes OrderSection and PaymentSection
	 */
	class TableButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			for (int tb = 1; tb <= 10; tb++) {
				if (e.getSource() == tableButton[tb]) {

					/*Set border on clicked table button*/
					TitledBorder topBorder = BorderFactory.createTitledBorder("Actief");
					topBorder.setBorder(BorderFactory.createLineBorder(Color.black));
					topBorder.setTitlePosition(TitledBorder.TOP);
					tableButton[tb].setBorder(topBorder);

                    /*retrieve the matching table from the barmanager for a given tableNumber*/
					Table table = barmanager.getHashTable(tb);
                    /*Check if a table object is present*/
					if (table != null) {
                        /**/
						if ("Bezet".equals(table.getTableStatus()) || "Hulp".equals(table.getTableStatus())) {
							completeOrderButton.setText("Status Aanpassen");
							panelWest.revalidate();
							panelCenter.removeAll();
							panelCenter.add(orderSection.getTableLeft(table, panelCenter));
							activeTable = tb;
							panelCenter.revalidate();
						} else if ("Afrekenen".equals(table.getTableStatus())) {
							completeOrderButton.setText("Afronden");
							panelWest.revalidate();
							panelCenter.removeAll();
							Payment p = paymentManager.getActivePayment(tb);
							activeTable = tb;
							panelCenter.add(paymentSection.getPaymentPanel(p));
							revalidate();
						}
						else {
							panelCenter.removeAll();

						}
					}
				} else {
					TitledBorder topBorderInactive = BorderFactory.createTitledBorder("");
					topBorderInactive.setBorder(BorderFactory.createLineBorder(Color.decode("#DFDFDF")));
					topBorderInactive.setTitlePosition(TitledBorder.TOP);
					tableButton[tb].setBorder(topBorderInactive);
					tableButton[tb].setBorder(BorderFactory.createEtchedBorder());
					revalidate();
				}
			}
			revalidate();
		}
	}



	/**
	 * CONTENTS MIGHT GET REPLACED BY A DEDICATED CompleOrderButton CLASS WTIH EXCEPTION HANDLING
	 * Handles the CompleteOrder button.
	 * Depending on the tableStatus, completes the Payment or Order.
	 */
	class CompleteOrderHandler implements  ActionListener {
		public void actionPerformed(ActionEvent e) {

			if (employeeBox.getItemCount() > 0) {
				if("Afrekenen".equals(barmanager.getHashTable(activeTable).getTableStatus())) {
					try {
						paymentManager.completePayment(activeTable);
						barmanager.removeTable(activeTable);
						tableButton[activeTable].setBackground(Color.decode("#DFDFDF"));
						revalidate();
						panelCenter.removeAll();
						JOptionPane.showMessageDialog(BarGUI.this, "Rekening Succesvol afgerond", "Rekening Afgerond", JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception f) {
						JOptionPane.showMessageDialog(BarGUI.this, f.getMessage(), "Fout", JOptionPane.ERROR_MESSAGE);
						Logger logger = Logger.getAnonymousLogger();
						logger.log(Level.SEVERE, "An exception was thrown in BarGUI at CompleteOrderHandeler", f);
					}
				} else if ("Bezet".equals(barmanager.getHashTable(activeTable).getTableStatus())) {
					if(orderSection.getSelectedOrder() != null) {
						JPanel optionPanel = new JPanel();
						optionPanel.add(new JButton("In Behandeling")).setBackground(Color.decode("#DFDFDF"));
						optionPanel.add(new JButton("Gereed")).setBackground(Color.decode("#DFDFDF"));
						optionPanel.add(new JButton("Geserveerd")).setBackground(Color.decode("#DFDFDF"));
						String[] buttons = {"In Behandeling", "Gereed", "Geserveerd"};
//					JOptionPane.showMessageDialog(BarGUI.this, optionPanel, "Status", JOptionPane.DEFAULT_OPTION);
						int optionPane = JOptionPane.showOptionDialog(BarGUI.this, "Status Aanpassen", "Status",
								JOptionPane.DEFAULT_OPTION, 0, null, buttons, buttons[2]);
						try {
							OrderDAO orderDAO = new OrderDAO();
							switch (optionPane) {
								case 0:
									System.out.println("In behandeling");
									int status = 2;
									barmanager.getHashTable(activeTable).getSpecificOrder(orderSection.getSelectedOrder().getOrderNumber()).setOrderStatus("In behandeling");
									orderDAO.updateOrder(orderSection.getSelectedOrder().getOrderNumber(), status);
									panelCenter.revalidate();
									break;
								case 1:
									System.out.println("Gereed");
									status = 3;
//									orderSection.getSelectedOrder().setOrderStatus("Gereed");
									barmanager.getHashTable(activeTable).getSpecificOrder(orderSection.getSelectedOrder().getOrderNumber()).setOrderStatus("Gereed");
									orderDAO.updateOrder(orderSection.getSelectedOrder().getOrderNumber(), status);
									panelCenter.revalidate();
									break;
								case 2:
									System.out.println("Geserveerd");
									status = 4;
									barmanager.getHashTable(activeTable).getSpecificOrder(orderSection.getSelectedOrder().getOrderNumber()).setOrderStatus("Geserveerd");
									orderDAO.updateOrder(orderSection.getSelectedOrder().getOrderNumber(), status);
									panelCenter.revalidate();
									break;
								default:
									break;
							}
							panelCenter.revalidate();
							orderSection.clearSelectedOrder();
						} catch (Exception f) {
							JOptionPane.showMessageDialog(BarGUI.this, f.getMessage(), "Fout", JOptionPane.ERROR_MESSAGE);
							Logger logger = Logger.getAnonymousLogger();
							logger.log(Level.SEVERE, "An exception was thrown in BarGUI at CompleteOrderHandler", f);
						}
					} else {
						JOptionPane.showMessageDialog(BarGUI.this, "Selecteer een bestelling", "Fout", JOptionPane.ERROR_MESSAGE);
					}
				}
			} else {
				JOptionPane.showMessageDialog(BarGUI.this, "Afronden Mislukt. Log eerst in.", "Fout", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// Inner class
	// Will log a employee in and can log a employee out
	// And add / remove them from the employeelist automatically
	class LogInOutHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			// Put the employeeList from the loginmanager in 'lijstje'
			employeeList = new HashSet<Employee>();
			employeeList = loginmanager.getEmployees();

			try {
				if (e.getSource() == loginButton) {
					if (!logInOutField.getText().equals("")) {
						// Dit werkt
						int employeeCode = Integer.parseInt(logInOutField.getText());

						// System.out.println(employeeCode);
						Employee employee = loginmanager.findEmployee(employeeCode);

						// Remove all items from the JComboBox
						employeeBox.removeAllItems();

						for (Employee ef : employeeList) {
							employeeBox.addItem(ef);
						}

						logInOutField.setText("");
					} else {
						JOptionPane.showMessageDialog(panelCenter, "Voer eerst u medewerkersnummer in om in te kunnen loggen", "Foutmelding: incorrecte invoer", JOptionPane.ERROR_MESSAGE);
						logInOutField.setText("");
					}
				}

				if (e.getSource() == logoutButton) {
					if (employeeBox.getItemCount() != 0 && logInOutField.getText().equals("")) {
						Employee employee = (Employee) employeeBox.getSelectedItem();
						boolean removeSuccess = loginmanager.removeEmployeeFromList(employee);

						// Remove all items from the JComboBox
						employeeBox.removeAllItems();

						for (Employee ef : employeeList) {
							employeeBox.addItem(ef);
						}
						logInOutField.setText("");
					} else if (employeeBox.getItemCount() != 0 && !logInOutField.getText().equals("")) {
						JOptionPane.showMessageDialog(panelCenter, "Het medewerkersveld moet leeg zijn om uit te kunnen loggen", "Foutmelding: incorrecte invoer", JOptionPane.ERROR_MESSAGE);
						logInOutField.setText("");
					} else {
						JOptionPane.showMessageDialog(panelCenter, "Er zijn nog geen medewerkers ingelogd, log eerst in", "Foutmelding: incorrecte invoer", JOptionPane.ERROR_MESSAGE);
						logInOutField.setText("");
					}
				}
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(panelCenter, "Medewerkerscode wordt niet herkend, deze bestaat alleen uit cijfers!", "Foutmelding: incorrecte invoer", JOptionPane.ERROR_MESSAGE);
				logInOutField.setText("");
			} catch (AlreadyLoggedInException alie) { // not logged in yet
				JOptionPane.showMessageDialog(panelCenter, "U bent al aangemeld", "Foutmelding: Aanmelden", JOptionPane.ERROR_MESSAGE);
				logInOutField.setText("");
			} catch (NoDBConnectionException ndce) { // not logged in yet
				JOptionPane.showMessageDialog(panelCenter, "Geen connectie met de database, probeer later opnieuw", "Foutmelding: Aanmelden", JOptionPane.ERROR_MESSAGE);
				logInOutField.setText("");
			} catch (WrongEmployeeNumberException wene) {
				JOptionPane.showMessageDialog(panelCenter, "Het ingevoerde medewerkersnummer bestaat niet", "Foutmelding: Aanmelden", JOptionPane.ERROR_MESSAGE);
				logInOutField.setText("");
			}
		}
	}


	class SignupHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {


			panelCenter.removeAll();
			System.out.println("show signup area");
			RegisterSection rs = new RegisterSection();
			panelCenter.add(rs);
			panelCenter.revalidate();
		}
	}
}

