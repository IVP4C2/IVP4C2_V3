package nl.edu.avans.ivp4c2.presentation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.ResultSetMetaData;

import nl.edu.avans.ivp4c2.domain.*;
import nl.edu.avans.ivp4c2.manager.BarManager;
import nl.edu.avans.ivp4c2.manager.LoginManager;
import nl.edu.avans.ivp4c2.manager.PaymentManager;
import sun.plugin2.message.ShowStatusMessage;


public class BarGUI extends JPanel {

	// Attributes
	private JLabel logo;
	private Font font;

	// Buttons
	private JComboBox<Employee> employeeBox;
	private JButton completeOrderButton;
	private JButton tableHistoryButton;
	private JButton signupButton;
	private JButton signInOutButton;


	private final int AMOUNT_OF_TABLEBUTTONS = 11;
	private JButton[] tableButton;

	// Booleans
	private boolean green;
	private boolean yellow;

	// Panels
	private JPanel panelNorth;
	private JPanel panelNorthLeft;
	private JPanel panelNorthRight;
	private JPanel panelWest;
	private JPanel panelCenter;
	private int activeTable;

	/*
	 * Initialize JTable to fill rightPanel and leftPanel. By doing so, we make
	 * sure there is always an object present in the layout
	 */
	private PaymentManager paymentManager;

	private BarManager barmanager;

