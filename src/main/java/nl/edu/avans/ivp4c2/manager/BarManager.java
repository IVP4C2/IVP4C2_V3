package nl.edu.avans.ivp4c2.manager;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.sun.org.apache.xpath.internal.SourceTree;
import nl.edu.avans.ivp4c2.datastorage.*;
import nl.edu.avans.ivp4c2.domain.Order;
import nl.edu.avans.ivp4c2.domain.Table;

/**
 * Manager which handles all operations regarding orders and tables
 * @author IVP4C2
 */
public class BarManager {

	// private ArrayList<Order> orders;
	private HashMap<Integer, Table> tableHashmap;
	private ArrayList<Table> occupiedTables; // ArrayList for all occupied tables
	private ArrayList<Table> paymentTables; // ArrayList for all payment tables
	private ArrayList<Table> emptyTables; // ArrayList for all empty tables
	private TableDAO tabledao;

	public BarManager() {
		tableHashmap = new HashMap<Integer, Table>();
		occupiedTables = new ArrayList<Table>();
		paymentTables = new ArrayList<Table>();
		emptyTables = new ArrayList<Table>();
		tabledao = new TableDAO();

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
	 * Hashmap which returns a Table object, used to fill the JTable
	 * @param tableNumber
	 * @return Table
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


	/* ArrayList getters
	 *
	 * These methods return an ArrayList with Table objects.
	 * These ArrayLists are stored in the system memory
	 */

	/**
	 * Returns the tables which have the 'Bezet' status in an ArrayList
	 * @return occupied tables
	 */
	public ArrayList<Table> getOccupiedTables() {
		return occupiedTables;
	}

	/**
	 * Returns the tables which have the 'Afrekenen' status in an ArrayList
	 * @return payment tables
	 */
	public ArrayList<Table> getPaymentTables() {
		return paymentTables;
	}

	/**
	 * Return the tables which have the 'Leeg' status in an ArrrayList
	 * @return
	 */
	public ArrayList<Table> getEmptyTables() {
		return emptyTables;
	}



	/*DAO table getters
	 * 
	 * These methods update the three ArrayLists stored in the system memory every 5 seconds by
	 * retrieving data from the database. Each list is cleared before new data is
	 * added. By doing so, a table can never be in two or more lists
	 */

	/**
	 * Retrieves all tebles from the database which have the status 'Bezet'
	 * Also adds these tables to the tableHashmap
	 * @return ArrayList<Table>
	 */
	public ArrayList<Table> getOccupiedTablesDAO() {
		occupiedTables.clear();
		for (Table t : tabledao.getTableOccupied()) {
			occupiedTables.add(t);
			tableHashmap.put(t.getTableNumber(), t); 	// add active tables to the hashmap
		}
		return occupiedTables;
	}

	/**
	 * Retrieves all tebles from the database which have the status 'Afrekenen'
	 * Also adds these tables to the tableHashMap
	 * @return ArrayList<Table>
	 */
	public ArrayList<Table> getPaymentTablesDAO() {
		paymentTables.clear();
		for (Table t : tabledao.getTablePayment()) {
			paymentTables.add(t);
			tableHashmap.put(t.getTableNumber(), t);
		}
		return paymentTables;
	}

	/**
	 * Retrieves all tebles from the database which have the status 'Leeg'
	 * @return ArrayList<Table>
	 */
	public ArrayList<Table> getEmptyTablesDAO() {
		emptyTables.clear();
		for (Table t : tabledao.getTableEmpty()) {
			emptyTables.add(t);

		}
		return emptyTables;
	}
}
