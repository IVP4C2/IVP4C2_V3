package nl.edu.avans.ivp4c2.presentation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
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
		BufferedImage image = null;
		try {
			image = ImageIO
					.read(new File("src/main/resources/logo_resized.jpg"));
		} catch (IOException ex) {
			Logger.getLogger(BarGUI.class.getName())
					.log(Level.SEVERE, null, ex);
		}
		panelNorthLeft.add(new JLabel(new ImageIcon(image)));

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
		//HashMap<Integer, Employee> employeeNames = loginmanager.getEmployees();

		signupButton = new JButton("Inschrijven");
		signupButton.setBackground(Color.decode("#DFDFDF"));
		signupButton.setFont(font);
		signupButton.setBorder(BorderFactory.createEtchedBorder());
		completeOrderButton = new JButton("Afronden");
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
        ArrayList<Table> tableStatusOrder = barmanager.getOccupiedTables(); //Contains all 'Bezet' tables
        ArrayList<Table> tableStatusPayment = barmanager.getPaymentTables(); //Contains all 'Afrekenen' tables
        ArrayList<Table> tableStatusEmpty = barmanager.getEmptyTables(); //Contains all 'Leeg' tables
        Timestamp longestBarOrder = null; //Used to store the Date of the longest waiting bar order
        Timestamp longestKitchenOrder = null; //Used to store the Date of the longest waiting kitchen order
        Timestamp longestPaymentRequest = null; //Used to store the Date of the longest waiting payment request
        HashSet<Integer> barOrderTables = new HashSet<>();  //reference to table which get a green color
        HashSet<Integer> kitchenOrderTables = new HashSet<>(); //reference to table which get a yellow color
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
					if (!table.equals(null)) {
                        /**/
						if (table.getTableStatus().equals("Bezet") || table.getTableStatus().equals("Hulp")) {
							panelCenter.removeAll();
							panelCenter.add(orderSection.getTableLeft(table, panelCenter));
                            activeTable = tb;
							panelCenter.revalidate();
						} else if (table.getTableStatus().equals("Afrekenen")) {
							System.out.println("Status afrekenen");
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
				}
				else {
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
				System.out.println(employeeBox.getSelectedItem().toString());
				try {
					paymentManager.completePayment(activeTable);
					barmanager.removeTable(activeTable);
					tableButton[activeTable].setBackground(Color.decode("#DFDFDF"));
					revalidate();
					panelCenter.removeAll();
					JOptionPane.showMessageDialog(BarGUI.this, "Rekening Succesvol afgerond", "Rekening Afgerond", JOptionPane.INFORMATION_MESSAGE);

				} catch (Exception f) {
					JOptionPane.showMessageDialog(BarGUI.this, "Bestelling kon niet worden afgerond", "Fout", JOptionPane.ERROR_MESSAGE);
				}
            }
            else {
                JOptionPane.showMessageDialog(BarGUI.this, "Afronden Mislukt. Log eerst in.", "Fout", JOptionPane.ERROR_MESSAGE);
            }
        }
	}

	/**
	 * The Login and logout handler
	 * This handler makes it possible to let employees login and also logout
	 * If the employee fill in his employeecode in the textfield and click on login the employee will be logged in
	 * When the employee also fill in his employeecode and click on logout the employee will be logged out
	 * For protection whe are using codes that only the employee should know
	 */
	class LogInOutHandler extends Component implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				// A employee can be found with his employeenumber and when a employee is found
				int employeeCode = Integer.parseInt(logInOutField.getText());
				// This method will check if a employee excists in the database and it exists it will be loaded in the memory.
				Employee empl = loginmanager.findEmployee(employeeCode);
				if (empl != null) {
					employeeBox.removeAllItems();
					// All the employees will be stored in a list called employees.
					HashMap<Integer, Employee> employees;
					employees = loginmanager.getEmployees();
					// This loop will check if a employee is in the list,
					// if a employee is above added to the list it will be added to the JComboBox
					for (Map.Entry<Integer, Employee> entry : employees.entrySet()) {
						Employee em = entry.getValue();
						employeeBox.addItem(em);
					}
					// If a employee is selected in the JComboBox and there is clicked on the logoutButton a employee will logout.
					// If a employee number is filled in the textField and there is clicked on the logoutButton the employee will also logout.
					if (e.getSource() == logoutButton) {
						int input = Integer.parseInt(logInOutField.getText());
						employees.remove(input);
						employeeBox.removeAllItems();
						for (Map.Entry<Integer, Employee> entry : employees.entrySet()) {
							Employee em = entry.getValue();
							employeeBox.addItem(em);
						}
					}
					logInOutField.revalidate();
					logInOutField.setText(null);
				} else {
					JOptionPane.showMessageDialog(LogInOutHandler.this, "Het ingevoerde nummer wordt niet herkend, probeer het opnieuw! ", "Foutmelding", JOptionPane.ERROR_MESSAGE);
				}
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(LogInOutHandler.this, "Vul uw medewerkerscode in", "Foutmelding", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}

