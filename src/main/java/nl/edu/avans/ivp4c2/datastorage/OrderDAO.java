package nl.edu.avans.ivp4c2.datastorage;

import nl.edu.avans.ivp4c2.domain.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *This DAO retrieves the order data and creates an Order object.
 *This DAO uses the ProductDAO to fill the Order with products
 * @author IVP4C2
 */
public class OrderDAO {

	public OrderDAO() {
		// Nothing to be initialized. This is a stateless class. Constructor
		// has been added to explicitely make this clear.
	}
	
	/*Retrieves all orders for a given tableNumber
	 * @param int tableNumber
	 * @return ArrayList<Order> */
	public ArrayList<Order> getTableOrder(int tableNumber) {
		ArrayList<Order> orders = new ArrayList<Order>();

		// First open a database connnection
		DatabaseConnection connection = new DatabaseConnection();
		if (connection.openConnection()) {
			// If a connection was successfully setup, execute the SELECT
			// statement.
			//Select all orders for a given tableNumber
			ResultSet resultset = connection
					.executeSQLSelectStatement("SELECT b.bestellingNummer, b.tafelNummer, b.bestellingTijd, b.bestellingStatus "
							+ "FROM bestelling b INNER JOIN bestelling_product bp ON b.bestellingNummer = bp.bestellingNummer "
							+ "INNER JOIN product p ON bp.productNummer = p.productNummer WHERE b.tafelNummer = '"+tableNumber+"' GROUP BY bp.bestellingNummer;");
			
			if(resultset != null)
            {
                try
                {
                    while(resultset.next())
                    {
                    	//Create new Order Object for each record
                       Order newOrder = new Order(
                    		   resultset.getInt("bestellingNummer"),
                    		   resultset.getString("bestellingStatus"),
                    		   resultset.getString("bestellingTijd")
                    		   );
                       
                       //Create new ProductDAO to retrieve and create Product Object for the order
                       ProductDAO productDAO = new ProductDAO();
                       ArrayList<Product> products = productDAO.getProduct(resultset.getInt("bestellingNummer")); //Returns an ArrayList with Products
                       for(Product p : products) { //Add product to the new Order
                    	   newOrder.addProduct(p);
                       }
                    }
                    
                }
                catch(SQLException e)
                {
                    System.out.println(e);
                    orders = null;
                }
            }

            connection.closeConnection();
        }
		//Return orders ArrayList to be used in TableDAO
		return orders;
	}
}
