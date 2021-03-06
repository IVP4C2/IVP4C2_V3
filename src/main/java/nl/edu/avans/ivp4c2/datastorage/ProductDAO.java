package nl.edu.avans.ivp4c2.datastorage;

import nl.edu.avans.ivp4c2.datastorage.DatabaseConnection;
import nl.edu.avans.ivp4c2.domain.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Database Access Object for the Product class. Handles all operation regarding the Product class in which a database is used
 * @author IVP4C2
 */
public class ProductDAO {

	public ProductDAO() {
		// Nothing to be initialized. This is a stateless class. Constructor
		// has been added to explicitely make this clear.
	}

	/**
	 * Retrieves products for an Order. Takes an orderNumber as parameter
	 * Returns a List containing products
	 * @param orderNumber number of the order
	 * @return A list containing products
	 */
	public List<Product> getProductViaOrder(int orderNumber) {
		String statement = "SELECT * FROM `selectitems_v` WHERE `order_id` = '"+orderNumber+"';";
		return getProduct(statement);
	}

	/**
	 * Retrieves products for a Payment. Takes a PaymentNumber as parameter
	 * Returns a List containing products
	 * @param billId is the id of the bill
	 * @return A list containing products
	 */
	public List<Product> getProductViaBill(int billId) {
		String statement = "SELECT `item_id`, `name`, `price`, `t`.`percents`, COUNT(*) AS amount " +
				"FROM `item` `i` INNER JOIN `kpt_orderline` `kol` ON `i`.`item_id` = `kol`.`fk_item_id` " +
				"INNER JOIN `order` `o` ON `kol`.`fk_order_id` = `o`.`order_id` " +
				"INNER JOIN `kpt_billed_order` `kbo` ON `kbo`.`fk_order_id` = `o`.`order_id` " +
				"INNER JOIN `tax` `t` ON `t`.`tax_id` = `i`.`fk_tax_id` " +
				"WHERE `kbo`.`fk_bill_id` = '"+billId+"' " +
				"AND `o`.`fk_status_id` != '5' " +
				"GROUP BY `item_id`;";
		return getProduct(statement);
	}

	/**
	 * Retrieves products from the database using the query given as a parameter.
	 * Returns a List containing products
	 * @param statement an SQL query as String.
	 * @return A list containing products
	 */
	private static final List<Product> getProduct(String statement) {
		
		List<Product> products = new ArrayList<Product>();
		//Open db connection
		DatabaseConnection connection = new DatabaseConnection();
		if(connection.openConnection()) {
			//connection opened succesfully
			//execute SQL statement to retrieve Tables
			//Select all product for a given orderNumber
			ResultSet resultset = connection.executeSQLSelectStatement(statement);

	            if(resultset != null) {
	                try {
	                    while(resultset.next()) {
	                    	//Create a new Product for each record
	                       Product newProduct = new Product(
								   resultset.getInt("item_id"),
								   resultset.getString("name"),
								   resultset.getInt("amount"),
								   resultset.getDouble("price"),
								   resultset.getInt("percents"));
	                       products.add(newProduct); //Add newProduct to product ArrayList
		                }       
	                } catch(SQLException e) {
	                    System.out.println(e);products = null;
	                }
				}
			connection.closeConnection();
		}
		//Return products ArrayList to be used in the OrderDAO
		return products;
	}
}
