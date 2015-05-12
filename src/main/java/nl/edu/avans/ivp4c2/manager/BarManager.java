package nl.edu.avans.ivp4c2.manager;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import nl.edu.avans.ivp4c2.datastorage.*;
import nl.edu.avans.ivp4c2.domain.Order;
import nl.edu.avans.ivp4c2.domain.Table;

public class BarManager {

	// private ArrayList<Order> orders;
	private ArrayList<Table> orderTables; // Arraylist for all order tables
	private ArrayList<Table> paymentTables; // ArrayList for all payment tables
	private ArrayList<Table> emptyTables; // ArrayList for all empty tables
	private TableDAO tabledao;

	public BarManager() {
		// orders = new ArrayList<Order>();
		orderTables = new ArrayList<Table>();
		paymentTables = new ArrayList<Table>();
		emptyTables = new ArrayList<Table>();
		tabledao = new TableDAO();
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

		}, 0, 3, TimeUnit.SECONDS);
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
	 */

	public ArrayList<Table> getActiveTablesDAO() {
		orderTables.clear();
		for (Table t : tabledao.getTableOrder()) {
			orderTables.add(t);
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
