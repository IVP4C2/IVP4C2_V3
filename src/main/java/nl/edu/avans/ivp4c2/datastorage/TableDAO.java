package nl.edu.avans.ivp4c2.datastorage;

import nl.edu.avans.ivp4c2.domain.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * This DAO retrieves data from the database and stores it in Table objects
 * These table objects contain an ArrayList with Orders. These Orders contain an ArrayList with Products.
 * The Table objects can be accessed by using the BarManager.
 * To create these Order objects, this class used the OrderDAO.
 * To create the product for an Order, the OrderDAO uses ProductDAo
 * This class can be seen as a master class from where other DAO's get called.
 * 
 *  @author IVP4C2
 */

public final class TableDAO {
	/*These integer will be used to complete the SQL select statement
     * Since the only variable in the SQL statment is the table status and the status is an ENUM, 
     * we can use final Strins to complete the select statement*/
	private static final int TABLE_OCCUPIED = 1; //Represents status 'Bezet'
	private static final int TABLE_PAYMENT = 2; //Represents status 'Afrekenen'
	private static final int TABLE_EMPTY = 4; //Represents status 'Leeg'
	private boolean connectionFailPopupShown = false;

	public TableDAO() {
		// Nothing to be initialized. This is a stateless class. Constructor
		// has been added to explicitely make this clear.
	}

	/**
	 * Retreives all tables which have the 'Bezet' status.
	 * Returns these tables in an ArrayList
	 * @return ArrayList containing Table object
	 */
    public final ArrayList<Table> getTableOccupied() {
    	ArrayList<Table> fetchedTables = new ArrayList<Table>();
    	fetchedTables = getTable(TABLE_OCCUPIED);
    	return fetchedTables;
    }

	/**
	 * Retreives all tables which have the 'Afrekenen' status.
	 * Returns these tables in an ArrayList
	 * @return ArrayList containing Table object
	 */
    public final ArrayList<Table> getTablePayment() {
    	ArrayList<Table> fetchedTables = new ArrayList<Table>();
    	fetchedTables = getTable(TABLE_PAYMENT);
    	return fetchedTables;
    }

	/**
	 * Retreives all tables which have the 'Leeg' status.
	 * Returns these tables in an ArrayList
	 * @return ArrayList containing Table object
	 */
    public final ArrayList<Table> getTableEmpty() {
    	ArrayList<Table> fetchedTables = new ArrayList<Table>();
    	fetchedTables = getTable(TABLE_EMPTY);
    	return fetchedTables;
    }
	
	
	/**Retrieves all tables from the database. The retrieved values get stored in Table objects.
	*A table object contains an ArrayList with Orders. An Order contains an ArrayList with Products
	*@param status
	*@return ArrayList containing Table Objectts
	*/
	private final ArrayList<Table> getTable(int status) {
		
		ArrayList<Table> tables = new ArrayList<Table>();
		//Open db connection
		DatabaseConnection connection = new DatabaseConnection();
		if(connection.openConnection()) {
			//connection opened succesfully
			//execute SQL statement to retrieve Tables
			ResultSet resultset = connection.executeSQLSelectStatement(
	                "SELECT `t`.`table_number`, `ts`.`status` FROM `table_status` `ts` " +
							"INNER JOIN `table` `t` ON `ts`.`table_status_id` = `t`.`fk_table_status_id` " +
							"WHERE `ts`.`table_status_id` = '"+status+"';");

				//If there is a result
	            if(resultset != null)
	            {
	                try
	                {
	                    while(resultset.next())
	                    {
	                    	//Create new Table Object for each record
							Table newTable = new Table(
									resultset.getInt("table_number"),
	                                resultset.getString("status"));
	                       		//Check if status is 'Bezet' or 'Arekenen'. If so, there is an order which can be retrieved from the database
		                       if(resultset.getString("status").equals("Bezet") || resultset.getString("status").equals("Afrekenen")) {
		                    	   OrderDAO orderDAO = new OrderDAO(); //Create new OrderDAO 
		                    	   ArrayList<Order> newOrder = OrderDAO.getTableOrder(resultset.getInt("table_number")); //Returns ArrayList with orders for tableNumber
		                    	   for(Order o : newOrder) { //Add orders to the new table
		                    		   newTable.addOrder(o);
		                    	   }
		                       }
	                        tables.add(newTable); //Add new table to the ArrayList
	                    }
	                }
	                catch(SQLException e)
	                {
	                    System.out.println(e);
	                    tables = null;
						connectionFailPopupShown = true;
	                }
	            }
			connection.closeConnection();
		}
		return tables;
	}
}
