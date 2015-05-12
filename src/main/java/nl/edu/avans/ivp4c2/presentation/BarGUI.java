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
import java.util.ArrayList;
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

import nl.edu.avans.ivp4c2.domain.Order;
import nl.edu.avans.ivp4c2.domain.Table;
import nl.edu.avans.ivp4c2.manager.BarManager;


public class BarGUI extends JPanel {

	// Attributes
	private JLabel logo;
	private Font font;

	// Buttons
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

	/*
	 * Initialize JTable to fill rightPanel and leftPanel. By doing so, we make
	 * sure there is always an object present in the layout
	 */
	private JTable tableLeft = new JTable();
	private JTable tableRight = new JTable();
	private JPanel rightPanel = new JPanel(); // rightTable will be set here
	private JPanel leftPanel = new JPanel(); // leftTable will be set here

	private BarManager barmanager;

	public BarGUI(BarManager barmanager) {
		this.barmanager = barmanager;

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
		panelCenter.setLayout(new GridLayout(1, 2)); // Has 1 row and two
														// columns. leftPanel
														// and rightPanel will
														// be set in these
														// columns

		// Confire left and right panel
		leftPanel.setLayout(new GridLayout(1, 1));
		rightPanel.setLayout(new GridLayout(1, 1));
		leftPanel.setBackground(Color.WHITE);
		rightPanel.setBackground(Color.WHITE);
		panelCenter.add(leftPanel);
		panelCenter.add(rightPanel);

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
		signupButton = new JButton("Inschrijven");
		signupButton.setBackground(Color.decode("#DFDFDF"));
		signupButton.setFont(font);
		signupButton.setBorder(BorderFactory.createEtchedBorder());
		completeOrderButton = new JButton("Afronden");
		completeOrderButton.setBackground(Color.decode("#DFDFDF"));
		completeOrderButton.setFont(font);
		completeOrderButton.setBorder(BorderFactory.createEtchedBorder());
		tableHistoryButton = new JButton("Geschiedenis");
		tableHistoryButton.setBackground(Color.decode("#DFDFDF"));
		tableHistoryButton.setFont(font);
		tableHistoryButton.setBorder(BorderFactory.createEtchedBorder());
		signInOutButton = new JButton("Aan/afmelden");
		signInOutButton.setBackground(Color.decode("#DFDFDF"));
		signInOutButton.setFont(font);
		signInOutButton.setBorder(BorderFactory.createEtchedBorder());
		
		// Items added to panel West
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

		}, 0, 3, TimeUnit.SECONDS);
	}

	// Methods

	/*
	 * Using new method to set table status. Since a table can only have the
	 * status 'Bestelling', 'Afrekenen' or 'Leeg', We can anticipate this and
	 * use three methods to set the tableButton colors accordingly
	 */
	public void setTableStatus() {

		ArrayList<Table> tableStatusOrder = new ArrayList<Table>();
		ArrayList<Table> tableStatusPayment = new ArrayList<Table>();
		ArrayList<Table> tableStatusEmpty = new ArrayList<Table>();
		tableStatusEmpty = barmanager.getEmptyTables();
		tableStatusOrder = barmanager.getActiveTables();
		tableStatusPayment = barmanager.getPaymentTables();
		Time orderTime = null;

		// Set table status empty
		for (Table te : tableStatusEmpty) {
			int tb = te.getTableNumber();
			tableButton[tb].setBackground(Color.decode("#DFDFDF"));
		}

		// Set table status Order
		for (Table to : tableStatusOrder) {
			int tb = to.getTableNumber();
			
			for(Order o: to.getOrders()) {
//				Time newOrderTime = o.getOrderTime();
//				if(newOrderTime.after(orderTime)) {
//					orderTime = newOrderTime;
//					tableButton[tb].setBackground(Color.CYAN);
//				} else {
//					tableButton[tb].setBackground(Color.GREEN);
//				}
//				
				System.out.println(o.getOrderTime().toString());
			}
			tableButton[tb].setBackground(Color.GREEN);
			System.out.println("hallooo");
			repaint();
		}

		// Set table status Payment
		for (Table tp : tableStatusPayment) {
			int tb = tp.getTableNumber();
			tableButton[tb].setBackground(Color.RED);
			repaint();
		}

	}

	// Method to create JTable
	public static DefaultTableModel buildTableModel(ResultSet rs)
			throws SQLException {

		ResultSetMetaData metaData = (ResultSetMetaData) rs.getMetaData();

		// Gets column names from ResultSet
		Vector<String> columnNames = new Vector<String>();
		int columnCount = metaData.getColumnCount();
		for (int column = 1; column <= columnCount; column++) {
			columnNames.add(metaData.getColumnName(column));
		}

		// data of the table
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();

		while (rs.next()) {
			Vector<Object> vector = new Vector<Object>();
			for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
				vector.add(rs.getObject(columnIndex));
			}
			data.add(vector);
		}

		return new DefaultTableModel(data, columnNames);
	}

	// Inner classes
