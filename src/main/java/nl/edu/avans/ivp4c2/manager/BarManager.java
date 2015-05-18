package nl.edu.avans.ivp4c2.manager;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.sun.org.apache.xpath.internal.SourceTree;
import nl.edu.avans.ivp4c2.datastorage.*;
import nl.edu.avans.ivp4c2.domain.Order;
import nl.edu.avans.ivp4c2.domain.Table;

public class BarManager {

	// private ArrayList<Order> orders;
	private HashMap<Integer, Table> tableHashmap;
	private ArrayList<Table> orderTables; // Arraylist for all order tables
	private ArrayList<Table> paymentTables; // ArrayList for all payment tables
	private ArrayList<Table> emptyTables; // ArrayList for all empty tables
	private TableDAO tabledao;
	private OrderDAO orderdao;
	private int destination;

	public BarManager() {
		// orders = new ArrayList<Order>();
		tableHashmap = new HashMap<Integer, Table>();
		orderTables = new ArrayList<Table>();
		paymentTables = new ArrayList<Table>();
		emptyTables = new ArrayList<Table>();
		tabledao = new TableDAO();
		orderdao = new OrderDAO();
		getActiveTablesDAO();
		getPaymentTablesDAO();
		getEmptyTablesDAO();

		/*
		 * Calls the three DAO table getters every 3 seconds. These methods are
		 * used to keep the ArrayLists up to date. These ArrayList can be used
		 * by calling the three ArrayList getters. Other methods that should be
		 * called every X seconds should be added here too
		 */
		ScheduledExecutorService exec = Executors
				.newSingleThreadScheduledExecutor();
		exec.scheduleAtFixedRate(new Runnable() {

			public void run() {
				getActiveTablesDAO();
				getPaymentTablesDAO();
				getEmptyTablesDAO();
			}

		}, 0, 5, TimeUnit.SECONDS);
	}

	/**
	 * Hashmap that return table object, used to fill the JTable
	 * @return Table
	 */
	public Table getHashTable(int tableNumber) {
		Table tempTable;
		tempTable = tableHashmap.get(tableNumber);
		return tempTable;
	}

	/*
	 * ArrayList getters
	 * 
	 * These methods return an ArrayList with Table objects
	 */
	public ArrayList<Table> getActiveTables() {
		return orderTables;
	}

	public ArrayList<Table> getPaymentTables() {
		return paymentTables;
	}

	public ArrayList<Table> getEmptyTables() {
		return emptyTables;
	}



	/*
	 * DAO table getters
	 * 
	 * These methods update the three ArrayLists every three seconds by
	 * reteiving data from the database Each list is cleared before new data is
	 * added. By doing so, a table can never be in two or more lists
	 *
	 */


	// Vult alleen de Arraylists
	public ArrayList<Table> getActiveTablesDAO() {
		orderTables.clear();
		for (Table t : tabledao.getTableOrder()) {
			orderTables.add(t);
			tableHashmap.put(t.getTableNumber(), t); 	// add active tables to the hashmap
		}
		return orderTables;
	}

	public ArrayList<Table> getPaymentTablesDAO() {
		paymentTables.clear();
		for (Table t : tabledao.getTablePayment()) {
			paymentTables.add(t);
		}
		return paymentTables;
	}

	public ArrayList<Table> getEmptyTablesDAO() {
		emptyTables.clear();
		for (Table t : tabledao.getTableEmpty()) {
			emptyTables.add(t);
		}
		return emptyTables;
	}


}
