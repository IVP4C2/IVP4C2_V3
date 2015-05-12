package nl.edu.avans.ivp4c2.datastorage;

import nl.edu.avans.ivp4c2.datastorage.DatabaseConnection;
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

public class TableDAO {
	/*These string will be used to complete the SQL select statement
     * Since the only variable in the SQL statment is the table status and the status is an ENUM, 
     * we can use final Strins to complete the select statement*/
	private static final String TABLE_ORDER = "Bestelling";
	private static final  String TABLE_PAYMENT = "Afrekenen";
	private static final  String TABLE_EMPTY = "Leeg";

	public TableDAO() {
		// Nothing to be initialized. This is a stateless class. Constructor
		// has been added to explicitely make this clear.
	}

    /*get all tables with status order(Bestelling)*/
    public ArrayList<Table> getTableOrder() {
    	ArrayList<Table> fetchedTables = new ArrayList<Table>();
    	fetchedTables = getTable(TABLE_ORDER);
    	return fetchedTables;
    }
    
    /*get all tables with status payment(Afrekenen)*/
    public ArrayList<Table> getTablePayment() {
    	ArrayList<Table> fetchedTables = new ArrayList<Table>();
    	fetchedTables = getTable(TABLE_PAYMENT);
    	return fetchedTables;
    }
    
    /*get all tables with status empty(Leeg)*/
    public ArrayList<Table> getTableEmpty() {
    	ArrayList<Table> fetchedTables = new ArrayList<Table>();
    	fetchedTables = getTable(TABLE_EMPTY);
    	return fetchedTables;
    }
	
	
	/*Retrieves all tables from the database. The retrieved values get stored in Table objects. 
	*A table object contains an ArrayList with Orders. An Order contains an ArrayList with Products
	*@param String status
	*@return ArrayList<Table>
	* */
	public ArrayList<Table> getTable(String status) {
		
		ArrayList<Table> tables = new ArrayList<Table>();
		//Open db connection
		DatabaseConnection connection = new DatabaseConnection();
		if(connection.openConnection()) {
			//connection opened succesfully
			//execute SQL statement to retrieve Tables
			ResultSet resultset = connection.executeSQLSelectStatement(
	                "SELECT tafelStatus, tafelNummer, aantalPersonen FROM `tafel` WHERE tafelStatus = '"+status+"';");

				//If there is a result
	            if(resultset != null)
	            {
	                try
	                {
	                    while(resultset.next())
	                    {
	                    	//Create new Table Object for each record
	                       Table newTable = new Table(
	                                resultset.getInt("tafelNummer"),
	                                resultset.getString("tafelStatus"),
	                                resultset.getInt("aantalpersonen"));
	                       		
	                       		//Check if status is "Bestelling". If so, there is an order which can be retrieved from the database
		                       if(resultset.getString("tafelStatus").equals("Bestelling")) {
		                    	   OrderDAO orderDAO = new OrderDAO(); //Create new OrderDAO 
		                    	   ArrayList<Order> newOrder = orderDAO.getTableOrder(resultset.getInt("tafelNummer")); //Returns ArrayList with orders for tableNumber
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
	                }
	            }

	            connection.closeConnection();
	        }
	        return tables;
	    }
}