//	class LeftMenuHandler implements ActionListener {
//		public void actionPerformed(ActionEvent e) {
//			if (e.getSource() == barOrderButton) {
//				barOrderButton.setBackground(Color.DARK_GRAY);
//				barOrderButton.setForeground(Color.WHITE);
//				leftPanel.removeAll();
//				rightPanel.removeAll();
//			} else {
//				barOrderButton.setForeground(Color.black);
//				barOrderButton.setBackground(Color.decode("#DFDFDF"));
//			}
//			if (e.getSource() == kitchenOrderButton) {
//				kitchenOrderButton.setBackground(Color.DARK_GRAY);
//				kitchenOrderButton.setForeground(Color.WHITE);
//				leftPanel.removeAll();
//				rightPanel.removeAll();
//				try {
//					JTable kitchenTable = new JTable(
//							buildTableModel(barmanager.getTableOrder()));
//					leftPanel.add(new JScrollPane(kitchenTable));
//					panelCenter.revalidate();
//				} catch (SQLException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			} else {
//				kitchenOrderButton.setForeground(Color.black);
//				kitchenOrderButton.setBackground(Color.decode("#DFDFDF"));
//			}
//		}
//	}

//	class KitchenOrderHandler implements ActionListener {
//		public void actionPerformed(ActionEvent e) {
//			kitchenOrderButton.setBackground(Color.DARK_GRAY);
//			kitchenOrderButton.setForeground(Color.WHITE);
//			repaint();
//		}
//	}

	class TableButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			rightPanel.removeAll();
			leftPanel.removeAll();

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

					// Setup center - left

					try {

						tableLeft = new JTable(
								buildTableModel(barmanager.getTableOrders(tableNumber)));
						tableLeft.setBorder(BorderFactory.createEtchedBorder());
						tableLeft.getTableHeader().setReorderingAllowed(false); // Added

						// Add mouse listener
						tableLeft.addMouseListener(new MouseAdapter() {

							@Override
							public void mouseClicked(final MouseEvent e) {
								if (e.getClickCount() == 1) {
									final JTable target = (JTable) e
											.getSource(); // Get left JTable
									final int row = target.getSelectedRow(); // Get
																				// row
									final int column = target
											.getSelectedColumn(); // Get column
									int value = (Integer) target.getValueAt(
											row, column); // Get value from cell

									/*
									 * Now that we have the orderNumber, we can
									 * create the right table
									 */

									try {
										rightPanel.removeAll();

										tableRight = new JTable(
												buildTableModel(barmanager
														.getOrders(tableNumber,
																value)));
										tableRight.setBorder(BorderFactory
												.createEtchedBorder());
										tableRight.setEnabled(false); // Disable
																		// user
																		// input
										rightPanel.add(
												new JScrollPane(tableRight))
												.setBackground(Color.WHITE);
										rightPanel.revalidate();
									} catch (SQLException f) {
										// TODO Auto-generated catch block
										f.printStackTrace();
									}
								}
							}
						});
						leftPanel.add(new JScrollPane(tableLeft))
								.setBackground(Color.WHITE);
						leftPanel.revalidate();
					} catch (SQLException f) {
						// TODO Auto-generated catch block
						f.printStackTrace();
					}

				} else {
					TitledBorder topBorderInactive = BorderFactory
							.createTitledBorder("");
					topBorderInactive.setBorder(BorderFactory
							.createLineBorder(Color.decode("#DFDFDF")));
					topBorderInactive.setTitlePosition(TitledBorder.TOP);
					tableButton[tb].setBorder(topBorderInactive);
					tableButton[tb].setBorder(BorderFactory
							.createEtchedBorder());
				}

			}
			revalidate();

		}
	}

}