	public BarGUI(BarManager barmanager) {
		this.barmanager = barmanager;
		LoginManager loginmanager = new LoginManager();

		setLayout(new BorderLayout());

		Font font = new Font("SansSerif", Font.PLAIN, 24);

		// create new panels
		panelNorth = new JPanel();
		panelNorthLeft = new JPanel();
		panelNorthRight = new JPanel();
		panelWest = new JPanel();
		// panelEast = new JPanel();
		panelCenter = new JPanel();
		panelCenter.setBackground(Color.WHITE);

		panelNorth.setLayout(new BorderLayout()); // Added
		// Removed panelNorth gridlayout, interfered with earlier declared
		// BorderLayout
		panelNorthLeft.setLayout(new GridLayout(1, 2));
		panelNorthLeft.setSize(600, 200);
		panelNorthRight.setLayout(new GridLayout(2, 5));
		panelWest.setLayout(new GridLayout(4, 1));
		// panelEast.setLayout(new BorderLayout());
		panelCenter.setLayout(new GridLayout(1, 1)); // Has 1 row and two
		// columns. leftPanel
		// and rightPanel will
		// be set in these
		// columns

		// Confire left and right panel

//		panelCenter.add(leftPanel);
//		panelCenter.add(rightPanel);

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
		JLabel logo = new JLabel(new ImageIcon(image));

		// Array with the ten table buttons
		tableButton = new JButton[AMOUNT_OF_TABLEBUTTONS];
		for (int tb = 1; tb <= 10; tb++) {
			tableButton[tb] = new JButton("" + tb);
			tableButton[tb].setBackground(Color.decode("#DFDFDF"));
			tableButton[tb].addActionListener(new TableButtonHandler());
			tableButton[tb].setFont(font);
			tableButton[tb].setBorder(BorderFactory.createEtchedBorder());
			panelNorthRight.add(tableButton[tb]); // Adding tableButtons here.
			// Using a second method for
			// this will be useless
		}

		panelNorthLeft.add(logo);

		// Setup West panel
		employeeBox = new JComboBox<Employee>();
		HashMap<Integer, Employee> employeeNames = loginmanager.getEmployees();

		signupButton = new JButton("Inschrijven");
		signupButton.setBackground(Color.decode("#DFDFDF"));
		signupButton.setFont(font);
		signupButton.setBorder(BorderFactory.createEtchedBorder());
		completeOrderButton = new JButton("Afronden");
		completeOrderButton.setBackground(Color.decode("#DFDFDF"));
		completeOrderButton.setFont(font);
		completeOrderButton.setBorder(BorderFactory.createEtchedBorder());
		completeOrderButton.addActionListener(new CompleteOrderHandler());
		tableHistoryButton = new JButton("Geschiedenis");
		tableHistoryButton.setBackground(Color.decode("#DFDFDF"));
		tableHistoryButton.setFont(font);
		tableHistoryButton.setBorder(BorderFactory.createEtchedBorder());
		signInOutButton = new JButton("Aan/afmelden");
		signInOutButton.addActionListener(new SignInOutHandler());
		signInOutButton.setBackground(Color.decode("#DFDFDF"));
		signInOutButton.setFont(font);
		signInOutButton.setBorder(BorderFactory.createEtchedBorder());

		// Items added to panel West
		panelWest.add(employeeBox);
		panelWest.add(completeOrderButton);
		panelWest.add(tableHistoryButton);
		panelWest.add(signupButton);
		panelWest.add(signInOutButton);

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
	 * Using new method to set table status. Since a table can only have the
	 * status 'Bestelling', 'Afrekenen' or 'Leeg', We can anticipate this and
	 * use three methods to set the tableButton colors accordingly
	 */
	public void setTableStatus() {
		ArrayList<Table> tableStatusOrder = barmanager.getOccupiedTables();
		ArrayList<Table> tableStatusPayment = barmanager.getPaymentTables();
		ArrayList<Table> tableStatusEmpty = barmanager.getEmptyTables();
		Date longestBarOrder = null; //Used to store the Date of the longest waiting bar order
		Date longestKitchenOrder = null; //Used to store the Date of the longest waiting kitchen order
		Date longestPaymentRequest = null; //Used to store the Date of the longest waiting payment request


		// Set table status empty
		for (Table te : tableStatusEmpty) {
			int tb = te.getTableNumber();
//			tableButton[tb].setBackground(Color.decode("#DFDFDF"));
			tableButton[tb].enable(false);
			repaint();
		}

		// Set table status Order
		for (Table to : tableStatusOrder) {
			int tb = to.getTableNumber();
			boolean hasLongestBarOrder = false;
			boolean hasLongestKitchenOrder = false;
			/*The two booleans below are used to set the tableButton color accordingly.
			* If there is an order in the Table's order list with destination 1, hasBarOrder will be set to true
			* If there is an order in the Table's order list with destination 2, hasKitchenOrder will be set to true.*/
			boolean hasBarOrder = false;
			boolean hasKitchenOrder = false;
			for (Order o : to.getOrders()) {
				/*Check the order destination and time for each order.*/
				if (o.getDestination() == 1 && !hasKitchenOrder) {
					if (longestBarOrder == null || o.getOrderTime().before(longestBarOrder)) {
						System.out.println("Before bar");
						tableButton[tb].setBackground(Color.decode("#008A2E"));
						longestBarOrder = o.getOrderTime();
						hasLongestBarOrder = true;
					} else if (!hasLongestBarOrder) {
						tableButton[tb].setBackground(Color.GREEN);
					}
				}
				if (o.getDestination() == 2) {
					if (longestKitchenOrder == null || o.getOrderTime().before(longestKitchenOrder)) {
						System.out.println("Before kitchen");
						tableButton[tb].setBackground(Color.ORANGE);
						longestKitchenOrder = o.getOrderTime();
						hasLongestKitchenOrder = true;
						hasKitchenOrder = true;
					} else if (!hasLongestKitchenOrder) {
						tableButton[tb].setBackground(Color.YELLOW);
						hasKitchenOrder = true;
					}
				}
			}
			repaint();
		}

		// Set table status Payment
		for (Table tp : tableStatusPayment) {
			int tb = tp.getTableNumber();
			tableButton[tb].setBackground(Color.RED);
			repaint();
		}

	}


	// Inner classes


	class TableButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			for (int tb = 1; tb <= 10; tb++) {
				if (e.getSource() == tableButton[tb]) {

					final int tableNumber = tb; // create new integer. Easier to
					// work with.
					// give the active button a border
					TitledBorder topBorder = BorderFactory
							.createTitledBorder("Actief");
					topBorder.setBorder(BorderFactory
							.createLineBorder(Color.black));
					topBorder.setTitlePosition(TitledBorder.TOP);
					tableButton[tb].setBorder(topBorder);
					Table table = null;
					table = barmanager.getHashTable(tableNumber);
					if (!table.equals(null)) {
						if (!table.getTableStatus().equals("Afrekenen")) {
							panelCenter.removeAll();
							JPanel barPanel = new JPanel(new GridLayout(1, 2));
							JPanel leftPanel = new JPanel(new GridLayout(1, 1));
							final JPanel rightPanel = new JPanel(new GridLayout(1, 1));
							leftPanel.setBackground(Color.WHITE);
							rightPanel.setBackground(Color.WHITE);
							barPanel.add(leftPanel);
							barPanel.add(rightPanel);
							panelCenter.add(barPanel);

							final OrderSection orderSection = new OrderSection();
							JTable tableLeft = orderSection.getTableLeft(table);

							// Add mouse listener
							final Table finalTable = table;
							tableLeft.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseClicked(final MouseEvent e) {
									if (e.getClickCount() == 1) {
										rightPanel.removeAll();
										final JTable target = (JTable) e
												.getSource(); // Get left JTable
										final int row = target.getSelectedRow(); //Get row selected by user
										int value = (Integer) target.getValueAt(row, 1); // Get value from cell. 'row' is the row clicked by the user, '1' is the second column
										Order tempOrder = finalTable.getSpecificOrder(value);
										JTable tableRight = orderSection.getTableRight(tempOrder);
										rightPanel.add(
												new JScrollPane(tableRight))
												.setBackground(Color.WHITE);
										rightPanel.revalidate();
									}
								}
							});
							leftPanel.add(new JScrollPane(tableLeft))
									.setBackground(Color.WHITE);
							leftPanel.revalidate();
						} else if (table.getTableStatus().equals("Afrekenen")) {
							System.out.println("Status afrekenen");
							panelCenter.removeAll();
							paymentManager = new PaymentManager();
							PaymentSection paymentSection = new PaymentSection();
							Payment p = paymentManager.getPayment(tableNumber);
							activeTable = tableNumber;
							System.out.println("Afrekenen");
							panelCenter.add(paymentSection.getPaymentPanel(p));
							revalidate();
						}
						else {
							revalidate();
						}
					}
				}
				else {
						TitledBorder topBorderInactive = BorderFactory
								.createTitledBorder("");
						topBorderInactive.setBorder(BorderFactory
								.createLineBorder(Color.decode("#DFDFDF")));
						topBorderInactive.setTitlePosition(TitledBorder.TOP);
						tableButton[tb].setBorder(topBorderInactive);
						tableButton[tb].setBorder(BorderFactory
								.createEtchedBorder());
					revalidate();
				}
			}
			revalidate();
		}
	}

	class SignInOutHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			new LoginGUI();
			signInOutButton.setText("asdfsdaf");
		}
	}

	class CompleteOrderHandler implements  ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(paymentManager.completePayment(activeTable)) {
				barmanager.removeTable(activeTable);
				tableButton[activeTable].setBackground(Color.decode("#DFDFDF"));
				revalidate();
				JPanel completeOrderStatus = new JPanel();
				JLabel label = new JLabel("Rekening afgerond");
				completeOrderStatus.add(label);
				JOptionPane.showMessageDialog(BarGUI.this, completeOrderStatus, "Inlogscherm", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
}
