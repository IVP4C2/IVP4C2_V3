package nl.edu.avans.ivp4c2.manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import nl.edu.avans.ivp4c2.datastorage.*;
import nl.edu.avans.ivp4c2.domain.Order;
import nl.edu.avans.ivp4c2.domain.Table;
import nl.edu.avans.ivp4c2.presentation.BarGUI;

import javax.swing.*;

import static javax.swing.JOptionPane.*;

/**
 * Manager which handles all operations regarding orders and tables
 * @author IVP4C2
 */
public class BarManager {
	private HashMap<Integer, Table> tableHashmap; //Used to store all tables
	private HashMap<Integer, ArrayList<Order>> tableHistoryHashMap; //Holds the order history for each table.
	TableDAO tableDAO;

	public BarManager() {
		tableHashmap = new HashMap<Integer, Table>();
		tableDAO = new TableDAO();
		tableHistoryHashMap  = new HashMap<>();
		getOccupiedTablesDAO();
		getPaymentTablesDAO();
		getEmptyTablesDAO();

		/*
		 * Calls the three DAO table getters every 5 seconds. These methods are
		 * used to keep the ArrayLists up to date. These ArrayList can be used
		 * by calling the three ArrayList getters. Other methods that need to be
		 * called every X seconds should be added here too
		 */
		ScheduledExecutorService exec = Executors
				.newSingleThreadScheduledExecutor();
		exec.scheduleAtFixedRate(new Runnable() {
			public void run() {
				getOccupiedTablesDAO();
				getPaymentTablesDAO();
				getEmptyTablesDAO();
			}
		}, 0, 5, TimeUnit.SECONDS);
	}

	/**
	 * Returns a Table object from the tableHashMap, used to fill the JTable
	 * @param tableNumber is the number of the table
	 * @return Table of given tableNumber
	 */
	public Table getHashTable(int tableNumber) {
		Table tempTable;
			if(!tableHashmap.containsKey(tableNumber)) {
				tempTable = null;
			} else {
				tempTable = tableHashmap.get(tableNumber);
			}
		return tempTable;
	}


	/**
	 * Adds a Served Order to the HashMap.
	 * @param tableNumber the table number to be used as key
	 * @param order the order to add
	 */
	public void addOrderToHistory(int tableNumber, Order order) {
		ArrayList<Order> tempList = new ArrayList<Order>();
		tempList.add(order);
		if (tableHistoryHashMap.containsKey(tableNumber)) {
			for(Order o : tableHistoryHashMap.get(tableNumber)) {
				tempList.add(o);
			}
			tableHistoryHashMap.replace(tableNumber, tempList);
		} else {
			tableHistoryHashMap.put(tableNumber, tempList);
		}
	}

	/**
	 * Returns an ArrayList containing the Order previously done by the Table and which are already served
	 * @param table the table from which to get the completed orders
	 * @return returns an ArrayList with all the completed Orders for the table
	 */
	public ArrayList<Order> getOrderHitory(Table table) {
		ArrayList<Order> orderHistory = new ArrayList<>();
		for(Map.Entry<Integer, ArrayList<Order>> oList : tableHistoryHashMap.entrySet()) {
			if(oList.getKey() == table.getTableNumber()) {
				for (Order order : oList.getValue()) {
					orderHistory.add(order);
				}
			}
		}
		return orderHistory;
	}


	public void removeTable(int tableNumber) throws Exception{
		try {
			tableHashmap.remove(tableNumber);
		} catch (Exception e) {
			Logger logger = Logger.getAnonymousLogger();
			logger.log(Level.SEVERE, "an exception was thrown in the BarManager", e);
		}
	}


	/*
	 * These methods return a List with Table objects.
	 * These Lists are stored in the system memory
	 */

	/**
	 * Returns the tables which have the 'Bezet' status in an ArrayList
	 * @return occupied tables
	 */
	public List<Table> getOccupiedTables() {
		ArrayList<Table> arrayList = new ArrayList<Table>();
		for(Map.Entry<Integer, Table> e : tableHashmap.entrySet()) {
			if(e.getValue().getTableStatus().equals("Bezet")) {
				arrayList.add(e.getValue());
			}
		}
		return arrayList;
	}

	/**
	 * Returns the tables which have the 'Afrekenen' status in an ArrayList
	 * @return payment tables
	 */
	public List<Table> getPaymentTables() {
		ArrayList<Table> arrayList = new ArrayList<Table>();
		for(Map.Entry<Integer, Table> e : tableHashmap.entrySet()) {
			if(e.getValue().getTableStatus().equals("Afrekenen")) {
				arrayList.add(e.getValue());
			}
		}
		return arrayList;
	}

	/**
	 * Return the tables which have the 'Leeg' status in an ArrrayList
	 * @return
	 */
	public List<Table> getEmptyTables() {
		ArrayList<Table> arrayList = new ArrayList<Table>();
		for(Map.Entry<Integer, Table> e : tableHashmap.entrySet()) {
			if(e.getValue().getTableStatus().equals("Leeg")) {
				arrayList.add(e.getValue());
			}
		}
		return arrayList;
	}



	/*DAO table getters
	 * 
	 * These methods update the three Lists stored in the system memory every 5 seconds by
	 * retrieving data from the database.
	 */

	/**
	 * Retrieves all tables from the database which have the status 'Bezet'
	 * Also adds these tables to the tableHashmap
	 */
	public void getOccupiedTablesDAO() {
		for (Table t : tableDAO.getTableOccupied()) {
			if(tableHashmap.containsKey(t.getTableNumber())) {
				tableHashmap.replace(t.getTableNumber(), t);
			} else {
				tableHashmap.put(t.getTableNumber(), t);
			} 	// add active tables to the hashmap
		}
	}

	/**
	 * Retrieves all tebles from the database which have the status 'Afrekenen'
	 * Also adds these tables to the tableHashMap
	 */
	public void getPaymentTablesDAO() {
		for (Table t : tableDAO.getTablePayment()) {
			if(tableHashmap.containsKey(t.getTableNumber())) {
				tableHashmap.replace(t.getTableNumber(), t);
			} else {
				tableHashmap.put(t.getTableNumber(), t);
			}
		}
	}

	/**
	 * Retrieves all tebles from the database which have the status 'Leeg'
	 */
	public void getEmptyTablesDAO() {
		for (Table t : tableDAO.getTableEmpty()) {
			if(tableHashmap.containsKey(t.getTableNumber())) {
				tableHashmap.replace(t.getTableNumber(), t);
			} else {
				tableHashmap.put(t.getTableNumber(), t);
			}
		}
	}
}
